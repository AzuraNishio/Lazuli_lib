package nishio.lazuli_lib.core.framebuffers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.shaders.LazuliBlendMode;
import nishio.lazuli_lib.core.shaders.LazuliFramebufferShader;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.Lazuli_Lib;
import org.joml.Vector4f;

public class LazuliFramebufferUtills {
    private static Framebuffer swap;
    private static Framebuffer swap2;
    private static int resX;
    private static int resY;
    public static LazuliFramebufferShader copy;

    public static Framebuffer getSwapBuffer(){
        if (swap == null){
            MinecraftClient cli = MinecraftClient.getInstance();
            swap = new SimpleFramebuffer(cli.getFramebuffer().viewportWidth, cli.getFramebuffer().viewportHeight, true, true);
        }
        return swap;
    }

    public static Framebuffer getSwapBuffer2(){
        if (swap2 == null){
            MinecraftClient cli = MinecraftClient.getInstance();
            swap2 = new SimpleFramebuffer(cli.getFramebuffer().viewportWidth, cli.getFramebuffer().viewportHeight, true, true);
        }
        return swap2;
    }

    public static Framebuffer copyToSwap(Framebuffer in){
        getSwapBuffer().clear(false);
        copy.renderToFramebuffer(0, in, getSwapBuffer());
        return getSwapBuffer();
    }

    public static Framebuffer copyToSwap2(Framebuffer in){
        getSwapBuffer().clear(false);
        copy.renderToFramebuffer(0, in, getSwapBuffer2());
        return getSwapBuffer2();
    }

    public static Framebuffer copyFromSwap(Framebuffer out){
        copy.renderToFramebuffer(0, getSwapBuffer(), out);
        return getSwapBuffer();
    }

    public static void register() {
        copy = new LazuliFramebufferShader(
                Identifier.of(Lazuli_Lib.MOD_ID, "raw_copy")
        ).addDefaultUniforms().register();

        LazuliShaderRegistry.close();

        ClientTickEvents.START_CLIENT_TICK.register((t) ->{

            Framebuffer main = MinecraftClient.getInstance().getFramebuffer();

            if (resX != main.viewportWidth || resY != main.viewportHeight) {
                resX = main.viewportWidth;
                resY = main.viewportHeight;
                if (swap != null){
                    swap.resize(resX, resY, false);
                    swap2.resize(resX, resY, false);
                }
            }
        });
    }
}
