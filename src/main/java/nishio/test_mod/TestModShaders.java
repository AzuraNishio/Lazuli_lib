package nishio.test_mod;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.LazuliBlendMode;
import nishio.lazuli_lib.core.LazuliShader;
import nishio.lazuli_lib.core.LazuliShaderRegistry;
import nishio.lazuli_lib.core.LazuliUniform;
import nishio.lazuli_lib.internals.LazuliDataGenerator;
import nishio.lazuli_lib.internals.Lazuli_Lib;
import nishio.lazuli_lib.internals.LazulidefaultUniforms;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestModShaders {
    public static String RENDER_TYPE_TEST = "rendertype_test";

    public static final String CONTRAST = "shaders/post/contrast.json";
    public static final String EASY_CONTRAST = "shaders/program/contrast.json";
    public static LazuliShader testShader;



    public static void registerShaders() {

        LazuliShaderRegistry.registerPostProcessingShader(CONTRAST, Lazuli_Lib.MOD_ID);

        Map<String, LazuliUniform<?>> uniforms = new HashMap<>();
        List<String> samplers = new ArrayList<>();
        uniforms.put("Test", new LazuliUniform<Vec3d>("Test", new Vec3d(2, 0, 0)));
        uniforms.put("Test2", new LazuliUniform<Vec3d>("Test2", new Vec3d(0, 0.4, 0)));
        LazulidefaultUniforms.addDefaultUniforms(uniforms);
        samplers.add("Sampler0");

        testShader = new LazuliShader(
                TestModClient.MOD_ID,
                RENDER_TYPE_TEST,
                RENDER_TYPE_TEST,
                RENDER_TYPE_TEST,
                LazuliBlendMode.DEFAULT,
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                uniforms,
                samplers
        );

        var loader = FabricLoader.getInstance();

        List<LazuliShader> shaders = new ArrayList<>();
        shaders.add(testShader);

        if (!loader.isDevelopmentEnvironment()) return;

        //Path output = FabricLoader.getInstance()
        //        .getGameDir()
        //        .resolve("lazuli-generated");


        // Generate directly into src/main/resources
        Path projectRoot = FabricLoader.getInstance().getGameDir().getParent();
        Path output = projectRoot.resolve("src/main/lazuli_gen/");



        DataGenerator generator =
                new DataGenerator(output, SharedConstants.getGameVersion(), true);

        DataGenerator.Pack pack =
                generator.createVanillaPack(true);

        pack.addProvider((op) -> new LazuliDataGenerator(op, shaders) {
            @Override
            public String getName() {
                return "test_mod";
            }
        });


        try {
            generator.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //testShader.register();
    }

}