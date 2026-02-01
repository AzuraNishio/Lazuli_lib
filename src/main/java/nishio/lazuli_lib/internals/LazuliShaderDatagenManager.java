package nishio.lazuli_lib.internals;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.data.DataGenerator;
import net.minecraft.resource.ResourcePackManager;
import nishio.lazuli_lib.core.shaders.LazuliShader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil.GSON;

public class LazuliShaderDatagenManager {

    private static final List<LazuliShader> shaders = new ArrayList<>();

    public static void initialize(){ //On load create the resource pack and register it in case it is empty
        var loader = FabricLoader.getInstance();

        if (!loader.isDevelopmentEnvironment()) return;

        Path projectRoot = FabricLoader.getInstance().getGameDir();
        Path lazuli_gen_path = projectRoot.resolve("lazuli_gen/");
        ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();

        try {
            createMetadataAndLogo(lazuli_gen_path);
            manager.providers.add(new LazuliResourcePackProvider(lazuli_gen_path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void registerShader(LazuliShader s){
        shaders.add(s);
    }


    public static void genNamespace(String namespace){
        var loader = FabricLoader.getInstance();

        if (!loader.isDevelopmentEnvironment()) return;


        //Path and stuff setup
        Path projectRoot = FabricLoader.getInstance().getGameDir();
        Path lazuli_gen_path = projectRoot.resolve("lazuli_gen/");

        DataGenerator generator =
                new DataGenerator(lazuli_gen_path, SharedConstants.getGameVersion(), true);

        DataGenerator.Pack pack =
                generator.createVanillaPack(true);

        Map<String, List<LazuliShader>> shaderSets = new HashMap<>();

        //Separate shaders by namespace
        for (LazuliShader s : shaders){
            String ns = s.jsonId.getNamespace();
            if (!shaderSets.containsKey(ns)){
                shaderSets.put(ns, new ArrayList<>());
            }
            shaderSets.get(ns).add(s);
        }

        pack.addProvider((op) -> new LazuliDataGenerator(op, shaderSets.get(namespace)) {
            @Override
            public String getName() {
                    return namespace;
                }
        });


        try {
            generator.run();
            createMetadataAndLogo(lazuli_gen_path); //create metadata again if generator ran

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    static private void createMetadataAndLogo(Path path) throws IOException {
        Path metaPath = path.resolve("pack.mcmeta");

        JsonObject meta = new JsonObject();
        JsonObject packmetadata = new JsonObject();
        packmetadata.addProperty("pack_format", 34);
        packmetadata.addProperty("description", "Lazuli resources uwu");
        meta.add("pack", packmetadata);

        Files.createDirectories(metaPath.getParent());
        Files.writeString(metaPath, GSON.toJson(meta));

        Path logoPath = path.resolve("pack.png");
        if(!Files.exists(logoPath)){
            Path logo = path.getParent().getParent().resolve("src/main/resources/assets/lazuli_lib/icon.png");
            Files.copy(logo, logoPath);
        }

    }

}
