package nishio.lazuli_lib.core;
/** Main entry for hooking render callbacks. */

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4d;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class LazuliRenderingRegistry {

    private static Camera camera;
    private static Matrix4f customViewProj;
    private static final List<LazuliRenderEvents.LazuliRenderCallback> RENDER_CALLBACKS = new CopyOnWriteArrayList<>();
    private static final List<LazuliRenderEvents.LazuliPostCallback> POST_CALLBACKS = new CopyOnWriteArrayList<>();
    public static final AtomicReference<Float> time = new AtomicReference<>(0f);
    private static boolean wasZooming = false;
    private static Matrix4f matrix4f;


    public static final Identifier CELESTIAL_SYNC =
            Identifier.of("new_horizons", "celestial_sync");

    public static void registerLazuliRenderPhases() {
        WorldRenderEvents.LAST.register(context -> {
            camera = context.camera();
            matrix4f = context.positionMatrix();
            if (camera == null || matrix4f == null) return;

            float tickDelta = context.tickCounter().getTickDelta(true);

            Tessellator tessellator = Tessellator.getInstance();
            time.updateAndGet(v -> v + tickDelta);

            //Matrix transformations! Yayyyyyyyyyyyyyyyyyyyyyy
            MatrixStack ms = new MatrixStack();
            ms.multiplyPositionMatrix(matrix4f);
            ms.push();
            ms.multiply(camera.getRotation());
            Matrix4f viewProj = ms.peek().getPositionMatrix();

            //Setup user friendly render state
            LapisRenderer.disableCull();
            LapisRenderer.enableDepthTest();
            LapisRenderer.setShaderColor(1f,1f,1f,1f);

            //run registered render phases
            for (LazuliRenderEvents.LazuliRenderCallback callback : RENDER_CALLBACKS) {
                callback.render(new LazuliRenderContext(viewProj, context, tickDelta));
            }

            // 6) Restore vanilla render state
            LapisRenderer.disableBlend();
            LapisRenderer.setShaderColor(1f, 1f, 1f, 1f);
            LapisRenderer.depthMask(true);
            LapisRenderer.setShaderFogShape(FogShape.CYLINDER);
            LapisRenderer.setShaderFogColor(0f, 0f, 0f);
            LapisRenderer.setShader(GameRenderer::getPositionColorProgram);
            LapisRenderer.enableDepthTest();

            for (LazuliRenderEvents.LazuliPostCallback callback : POST_CALLBACKS) {
                callback.post(context, customViewProj, tickDelta);
            }
        });
    }

    /** Register a custom geometry rendering callback */
    public static void registerRenderCallback(LazuliRenderEvents.LazuliRenderCallback callback) {
        RENDER_CALLBACKS.add(callback);
    }

    /** Register a custom post rendering callback (your planets, rings, etc.). */
    public static void registerPostCallback(LazuliRenderEvents.LazuliPostCallback callback) {
        POST_CALLBACKS.add(callback);
    }

    private static Matrix4f convertMatrix4dToMatrix4f(Matrix4d d) {
        return new Matrix4f(
                (float)d.m00(), (float)d.m01(), (float)d.m02(), (float)d.m03(),
                (float)d.m10(), (float)d.m11(), (float)d.m12(), (float)d.m13(),
                (float)d.m20(), (float)d.m21(), (float)d.m22(), (float)d.m23(),
                (float)d.m30(), (float)d.m31(), (float)d.m32(), (float)d.m33()
        );
    }

}
