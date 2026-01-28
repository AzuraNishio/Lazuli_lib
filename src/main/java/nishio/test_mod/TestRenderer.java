package nishio.test_mod;
/** Renderer utility for the test mod. */

import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.*;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class TestRenderer {
    static Vec3d head = new Vec3d(0, 100, 340);
    static Vec3d dir = new Vec3d(0, 0, 1);
    private static final Random RANDOM   = new Random();
    public static void register(){
        LazuliClock.Cronometer cronometer = LazuliClock.newCronometer();
        LazuliRenderingRegistry.registerRenderCallback((context) -> {
            float t = cronometer.readLerpedTicks();
            if (t > 20){
                cronometer.reset();
                head = new Vec3d(0, 100, 340);
            }

            LazuliBufferBuilder bb = context.getLazuliBB(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);
            LapisRenderer.enableCull();
            LapisRenderer.enableDepthTest();
            LapisRenderer.setShader(LazuliShaderRegistry.getShader(TestModShaders.RENDER_TYPE_ATMOSPHERE));

            LazuliVertex v1 = new LazuliVertex().pos(-5, 100, 300).color(1f,1f,0f,1f);
            LazuliVertex v2 = new LazuliVertex().pos(-5, 100, 290).color(1f,1f,0f,1f);
            LazuliVertex v3 = new LazuliVertex().pos(5, 100, 290).color(1f,1f,0f,1f);
            LazuliVertex v4 = new LazuliVertex().pos(5, 100, 300).color(1f,1f,0f,1f);

            bb.addVertex(v1).addVertex(v2).addVertex(v3).addVertex(v4).drawAndReset();

            bb.draw();

        });

        LazuliRenderingRegistry.registerPostCallback((context, viewProjMatrix, tickDelta) -> {
            LazuliShaderRegistry.getPostProcessor(TestModShaders.CONTRAST).render(tickDelta);
        });
    }



    private static Vec3d randomDir() {
        double angle = RANDOM.nextDouble() * Math.PI * 2.0;
        return new Vec3d(Math.cos(angle), 0, Math.sin(angle)).normalize();
    }


}
