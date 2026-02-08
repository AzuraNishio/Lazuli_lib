package nishio.test_mod;

import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.shaders.LazuliFramebufferShader;
import nishio.lazuli_lib.core.shaders.LazuliShader;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.LazulidefaultUniforms;
import org.joml.Vector2f;

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

        LazuliShaderRegistry.close();
    }
}