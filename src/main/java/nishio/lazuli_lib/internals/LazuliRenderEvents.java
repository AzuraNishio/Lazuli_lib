package nishio.lazuli_lib.internals;
/** Registers callbacks for world rendering. */

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import nishio.lazuli_lib.core.world_rendering.LazuliRenderContext;
import org.joml.Matrix4f;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LazuliRenderEvents {
    private static final List<LazuliRenderCallback> CALLBACKS = new CopyOnWriteArrayList<>();

    public static void register(LazuliRenderCallback callback) {
        CALLBACKS.add(callback);
    }

    @FunctionalInterface
    public interface LazuliRenderCallback {
        void render(LazuliRenderContext context);
    }

    @FunctionalInterface
    public interface LazuliPostCallback {
        void post(WorldRenderContext context, Matrix4f viewProjectionMatrix, float tickDelta);
    }



}
