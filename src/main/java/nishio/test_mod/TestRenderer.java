package nishio.test_mod;
/** Renderer utility for the test mod. */

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.framebuffers.LazuliFramebufferUtills;
import nishio.lazuli_lib.core.miscellaneous.LazuliMathUtils;
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
    static Framebuffer BUFFER_1;// = new SimpleFramebuffer(2000, 2000, false, false);
    static Framebuffer BUFFER_2;// = new SimpleFramebuffer(2000, 2000, false, false);
    static boolean activeBuffer = true;
    static Framebuffer main;// = new SimpleFramebuffer(128, 128, false, false);
    static boolean beggining = true;
    static float cicle = 0;
    static Framebuffer mainB;
    static Framebuffer outB;

    private static final Random RANDOM   = new Random();
    public static void register(){
        LazuliClock.Cronometer cronometer = LazuliClock.newCronometer();
        LazuliRenderEvents.registerRenderCallback((context) -> {


        });


        LazuliRenderEvents.registerRenderCallback((context) -> {
            LazuliBufferBuilder bb = context.getLazuliBB(VertexFormat.DrawMode.QUADS);

            LapisRenderer.setShader(TestModShaders.RIPPLES_GEOMETRY_SHADER);



            TestModShaders.RIPPLES_GEOMETRY_SHADER.setSampler("Sampler0", Lazuli_Lib.id("icon.png"));


            if(BUFFER_1 != null){
                mainB.beginRead();
                RenderSystem.setShaderTexture(0, mainB.getColorAttachment());
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

            if(BUFFER_1 != null) {
                mainB.endRead();
            }

        });

        LazuliRenderEvents.registerPostCallback((context, viewProjMatrix, tickDelta) -> {
            if(BUFFER_1 == null){
                BUFFER_1 = new SimpleFramebuffer(3200, 3200, true, true);
                BUFFER_2 = new SimpleFramebuffer(3200, 3200, true, true);
            }

            if(BUFFER_1 != null) {
                if (activeBuffer) {
                    mainB = BUFFER_1;
                    outB = BUFFER_2;
                } else {
                    mainB = BUFFER_2;
                    outB = BUFFER_1;
                }



                Framebuffer main = MinecraftClient.getInstance().getFramebuffer();

                MinecraftClient minecraftClient = MinecraftClient.getInstance();

                if (minecraftClient.player != null) {
                    if(minecraftClient.player.getPos().getY() < 100.1) {
                        Vec2f pos = new Vec2f((float) (minecraftClient.player.getPos().x / 20f), (float) minecraftClient.player.getPos().z / 20f);
                        Vec2f[] base = LazuliMathUtils.Rectangle2dFromCenter(pos, 0.05f, 0.1f, minecraftClient.player.bodyYaw);
                        TestModShaders.WHITE_SHADER.renderToFramebuffer(0, BUFFER_1, base[0], base[1], base[2], base[3]);
                    }
                }
                cicle += context.tickCounter().getTickDelta(false);
                if (cicle > 1/24f) {
                    cicle = 0;
                    main.endWrite();

                    TestModShaders.RIPPLES_FRAMEBUFFER_SHADER.renderToFramebuffer(tickDelta, outB, mainB);

                    activeBuffer = !activeBuffer;


                    main.beginWrite(true);
                }

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
