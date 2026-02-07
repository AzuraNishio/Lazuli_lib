package nishio.lazuli_lib.internals.datagen;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.data.DataGenerator;
import net.minecraft.resource.ResourcePackManager;
import nishio.lazuli_lib.internals.LazuliResourcePackProvider;
import nishio.lazuli_lib.internals.LazuliShaderTop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.datafixer.fix.BlockEntitySignTextStrictJsonFix.GSON;

public class LazuliShaderDatagenManager {

    private static final List<LazuliShaderTop<?>> shaders = new ArrayList<>();

    public static void initialize(){ //On load create the resource pack and register it in case it is empty
        var loader = FabricLoader.getInstance();

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

    public static void registerShader(LazuliShaderTop s){
        shaders.add(s);
    }


    public static void gen(){
        var loader = FabricLoader.getInstance();


        //Path and stuff setup
        Path projectRoot = FabricLoader.getInstance().getGameDir();
        Path lazuli_gen_path = projectRoot.resolve("lazuli_gen/");

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
        try (var logoStream = LazuliShaderDatagenManager.class
                .getResourceAsStream("/assets/lazuli_lib/icon.png")) {
            if (logoStream != null) {
                Files.copy(logoStream, logoPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }

    }

}
