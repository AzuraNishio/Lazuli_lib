package nishio.test_mod;

import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformFloat3v;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.events.LazuliRenderEvents;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.shaders.LazuliBlendMode;
import nishio.lazuli_lib.core.shaders.LazuliFramebufferShader;
import nishio.lazuli_lib.core.shaders.LazuliShader;
import nishio.lazuli_lib.core.tools.LazuliShaderDevTools;
import nishio.lazuli_lib.core.warp.LazuliWarp;
import nishio.lazuli_lib.core.warp.LazuliWarpDefaultTargets;
import nishio.lazuli_lib.internals.stuff.LazuliMinecraftShaderGetter;

public class TestModShaders {
    public static LazuliShader RIPPLES_GEOMETRY_SHADER;
    public static LazuliFramebufferShader RIPPLES_FRAMEBUFFER_SHADER;
    public static LazuliFramebufferShader RED_FRAMEBUFFER_SHADER;
    public static LazuliFramebufferShader WHITE_SHADER;

    public static void registerShaders() {
        LazuliShaderDevTools.enableFastShaderReloading();
        LazuliShaderDevTools.enableGlCompilerErrorMessages();

        RIPPLES_GEOMETRY_SHADER = new LazuliShader(
                Identifier.of(TestModClient.MOD_ID, "ripple_geometry")
        ).addSampler("Sampler0").addDefaultUniforms().setBlendMode(LazuliBlendMode.ADDITIVE).register();


        RIPPLES_FRAMEBUFFER_SHADER = new LazuliFramebufferShader(
                Identifier.of(TestModClient.MOD_ID, "ripple")
        ).addDefaultUniforms().setBlendMode(LazuliBlendMode.DEFAULT).register();

        RED_FRAMEBUFFER_SHADER = new LazuliFramebufferShader(
                Identifier.of(TestModClient.MOD_ID, "red")
        ).addDefaultUniforms().setBlendMode(LazuliBlendMode.ADDITIVE).register();

//        LazuliRenderEvents.registerPostCallback((context, viewProjectionMatrix, tickDelta) -> {
//            RED_FRAMEBUFFER_SHADER.renderToScreen();
//        });


        WHITE_SHADER = new LazuliFramebufferShader(
                Identifier.of(TestModClient.MOD_ID, "white")
        ).addDefaultUniforms().setBlendMode(LazuliBlendMode.ADDITIVE).register();

        LazuliWarp waves = new LazuliWarp(Identifier.of(TestModClient.MOD_ID, "inject")).addTargets(LazuliWarpDefaultTargets.WORLD_TERRAIN).register();
        //LazuliWarp waves2 = new LazuliWarp(Identifier.of(TestModClient.MOD_ID, "inject2")).addTargets(LazuliWarpDefaultTargets.WORLD_TERRAIN).register();

        LazuliRenderEvents.registerRenderCallback(ctx ->{
            LazuliMinecraftShaderGetter.setVanillaShaderUniforms("epicenter", new Vec3d(0, 40, 0).subtract(ctx.camera().getPos()));
        });


        LazuliShaderRegistry.close();
    }
}