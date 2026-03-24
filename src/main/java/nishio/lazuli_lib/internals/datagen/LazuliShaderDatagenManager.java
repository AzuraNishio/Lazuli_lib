package nishio.lazuli_lib.internals.datagen;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.data.DataGenerator;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.tools.LazuliShaderDevTools;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.lazuli_lib.internals.LazuliShaderTop;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

import static net.minecraft.datafixer.fix.BlockEntitySignTextStrictJsonFix.GSON;

public class LazuliShaderDatagenManager {

    private static final List<LazuliShaderTop<?>> shaders = new ArrayList<>();
    private static final Map<String, String> files = new HashMap<>();

    private static void copyResourceToGenerated(Identifier id, String pathString, String extension, Path lazuliGen){
        InputStream stream = null;
        String path = "assets/".concat(id.getNamespace()).concat(pathString).concat(id.getPath()).concat(extension);



        if (FabricLoader.getInstance().isDevelopmentEnvironment()){
            Path PATH = FabricLoader.getInstance().getGameDir().getParent().resolve("src/main/resources/assets/".concat(id.getNamespace()).concat(pathString).concat(id.getPath()).concat(extension));
            LazuliLog.Warp.info("Copying file for fast reloading: ".concat(PATH.toString()));
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
            FileUtils.createParentDirectories(lazuliGen.resolve(path).toFile());
            Files.writeString(lazuliGen.resolve(path), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            LazuliLog.Warp.error("Failed to copy file %s".formatted(id.toString()));
            e.printStackTrace();
        }
    }

    public static void copyFastReloadShaders(Path lazuliGen){
        Set<Identifier> alreadyCopiedFragment = new HashSet<>();
        Set<Identifier> alreadyCopiedVertex = new HashSet<>();
        for (LazuliShaderTop<?> shader : shaders){
            if(shader.doFastReloading) {
                if (shader.fragmentId.getNamespace() == "minecraft" || shader.fragmentId.getNamespace() == "lazuli_lib"){
                    alreadyCopiedFragment.add(shader.fragmentId);
                }

                if (shader.vertexId.getNamespace() == "minecraft" || shader.vertexId.getNamespace() == "lazuli_lib"){
                    alreadyCopiedVertex.add(shader.vertexId);
                }

                if (!alreadyCopiedFragment.contains(shader.fragmentId)) {
                    copyResourceToGenerated(shader.fragmentId, shader.basePath(), ".fsh", lazuliGen);
                    alreadyCopiedFragment.add(shader.fragmentId);
                }

                if (!alreadyCopiedVertex.contains(shader.vertexId)) {
                    copyResourceToGenerated(shader.vertexId, shader.basePath(), ".fsh", lazuliGen);
                    alreadyCopiedVertex.add(shader.vertexId);
                }
            }
        }
    }
    public static void reload(){ //Regenerate everything if needed
        gen();
        for (LazuliShaderTop<?> s : shaders){
            s.reload(MinecraftClient.getInstance().getResourceManager());
        }
        MinecraftClient.getInstance().reloadResourcesConcurrently();
        MinecraftClient.getInstance().setOverlay(null);
        MinecraftClient.getInstance().player.sendMessage(Text.of("§d Reloaded shaders!"), true);
    }
    public static void initialize(){ //Register the new resources supplier
        var loader = FabricLoader.getInstance();

        Path projectRoot = FabricLoader.getInstance().getGameDir();
        Path lazuli_gen_path = projectRoot.resolve("lazuli_gen/");
        try {
            FileUtils.delete(lazuli_gen_path.toFile());
            Files.createDirectory(lazuli_gen_path);
            createMetadataAndLogo(lazuli_gen_path);
        } catch (IOException ignored) { }

        ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();

        manager.providers.add(new LazuliResourcePackProvider(lazuli_gen_path));
    }

    public static void registerShader(LazuliShaderTop s){
        shaders.add(s);
    }


    public static void gen(){

        var loader = FabricLoader.getInstance();

        //Path and stuff setup
        Path projectRoot = FabricLoader.getInstance().getGameDir();
        Path lazuli_gen_path = projectRoot.resolve("lazuli_gen/");
        try {
            FileUtils.deleteDirectory(lazuli_gen_path.toFile());
            LazuliWarpManager.generate();
        } catch (IOException ignored) {

        }

        DataGenerator generator =
                new DataGenerator(lazuli_gen_path, SharedConstants.getGameVersion(), true);

        DataGenerator.Pack pack =
                generator.createVanillaPack(true);


        pack.addProvider((op) -> new LazuliShaderGenerator(op, shaders) {
            @Override
            public String getName() {
                    return "Lazuli Shader Datagen";
                }
        });



        try {
            generator.run();
            createMetadataAndLogo(lazuli_gen_path); //create metadata again if generator ran

            if (LazuliShaderDevTools.doFastReload){
                copyFastReloadShaders(lazuli_gen_path);
            }

            LazuliWarpManager.WriteWarpShaders(lazuli_gen_path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    static private void createMetadataAndLogo(Path path) throws IOException {
        Path metaPath = path.resolve("pack.mcmeta");

        JsonObject meta = new JsonObject();
        JsonObject packmetadata = new JsonObject();
        packmetadata.addProperty("pack_format", 15);
        packmetadata.addProperty("description", "Lazuli resources uwu");
        meta.add("pack", packmetadata);

        Files.createDirectories(metaPath.getParent());
        Files.writeString(metaPath, GSON.toJson(meta));

        Path logoPath = path.resolve("pack.png");
        try (var logoStream = LazuliShaderDatagenManager.class.getResourceAsStream("/assets/lazuli_lib/icon.png")) {
            if (logoStream != null) {
                Files.copy(logoStream, logoPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }

    }

}
