package lazuli_lib.lazuli.core.mixin;

import lazuli_lib.lazuli.acess.data_containers.LazuliLine;
import lazuli_lib.lazuli.acess.data_containers.LazuliLineBuffer;
import lazuli_lib.lazuli.acess.data_containers.TriangleBuffer;
import lazuli_lib.lazuli.core.rendering.RenderingHelper;
import lazuli_lib.lazuli.acess.LazuliWorldRenderQueueManager;
import lazuli_lib.lazuli.acess.data_containers.Triangle;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(
            method = "renderWorldBorder",
            at = @At("HEAD")
    )
    private void inject_renderWorldBorder(Camera camera, CallbackInfo ci) {


        // Obtain the VertexConsumerProvider (usually passed in rendering functions)
        VertexConsumerProvider.Immediate vertexConsumers = VertexConsumerProvider.immediate(new BufferBuilder(2048));

        // Get the VertexConsumer for a specific layer (e.g., particles, world border)
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getSolid());




        // Flush any queued render buffers before rendering
        LazuliWorldRenderQueueManager.flushBuffers();

        // Texture UV coordinates
        float minU = 0;
        float maxU = 1;
        float minV = 0;
        float maxV = 1;

        // Full brightness
        int light = 15728880;

// Iterate through the queued triangle buffers and render each buffer
        LazuliWorldRenderQueueManager.flushBuffers();

        for (TriangleBuffer triangleBuffer : LazuliWorldRenderQueueManager.getTriangleQueue()) {
            for (Triangle tri : triangleBuffer.getTriangles()) {
                int[] color = tri.getColorAsArray();

                System.out.println("triying to render");

                // Render the triangle using the helper
                RenderingHelper.renderQuadDoubleSided(
                        vertexConsumer,
                        camera,
                        tri.getVertex1(),
                        tri.getVertex2(),
                        tri.getVertex3(),
                        tri.getVertex3(), // Repeated last vertex (since it's a quad function)
                        minU, minV, maxU, maxV, color, light
                );
            }
        }

// Iterate through the queued line buffers and render each buffer
        for (LazuliLineBuffer lineBuffer : LazuliWorldRenderQueueManager.getLineQueue()) {
            for (LazuliLine line : lineBuffer.getLines()) {
                int[] color = line.getColorAsArray();

                System.out.println("triying to render");
                // Render the line using the helper
//                RenderingHelper.renderPlayerFacingQuad(
//                        vertexConsumer,
//                        line.getVertex1(),
//                        line.getVertex2(),
//                        line.getWidth(), line.getZ(),
//                        minU, minV, maxU, maxV, color, light, camera
//                );
            }
        }
        vertexConsumers.draw();
    }
}
