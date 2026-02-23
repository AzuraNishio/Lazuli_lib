package nishio.test_mod;

import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.shaders.LazuliBlendMode;
import nishio.lazuli_lib.core.shaders.LazuliFramebufferShader;
import nishio.lazuli_lib.core.shaders.LazuliShader;
import nishio.lazuli_lib.core.warp.LazuliWarp;
import nishio.lazuli_lib.core.warp.LazuliWarpDefaultTargets;

public class TestModShaders {
    public static LazuliShader RIPPLES_GEOMETRY_SHADER;
    public static LazuliFramebufferShader RIPPLES_FRAMEBUFFER_SHADER;
    public static LazuliFramebufferShader WHITE_SHADER;

    public static void registerShaders() {
        RIPPLES_GEOMETRY_SHADER = new LazuliShader(
                Identifier.of(TestModClient.MOD_ID, "ripple_geometry")
        ).addSampler("Sampler0").addDefaultUniforms().register();


        RIPPLES_FRAMEBUFFER_SHADER = new LazuliFramebufferShader(
                Identifier.of(TestModClient.MOD_ID, "ripple")
        ).addDefaultUniforms().setBlendMode(LazuliBlendMode.ADDITIVE).register();


        WHITE_SHADER = new LazuliFramebufferShader(
                Identifier.of(TestModClient.MOD_ID, "white")
        ).addDefaultUniforms().setBlendMode(LazuliBlendMode.ADDITIVE).register();

        LazuliWarp red = new LazuliWarp(Identifier.of(TestModClient.MOD_ID, "inject")).register().addTargets(LazuliWarpDefaultTargets.WORLD_TERRAIN);

        LazuliShaderRegistry.close();
    }
}