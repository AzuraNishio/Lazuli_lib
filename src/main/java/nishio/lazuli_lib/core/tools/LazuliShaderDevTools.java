package nishio.lazuli_lib.core.tools;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import nishio.lazuli_lib.internals.LazuliLog;
import nishio.lazuli_lib.internals.datagen.LazuliShaderDatagenManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LazuliShaderDevTools {
    private static boolean wasFocused = true;
    public static boolean doFastReload = false;
    public static void enableFastShaderReloading(){
        doFastReload = true;
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ClientTickEvents.START_CLIENT_TICK.register(t -> {

                if (MinecraftClient.getInstance().isWindowFocused() && !wasFocused) {
                    if (MinecraftClient.getInstance().world != null){
                        LazuliShaderDatagenManager.reload();
                    }
                }
                wasFocused = MinecraftClient.getInstance().isWindowFocused();
            });
        }
    }

    public static void enableGlCompilerErrorMessages(){
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            GL43.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
                if (type == GL43.GL_DEBUG_TYPE_ERROR) {
                    String msg = GLDebugMessageCallback.getMessage(length, message);
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.sendMessage(Text.of("§eShader compilation error: §c".concat(msg)), false);
                    } else {
                        LazuliLog.Shaders.error("Shader compilation error: ".concat(msg));
                    }
                }
            }, 0L);
        });
    }
}
