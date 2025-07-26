package nishio.lazuli_lib.core;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import org.joml.Matrix4f;
import nishio.lazuli_lib.obj.Transform3D;

public class LazuliBufferBuilder {

    private final Tessellator tessellator;
    private final BufferBuilder buffer;
    private final VertexFormat.DrawMode drawMode;
    private final VertexFormat vertexFormat;
    private final Matrix4f matrix4f;
    private final Transform3D renderSpace;
    private boolean useCamera;

    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Transform3D renderSpace) {
        this.tessellator = tessellator;
        this.buffer = tessellator.getBuffer();
        this.drawMode = drawMode;
        this.vertexFormat = vertexFormat;
        this.matrix4f = matrix;
        this.renderSpace = renderSpace;
        this.useCamera = false;

        buffer.begin(drawMode, vertexFormat);
    }

    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix) {
        this(tessellator, drawMode, vertexFormat, matrix, Transform3D.OBJECT);
    }

    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix) {
        this(Tessellator.getInstance(), drawMode, vertexFormat, matrix, Transform3D.OBJECT);
    }

    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Transform3D renderSpace) {
        this(Tessellator.getInstance(), drawMode, vertexFormat, matrix, renderSpace);
    }

    public BufferBuilder getBuffer() {
        return buffer;
    }

    public Matrix4f getMatrix4f() {
        return matrix4f;
    }

    public Transform3D getRenderSpace() {
        return renderSpace;
    }

    public boolean usesCamera() {
        return useCamera;
    }

    public void useCamera() {
        this.useCamera = true;
    }

    public void disableCamera() {
        this.useCamera = false;
    }

    public void drawAndReset() {
        buffer.end();
        tessellator.draw();
        buffer.begin(drawMode, vertexFormat); // Reuse for next batch
    }
}
