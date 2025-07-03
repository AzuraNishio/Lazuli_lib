package nishio.lazuli_lib.core;
/** High level wrapper around BufferBuilder. */

import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class LazuliBufferBuilder {
    private BufferBuilder buffer;
    private final Matrix4f matrix4f;
    private Transform3D renderSpace;
    private final boolean useCamera;
    private Camera camera = null;
    private final Tessellator tessellator;
    private float lastR = 1.0f, lastG = 1.0f, lastB = 1.0f, lastA = 1.0f;
    private float lastU = 0.0f, lastV = 0.0f;
    private int lastLight = 0;
    private int lastOverlay = 0;
    private float lastNormalX = 0.0f, lastNormalY = 1.0f, lastNormalZ = 0.0f;
    private final float clampDist = 500;
    private boolean isEmpty = true;

    //===========================================[Bunch of overloaded builder methods]===========================================
    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Transform3D renderSpace) {
        this.tessellator = tessellator;
        this.buffer = tessellator.begin(drawMode, vertexFormat);
        this.matrix4f = matrix;
        this.renderSpace = renderSpace;
        this.useCamera = false;

    }
    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix) {
        this.tessellator = tessellator;
        this.buffer = tessellator.begin(drawMode, vertexFormat);
        this.matrix4f = matrix;
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = false;

    }
    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Transform3D renderSpace) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.begin(drawMode, vertexFormat);
        this.matrix4f = matrix;
        this.renderSpace = renderSpace;
        this.useCamera = false;
    }
    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.begin(drawMode, vertexFormat);
        this.matrix4f = matrix;
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = false;
    }
    //Now those with camera!
    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Transform3D renderSpace, Camera camera) {
        this.tessellator = tessellator;
        this.buffer = tessellator.begin(drawMode, vertexFormat);
        this.matrix4f = matrix;
        this.renderSpace = renderSpace;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Camera camera) {
        this.tessellator = tessellator;
        this.buffer = tessellator.begin(drawMode, vertexFormat);
        this.matrix4f = matrix;
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Transform3D renderSpace, Camera camera) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.begin(drawMode, vertexFormat);
        this.matrix4f = matrix;
        this.renderSpace = renderSpace;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Camera camera) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.begin(drawMode, vertexFormat);
        this.matrix4f = matrix;
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(BufferBuilder buffer, Matrix4f matrix, Transform3D renderSpace, Camera camera) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = buffer;
        this.matrix4f = matrix;
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(BufferBuilder buffer, Matrix4f matrix, Transform3D renderSpace) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = buffer;
        this.matrix4f = matrix;
        this.renderSpace = renderSpace;
        this.useCamera = false;
    }
    public LazuliBufferBuilder(BufferBuilder buffer, Matrix4f matrix) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = buffer;
        this.matrix4f = matrix;
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = false;
    }

    //====================================================[Done with them :D]====================================================

    public BufferBuilder getRaw() {
        return buffer;
    }

    public void drawAndReset() {
        if (!isEmpty) {
            BuiltBuffer builtBuffer = buffer.end();
            BufferRenderer.drawWithGlobalProgram(builtBuffer);
            this.isEmpty = true;
            this.buffer = this.tessellator.begin(builtBuffer.getDrawParameters().mode(), builtBuffer.getDrawParameters().format());
        }
    }

    public void draw(){
        if (!isEmpty) {
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            this.isEmpty = true;
        }
    }


    public LazuliBufferBuilder vertex(double x, double y, double z) {
        return vertex(new Vec3d(x, y, z));
    }

    public LazuliBufferBuilder vertex(Vec3d vec) {
        // Apply transform from render space
        Vec3d transformed = renderSpace.transformPoint(vec);
        this.isEmpty = false;

        // Subtract camera position if needed
        if (useCamera && camera != null) {
            transformed = transformed.subtract(camera.getPos());
        }
        if (transformed.length()>clampDist) {
            transformed = transformed.normalize().multiply(clampDist+(0.002*(transformed.length()-clampDist)));
        }

        buffer.vertex(matrix4f, (float) transformed.x, (float) transformed.y, (float) transformed.z);
        return this;
    }


    public LazuliBufferBuilder color(int r, int g, int b, int a) {
        buffer.color(r, g, b, a);
        return this;
    }

    public LazuliBufferBuilder texture(float u, float v) {
        buffer.texture(u, v);
        return this;
    }

    public LazuliBufferBuilder overlay(int overlay) {
        buffer.overlay(overlay);
        return this;
    }

    public LazuliBufferBuilder light(int light) {
        buffer.light(light);
        return this;
    }

    public LazuliBufferBuilder normal(float x, float y, float z) {
        buffer.normal(x, y, z);
        return this;
    }


    public LazuliBufferBuilder addVertex(LazuliVertex v) {
        Vec3d pos = v.getPos();
        if (pos != null) {
            vertex(pos);
        } else {
            vertex(0.0, 0.0, 0.0); // or keep last position if desired
        }

        // Color
        if (v.r != null) lastR = v.r;
        if (v.g != null) lastG = v.g;
        if (v.b != null) lastB = v.b;
        if (v.a != null) lastA = v.a;
        color((int)(lastR * 255f), (int)(lastG * 255f), (int)(lastB * 255f), (int)(lastA * 255f));

        // Texture coordinates
        if (v.u != null) lastU = v.u;
        if (v.v != null) lastV = v.v;
        texture(lastU, lastV);

        // Light
        if (v.light != null) lastLight = v.light;
        light(lastLight);

        // Overlay
        if (v.overlay != null) lastOverlay = v.overlay;
        overlay(lastOverlay);

        // Normal
        if (v.normalX != null) lastNormalX = v.normalX;
        if (v.normalY != null) lastNormalY = v.normalY;
        if (v.normalZ != null) lastNormalZ = v.normalZ;

        Vector3f normal = new Vector3f(lastNormalX, lastNormalY, lastNormalZ);
        normal.rotate(renderSpace.rotation);
        normal(normal.x, normal.y, normal.z);

        return this;
    }



    public void setRenderSpace(Transform3D renderSpace) {
        this.renderSpace = renderSpace;
    }

    public Transform3D getRenderSpace() {
        return this.renderSpace;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
