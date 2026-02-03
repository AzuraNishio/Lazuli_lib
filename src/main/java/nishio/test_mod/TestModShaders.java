package nishio.test_mod;

import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.shaders.LazuliBlendMode;
import nishio.lazuli_lib.core.shaders.LazuliShader;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.Lazuli_Lib;
import nishio.lazuli_lib.internals.LazulidefaultUniforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestModShaders {
    public static String RENDER_TYPE_TEST = "rendertype_test";
    public static String RENDER_TYPE_TEST2 = "rendertype_test2";

    public static final String CONTRAST = "shaders/post/contrast.json";
    public static final String EASY_CONTRAST = "shaders/program/contrast.json";
    public static LazuliShader testShader;
    public static LazuliShader testShader2;



    public static void registerShaders() {

        LazuliShaderRegistry.registerPostProcessingShader(CONTRAST, Lazuli_Lib.MOD_ID);

        Map<String, LazuliUniform<?>> uniforms = new HashMap<>();
        List<String> samplers = new ArrayList<>();
        uniforms.put("state", new LazuliUniform<Vec3d>("state", new Vec3d(2, 0, 0)));
        LazulidefaultUniforms.addDefaultUniforms(uniforms);
        samplers.add("Sampler0");

        testShader = new LazuliShader(
                Identifier.of(TestModClient.MOD_ID, RENDER_TYPE_TEST),
                Identifier.of(TestModClient.MOD_ID, RENDER_TYPE_TEST),
                Identifier.of(TestModClient.MOD_ID, RENDER_TYPE_TEST),
                LazuliBlendMode.DEFAULT,
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                uniforms,
                samplers
        );

        LazuliShaderRegistry.registerShader(testShader);

        testShader2 = new LazuliShader(
                Identifier.of(TestModClient.MOD_ID, RENDER_TYPE_TEST2),
                Identifier.of(TestModClient.MOD_ID, RENDER_TYPE_TEST2),
                Identifier.of(TestModClient.MOD_ID, "full_screen"),
                LazuliBlendMode.ADDITIVE,
                VertexFormats.POSITION_TEXTURE,
                uniforms,
                samplers
        );

        LazuliShaderRegistry.registerShader(testShader2);

        LazuliShaderRegistry.close();





    }

}