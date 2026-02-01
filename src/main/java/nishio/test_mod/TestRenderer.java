package nishio.test_mod;
/** Renderer utility for the test mod. */

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.*;
import nishio.lazuli_lib.core.LapisRenderer;
import nishio.lazuli_lib.core.LazuliBufferBuilder;
import nishio.lazuli_lib.internals.Lazuli_Lib;

import java.util.Random;

public class TestRenderer {
    static Vec3d head = new Vec3d(0, 100, 340);
    static Vec3d dir = new Vec3d(0, 0, 1);
    private static final Random RANDOM   = new Random();
    public static void register(){
        LazuliClock.Cronometer cronometer = LazuliClock.newCronometer();
        LazuliRenderingRegistry.registerRenderCallback((context) -> {
            LazuliBufferBuilder bb = context.getLazuliBB(VertexFormat.DrawMode.QUADS);

            LapisRenderer.setShader(TestModShaders.testShader.getProgram());

            TestModShaders.testShader.setSampler("Sampler0", Lazuli_Lib.id("icon.png"));

            TestModShaders.testShader.setUniform("Test2", new Vec3d(0,1,Math.sin(cronometer.readLerpedSeconds())));
            TestModShaders.testShader.setUniform("Test", new Vec3d(Math.sin(cronometer.readLerpedSeconds() * 1.4),1,1));

            LazuliVertex v1 = new LazuliVertex().pos(0, 100, 0).color(1f, 0f,0f,1f);
            LazuliVertex v2 = new LazuliVertex().pos(20, 100, 0).color(1f, 0f,0f,1f);
            LazuliVertex v3 = new LazuliVertex().pos(20, 100, 20).color(0f, 1f,0f,1f);
            LazuliVertex v4 = new LazuliVertex().pos(0, 100, 20).color(0f, 1f,0f,1f);

            bb.addVertex(v1).addVertex(v2).addVertex(v3).addVertex(v4);

            bb.draw();
        });

        LazuliRenderingRegistry.registerPostCallback((context, viewProjMatrix, tickDelta) -> {
            //LazuliShaderRegistry.getPostProcessor(TestModShaders.CONTRAST).render(tickDelta);
        });
    }



    private static Vec3d randomDir() {
        double angle = RANDOM.nextDouble() * Math.PI * 2.0;
        return new Vec3d(Math.cos(angle), 0, Math.sin(angle)).normalize();
    }


}
