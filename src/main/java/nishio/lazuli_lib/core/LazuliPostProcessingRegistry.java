package nishio.lazuli_lib.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LazuliPostProcessingRegistry {

    @FunctionalInterface
    public interface LazuliPostProcessingCallback {
        void register(MinecraftClient client, ResourceFactory factory);
    }

    private static final List<LazuliPostProcessingCallback> CALLBACKS = new CopyOnWriteArrayList<>();

    public static void register(LazuliPostProcessingCallback callback) {
        CALLBACKS.add(callback);
    }

    public static void runCallbacks(MinecraftClient client, ResourceFactory factory) {
        for (LazuliPostProcessingCallback callback : CALLBACKS) {
            callback.register(client, factory);
        }
    }
}
