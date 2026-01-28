package nishio.lazuli_lib.core;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;

public class LazuliRenderContext {

    public final Matrix4f viewProjMatrix;
    public final WorldRenderContext context;
    public final float tickDelta;

    public LazuliRenderContext(Matrix4f vp, WorldRenderContext c, float td) {
        this.viewProjMatrix = vp;
        this.context = c;
        this.tickDelta = td;
    }

    public Camera camera() {return context.camera();}

    public Tessellator tessellator() {return Tessellator.getInstance();}

    public LazuliBufferBuilder getLazuliBB(
            VertexFormat.DrawMode drawMode
    ) {
        return new LazuliBufferBuilder(
                tessellator(),
                drawMode,
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                viewProjMatrix,
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
                viewProjMatrix,
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
                viewProjMatrix,
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
                viewProjMatrix,
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
                viewProjMatrix,
                renderSpace
        );
    }

    public LazuliBufferBuilder wrapBufferBuilder(
            BufferBuilder buffer
    ) {
        return new LazuliBufferBuilder(
                buffer,
                viewProjMatrix,
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
                viewProjMatrix,
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
                viewProjMatrix,
                renderSpace
        );
    }
}
