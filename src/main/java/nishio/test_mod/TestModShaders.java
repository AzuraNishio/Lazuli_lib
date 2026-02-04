package nishio.test_mod;

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

    public static void registerShaders() {
        RIPPLES_GEOMETRY_SHADER = new LazuliShader(
                Identifier.of(TestModClient.MOD_ID, "rendertype_test")
        ).addSampler("Sampler0").addDefaultUniforms().register();


        RIPPLES_FRAMEBUFFER_SHADER = new LazuliFramebufferShader(
                Identifier.of(TestModClient.MOD_ID, "ripple")
        ).addUniform(new LazuliUniform<Vector2f>("Pos", new Vector2f(0,0))).addDefaultUniforms().addSampler("Sampler0").register();

        LazuliShaderRegistry.close();
    }
}