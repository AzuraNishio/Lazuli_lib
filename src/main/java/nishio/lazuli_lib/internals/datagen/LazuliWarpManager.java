package nishio.lazuli_lib.internals.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.shaders.LazuliShader;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.test_mod.TestModClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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


        for (LazuliTrueWarp warp : warps) {
            for (Identifier target : warp.targets) {
                LazuliLog.Warp.info("assets/%s/shaders/core/%s.json".formatted(target.getNamespace(), target.getPath()));
                try {
                    JsonElement shaderJson = LazuliEasyFileAcess.getVanillaPathJson("assets/%s/shaders/core/%s.json".formatted(target.getNamespace(), target.getPath()));

                    JsonObject json = shaderJson.getAsJsonObject();
                    LazuliShader shader = new LazuliShader(json, target).register();

                    for (LazuliUniform uni : warp.uniform){
                        shader.addUniform(uni);
                    }

                    String shaderFragment = LazuliEasyFileAcess.getVanillaPathString("assets/%s/shaders/core/%s.fsh".formatted(shader.fragmentId.getNamespace(), shader.fragmentId.getPath()));
                    shader.fragmentId = Identifier.of("warp_shaders", shader.fragmentId.getPath());
                    warp.fragments.put("%s.fsh".formatted(shader.fragmentId().getPath()), shaderFragment);

                    String shaderVertex = LazuliEasyFileAcess.getVanillaPathString("assets/%s/shaders/core/%s.vsh".formatted(shader.vertexId.getNamespace(), shader.vertexId.getPath()));
                    shader.vertexId = Identifier.of("warp_shaders", shader.vertexId.getPath());
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
            InputStream stream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("assets/".concat(warp.id.getNamespace()).concat("/shaders/warp/").concat(warp.id.getPath()).concat(".glsl"));

                try (var reader = new BufferedReader(new InputStreamReader(stream))) {
                    String content = reader.lines().collect(Collectors.joining("\n"));

                    LazuliWarpParser.parse(warp, content);
                    warp.print();
                } catch (IOException e) {
                    LazuliLog.Warp.error("Failed to read warp file %s".formatted(warp.id.toString()));
                }

        }
    }

    public static void WriteWarpShaders(Path path) {
        Path basePath = path.resolve("assets/warp_shaders/shaders/core");
        try {
            Files.createDirectories(basePath);
            for (LazuliTrueWarp warp: warps){
                Map<String, String> files = warp.generateFiles();
                for (String pathString : files.keySet()) {
                    String content = files.get(pathString);
                    Files.writeString(basePath.resolve(pathString), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                }
            }




        } catch (IOException e){
            LazuliLog.Warp.error("Failed");
            e.printStackTrace();
        }
    }
}
