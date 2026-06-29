package nishio.lazuli_lib.internals.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.shaders.LazuliShader;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.lazuli_lib.internals.compat.LazuliSodiumResourcePackCompat;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class LazuliWarpManager {
    public static List<LazuliTrueWarp> warps = new ArrayList<>();


    public static void registerWarp(LazuliTrueWarp warp){
        warps.add(warp);
    }

    public static void generate(){
        parseWarps();

        Map<String, LazuliShader> uniqueTargets = new HashMap<>();

        for (LazuliTrueWarp warp : warps) {
            for (Identifier target : warp.targets) {
                //LazuliLog.Warp.info("assets/%s/shaders/core/%s.json".formatted(target.getNamespace(), target.getPath()));
                try {
                    LazuliShader shader = null;
                    if (!uniqueTargets.containsKey(target.toString())) { //Create shader if it's a new target
                        JsonElement shaderJson = null;
                        if (target.getNamespace() == "sodium"){
                            JsonObject sodiumJson = new JsonObject();

                            sodiumJson.addProperty("vertex", target.toString());
                            sodiumJson.addProperty("fragment", target.toString());

                            // Adding Attributes
                            JsonArray attributesJson = new JsonArray();
                            sodiumJson.add("attributes", attributesJson);

                            // Adding Samplers
                            JsonArray samplersJson = new JsonArray();
                            sodiumJson.add("samplers", samplersJson);

                            // Adding Uniforms
                            JsonArray uniformJson = new JsonArray();
                            sodiumJson.add("uniforms", uniformJson);

                            shaderJson = sodiumJson;
                        } else {
                            shaderJson = LazuliEasyFileAcess.getGameAssetsPathJson("assets/%s/shaders/core/%s.json".formatted(target.getNamespace(), target.getPath()));
                        }

                        JsonObject json = shaderJson.getAsJsonObject();

                        shader = new LazuliShader(json, target).register();

                        shader.vertexId = Identifier.of("warp_shaders", shader.vertexId.getPath());
                        shader.fragmentId = Identifier.of("warp_shaders", shader.fragmentId.getPath());

                        shader.doFastReloading = false;

                        uniqueTargets.put(target.toString(), shader);

                    } else { //Else just add the new uniforms
                        shader = uniqueTargets.get(target.toString());
                    }

                    for (LazuliUniform uni : warp.uniform){
                        shader.addUniform(uni);
                    }
                    if (target.getNamespace() == "sodium") {
                        String shaderFragment = LazuliEasyFileAcess.getGameAssetsPathString("assets/%s/shaders/%s.fsh".formatted(target.getNamespace(), shader.fragmentId.getPath()));
                        warp.fragments.put("%s.fsh".formatted(Identifier.of("sodium", shader.fragmentId().getPath())), shaderFragment);

                        String shaderVertex = LazuliEasyFileAcess.getGameAssetsPathString("assets/%s/shaders/%s.vsh".formatted(target.getNamespace(), shader.vertexId.getPath()));
                        warp.vertexes.put("%s.vsh".formatted(Identifier.of("sodium", shader.vertexId.getPath())), shaderVertex);
                    } else {
                        String shaderFragment = LazuliEasyFileAcess.getGameAssetsPathString("assets/%s/shaders/core/%s.fsh".formatted(target.getNamespace(), shader.fragmentId.getPath()));
                        warp.fragments.put("%s.fsh".formatted(shader.fragmentId().toString()), shaderFragment);

                        String shaderVertex = LazuliEasyFileAcess.getGameAssetsPathString("assets/%s/shaders/core/%s.vsh".formatted(target.getNamespace(), shader.vertexId.getPath()));
                        warp.vertexes.put("%s.vsh".formatted(shader.vertexId.toString()), shaderVertex);
                    }


                } catch (IOException e) {
                    LazuliLog.Warp.warn("Failed to read %s!".formatted(target.getPath()));
                    e.printStackTrace();
                }
            }
        }
    }

    private static void parseWarps(){
        for (LazuliTrueWarp warp : warps){
            InputStream stream = null;
            String path = "assets/".concat(warp.id.getNamespace()).concat("/shaders/warp/").concat(warp.id.getPath()).concat(".glsl");

            if (FabricLoader.getInstance().isDevelopmentEnvironment()){
                Path PATH = FabricLoader.getInstance().getGameDir().getParent().resolve("src/main/resources/assets/".concat(warp.id.getNamespace()).concat("/shaders/warp/").concat(warp.id.getPath()).concat(".glsl"));
                LazuliLog.Warp.info("trying to load directly ".concat(PATH.toString()));
                try {
                    stream = Files.newInputStream(PATH);
                } catch (Exception e) {
                    e.printStackTrace();
                    LazuliLog.Warp.info("Could not directly load ".concat(path));
                    stream = Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream(path);
                }
            } else {
                stream = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(path);
            }


                try (var reader = new BufferedReader(new InputStreamReader(stream))) {
                    String content = reader.lines().collect(Collectors.joining("\n"));

                    LazuliWarpParser.parse(warp, content);
                    //warp.print();
                } catch (IOException e) {
                    LazuliLog.Warp.error("Failed to read warp file %s".formatted(warp.id.toString()));
                }

        }
    }

    public static void WriteWarpShaders(Path path) {
        Path basePath = path.resolve("assets/warp_shaders/shaders/core");
        Path baseSodium = path.resolve("assets/sodium/shaders/core");

        try {
            FileUtils.deleteDirectory(basePath.toFile());
            Files.createDirectories(basePath);
            if(LazuliSodiumResourcePackCompat.isIsSodiumLoaded()) {
                FileUtils.deleteDirectory(baseSodium.toFile());
                Files.createDirectories(baseSodium);
            }

            Map<String, String> modifiedFiles = new HashMap<>();

            for (LazuliTrueWarp warp: warps) {
                for (String id : modifiedFiles.keySet()) {
                    if (warp.fragments.containsKey(id)) {
                        warp.fragments.replace(id, modifiedFiles.get(id));
                    }
                    if (warp.vertexes.containsKey(id)) {
                        warp.vertexes.replace(id, modifiedFiles.get(id));
                    }
                }

                Map<String, String> generatedFiles = warp.generateFiles();

                modifiedFiles.putAll(generatedFiles);
            }

            try {
                for (String identifierString : modifiedFiles.keySet()) {
                    Identifier id = new Identifier(identifierString);
                    String content = modifiedFiles.get(identifierString);
                    basePath = path.resolve("assets/warp_shaders/shaders/core");
                    if (Objects.equals(id.getNamespace(), "sodium")){
                        basePath = path.resolve("assets/sodium/shaders");
                    }
                    try {
                        Files.createDirectory(basePath.resolve(id.getPath()).getParent());
                    } catch (IOException e) {}
                    Files.writeString(basePath.resolve(id.getPath()), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                }
            } catch (IOException e) {
                LazuliLog.Warp.error("Failed to write warped files for %s!".formatted(path.toString()));
                e.printStackTrace();
                throw e;
            }


        } catch (IOException e){
            LazuliLog.Warp.error("Failed to create warp %s!".formatted(path.toString()));
            e.printStackTrace();
        }
    }
}
