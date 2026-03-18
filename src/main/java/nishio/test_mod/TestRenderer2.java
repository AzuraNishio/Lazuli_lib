package nishio.test_mod;
/** Renderer utility for the test mod. */

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.events.LazuliRenderEvents;
import nishio.lazuli_lib.core.miscellaneous.LazuliClock;
import nishio.lazuli_lib.core.miscellaneous.LazuliMathUtils;
import nishio.lazuli_lib.core.world_rendering.LapisRenderer;
import nishio.lazuli_lib.core.world_rendering.LazuliBufferBuilder;
import nishio.lazuli_lib.core.world_rendering.LazuliVertex;
import nishio.lazuli_lib.internals.Lazuli_Lib;
import nishio.lazuli_lib.internals.stuff.LazuliMinecraftShaderGetter;

import java.util.Random;

public class TestRenderer2 {

    private static final Random RANDOM   = new Random();
    public static void register(){
        LazuliClock.Cronometer cronometer = LazuliClock.newCronometer();

        LazuliRenderEvents.registerPostCallback((context, viewProjectionMatrix, tickDelta) -> {
            TestModShaders.RED_FRAMEBUFFER_SHADER.renderToScreen();
        });

    }
}
