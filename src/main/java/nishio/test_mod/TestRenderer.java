package nishio.test_mod;
/** Renderer utility for the test mod. */

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.world_rendering.LapisRenderer;
import nishio.lazuli_lib.core.world_rendering.LazuliBufferBuilder;
import nishio.lazuli_lib.core.events.LazuliRenderEvents;
import nishio.lazuli_lib.core.miscellaneous.LazuliClock;
import nishio.lazuli_lib.core.world_rendering.LazuliVertex;
import nishio.lazuli_lib.internals.Lazuli_Lib;

import java.util.Random;

public class TestRenderer {
    static Vec3d head = new Vec3d(0, 100, 340);
    static Vec3d dir = new Vec3d(0, 0, 1);
    static Framebuffer testOut;// = new SimpleFramebuffer(2000, 2000, false, false);
    static Framebuffer testLast;// = new SimpleFramebuffer(2000, 2000, false, false);
    static Framebuffer main;// = new SimpleFramebuffer(128, 128, false, false);
    static boolean beggining = true;

    private static final Random RANDOM   = new Random();
    public static void register(){
        LazuliClock.Cronometer cronometer = LazuliClock.newCronometer();
        LazuliRenderEvents.registerRenderCallback((context) -> {


        });


        LazuliRenderEvents.registerRenderCallback((context) -> {
            LazuliBufferBuilder bb = context.getLazuliBB(VertexFormat.DrawMode.QUADS);

            LapisRenderer.setShader(TestModShaders.testShader);



            TestModShaders.testShader.setSampler("Sampler0", Lazuli_Lib.id("icon.png"));


            if(testOut != null){
                testOut.beginRead();
                RenderSystem.setShaderTexture(0, testOut.getColorAttachment());
            }

            //TestModShaders.testShader.setUniform("Test2", new Vec3d(0,1,Math.sin(cronometer.readLerpedSeconds())));
            //TestModShaders.testShader.setUniform("Test", new Vec3d(Math.sin(cronometer.readLerpedSeconds() * 1.4),1,1));
            RenderSystem.enableBlend();
            float n = 0.2f;

            for (float x = 0; x<20; x+= n) {
                for (float z = 0; z < 20; z += n) {
                    LazuliVertex v1 = new LazuliVertex().pos(x, 100.01, z).color(1f, 0f, 0f, 1f).uv(x / 20f, z / 20f);
                    LazuliVertex v2 = new LazuliVertex().pos(x + n, 100.01, z).color(1f, 0f, 0f, 1f).uv((x + n) / 20f, z / 20f);
                    LazuliVertex v3 = new LazuliVertex().pos(x + n, 100.01, z + n).color(0f, 1f, 0f, 1f).uv((x + n) / 20f, (z + n) / 20f);
                    LazuliVertex v4 = new LazuliVertex().pos(x, 100.01, z + n).color(0f, 1f, 0f, 1f).uv(x / 20f, (z + n) / 20f);

                    bb.addVertex(v1).addVertex(v2).addVertex(v3).addVertex(v4);
                }
            }

            bb.draw();

            if(testOut != null) {
                testOut.endRead();
            }

        });

        LazuliRenderEvents.registerPostCallback((context, viewProjMatrix, tickDelta) -> {
            if(testOut == null){
                testOut = new SimpleFramebuffer(2000, 2000, true, true);
                testLast = new SimpleFramebuffer(2000, 2000, true, true);
            }

            if(testOut != null) {
                Framebuffer main = MinecraftClient.getInstance().getFramebuffer();
                main.endWrite();
                LazuliShaderRegistry.getPostProcessor(TestModShaders.RIPPLE).render(tickDelta, testLast, testOut);
                LazuliShaderRegistry.getPostProcessor(TestModShaders.RIPPLE).render(tickDelta, testOut, testLast);
                main.beginWrite(true);
            } else {
                System.out.println("Yep it's nuull everyone");
            }
        });
    }



    private static Vec3d randomDir() {
        double angle = RANDOM.nextDouble() * Math.PI * 2.0;
        return new Vec3d(Math.cos(angle), 0, Math.sin(angle)).normalize();
    }


}
