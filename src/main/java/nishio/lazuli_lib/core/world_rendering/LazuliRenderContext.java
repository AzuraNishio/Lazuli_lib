package nishio.lazuli_lib.core.world_rendering;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;

public record LazuliRenderContext(Matrix4f viewProjMatrix, WorldRenderContext context, float tickDelta) {

    public Camera camera() {
        return context.camera();
    }

    public Tessellator tessellator() {
        return Tessellator.getInstance();
    }

    public LazuliBufferBuilder getLazuliBB(
            VertexFormat.DrawMode drawMode
    ) {
        return new LazuliBufferBuilder(
                tessellator(),
                drawMode,
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                Transform3D.ZERO,
                camera()
        );
    }

    public LazuliBufferBuilder getLazuliBB(
            VertexFormat.DrawMode drawMode,
            VertexFormat vertexFormat
    ) {
        return new LazuliBufferBuilder(
                tessellator(),
                drawMode,
                vertexFormat,
                Transform3D.ZERO,
                camera()
        );
    }

    public LazuliBufferBuilder getLazuliBB(
            VertexFormat.DrawMode drawMode,
            VertexFormat vertexFormat,
            Transform3D renderSpace
    ) {
        return new LazuliBufferBuilder(
                tessellator(),
                drawMode,
                vertexFormat,
                renderSpace,
                camera()
        );
    }

    public LazuliBufferBuilder getLazuliBBNoCamera(
            VertexFormat.DrawMode drawMode,
            VertexFormat vertexFormat
    ) {
        return new LazuliBufferBuilder(
                tessellator(),
                drawMode,
                vertexFormat,
                Transform3D.ZERO
        );
    }

    public LazuliBufferBuilder getLazuliBBNoCamera(
            VertexFormat.DrawMode drawMode,
            VertexFormat vertexFormat,
            Transform3D renderSpace
    ) {
        return new LazuliBufferBuilder(
                tessellator(),
                drawMode,
                vertexFormat,
                renderSpace
        );
    }

    public LazuliBufferBuilder wrapBufferBuilder(
            BufferBuilder buffer
    ) {
        return new LazuliBufferBuilder(
                buffer,
                Transform3D.ZERO,
                camera()
        );
    }

    public LazuliBufferBuilder wrapBufferBuilder(
            BufferBuilder buffer,
            Transform3D renderSpace
    ) {
        return new LazuliBufferBuilder(
                buffer,
                renderSpace,
                camera()
        );
    }

    public LazuliBufferBuilder wrapBufferBuilderNoCamera(
            BufferBuilder buffer,
            Transform3D renderSpace
    ) {
        return new LazuliBufferBuilder(
                buffer,
                renderSpace
        );
    }
}
