package nishio.lazuli_lib.internals;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.data.DataGenerator;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.LazuliBlendMode;
import nishio.lazuli_lib.core.LazuliShader;
import nishio.lazuli_lib.core.LazuliUniform;
import nishio.test_mod.TestModClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LazuliShaderDatagenManager {

    private static final List<LazuliShader> shaders = new ArrayList<>();

    public static void registerShader(LazuliShader s){
        shaders.add(s);
    }


    public static void genNamespace(String namespace){
        var loader = FabricLoader.getInstance();

        if (!loader.isDevelopmentEnvironment()) return;


        //Path and stuff setup
        Path projectRoot = FabricLoader.getInstance().getGameDir().getParent();
        Path output = projectRoot.resolve("src/main/lazuli_gen/");

        DataGenerator generator =
                new DataGenerator(output, SharedConstants.getGameVersion(), true);

        DataGenerator.Pack pack =
                generator.createVanillaPack(true);



        Map<String, List<LazuliShader>> shaderSets = new HashMap<>();

        //Separate shaders by namespace
        for (LazuliShader s : shaders){
            String ns = s.namespace;
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void gen(){
        var loader = FabricLoader.getInstance();

        if (!loader.isDevelopmentEnvironment()) return;


        //Path and stuff setup
        Path projectRoot = FabricLoader.getInstance().getGameDir().getParent();
        Path output = projectRoot.resolve("src/main/lazuli_gen/");

        DataGenerator generator =
                new DataGenerator(output, SharedConstants.getGameVersion(), true);

        DataGenerator.Pack pack =
                generator.createVanillaPack(true);



        Map<String, List<LazuliShader>> shaderSets = new HashMap<>();

        //Separate shaders by namespace
        for (LazuliShader s : shaders){
            String ns = s.namespace;
            if (!shaderSets.containsKey(ns)){
                shaderSets.put(ns, new ArrayList<>());
            }
            shaderSets.get(ns).add(s);
        }

        for (String namespace : shaderSets.keySet()) {
            pack.addProvider((op) -> new LazuliDataGenerator(op, shaderSets.get(namespace)) {
                @Override
                public String getName() {
                    return namespace;
                }
            });
        }



        try {
            generator.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
