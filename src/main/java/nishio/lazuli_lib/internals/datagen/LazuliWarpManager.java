package nishio.lazuli_lib.internals.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.shaders.LazuliShader;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.lazuli_lib.internals.LazuliShaderTop;
import nishio.test_mod.TestModClient;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Target;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LazuliWarpManager {
    public static List<LazuliTrueWarp> warps = new ArrayList<>();


    public static void registerWarp(LazuliTrueWarp warp){
        warps.add(warp);
    }

    public static void generate(){
        parseWarps();
        LazuliShader test = new LazuliShader(
                Identifier.of(TestModClient.MOD_ID, "ripple_geometry2")
        ).addSampler("Sampler1").addDefaultUniforms().register();

        Map<String, LazuliShader> uniqueTargets = new HashMap<>();

        for (LazuliTrueWarp warp : warps) {
            for (Identifier target : warp.targets) {
                //LazuliLog.Warp.info("assets/%s/shaders/core/%s.json".formatted(target.getNamespace(), target.getPath()));
                try {
                    LazuliShader shader = null;
                    if (!uniqueTargets.containsKey(target.toString())) { //Create shader if it's a new target
                        JsonElement shaderJson = LazuliEasyFileAcess.getVanillaPathJson("assets/%s/shaders/core/%s.json".formatted(target.getNamespace(), target.getPath()));
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

                    String shaderFragment = LazuliEasyFileAcess.getVanillaPathString("assets/%s/shaders/core/%s.fsh".formatted(target.getNamespace(), shader.fragmentId.getPath()));
                    warp.fragments.put("%s.fsh".formatted(shader.fragmentId().getPath()), shaderFragment);

                    String shaderVertex = LazuliEasyFileAcess.getVanillaPathString("assets/%s/shaders/core/%s.vsh".formatted(target.getNamespace(), shader.vertexId.getPath()));
                    warp.vertexes.put("%s.vsh".formatted(shader.vertexId.getPath()), shaderVertex);







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
        try {
            FileUtils.deleteDirectory(basePath.toFile());
            Files.createDirectories(basePath);

            Map<String, String> modifiedFiles = new HashMap<>();

            for (LazuliTrueWarp warp: warps){
                for (String name : modifiedFiles.keySet()) {
                    if (warp.fragments.containsKey(name)) {
                        warp.fragments.replace(name, modifiedFiles.get(name));
                    }
                    if (warp.vertexes.containsKey(name)) {
                        warp.vertexes.replace(name, modifiedFiles.get(name));
                    }
                }

                Map<String, String> generatedFiles = warp.generateFiles();

                modifiedFiles.putAll(generatedFiles);
                for (String pathString : generatedFiles.keySet()) {
                    String content = generatedFiles.get(pathString);
                    Files.writeString(basePath.resolve(pathString), LazuliLibShaderLanguageParser.parseToGLSL(content), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                }
            }

        } catch (IOException e){
            LazuliLog.Warp.error("Failed");
            e.printStackTrace();
        }
    }
}
