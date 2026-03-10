package nishio.lazuli_lib.core.tools;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import nishio.lazuli_lib.internals.datagen.LazuliShaderDatagenManager;
import org.lwjgl.glfw.GLFW;

public class LazuliAutoWarpReload {
    private static boolean wasFocused = true;
    public static void enable(){
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
}
