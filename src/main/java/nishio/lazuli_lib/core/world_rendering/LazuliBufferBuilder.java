package nishio.lazuli_lib.core.world_rendering;
/** High level wrapper around BufferBuilder. */

import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class LazuliBufferBuilder {
    private BufferBuilder buffer;
    private Transform3D renderSpace;
    private final boolean useCamera;
    private Camera camera = null;
    private final Tessellator tessellator;
    private float lastR = 1.0f, lastG = 1.0f, lastB = 1.0f, lastA = 1.0f;
    private float lastU = 0f, lastV = 0f;
    private int lastLight = 0;
    private int lastOverlay = 0;
    private float lastNormalX = 0.0f, lastNormalY = 1.0f, lastNormalZ = 0.0f;
    private final float clampDist = 500;
    private boolean isEmpty = true;
    private boolean uvSet = false;
    private int vertexCount = 0;

    //===========================================[Bunch of overloaded builder methods]===========================================
    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Matrix4f matrix, Transform3D renderSpace) {
        this.tessellator = tessellator;
        this.buffer = tessellator.getBuffer();
        this.buffer.begin(drawMode, vertexFormat);
        this.renderSpace = renderSpace;
        this.useCamera = false;

    }

    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat) {
        this.tessellator = tessellator;
        this.buffer = tessellator.getBuffer();
        this.buffer.begin(drawMode, vertexFormat);
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = false;

    }
    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Transform3D renderSpace) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.getBuffer();
        this.buffer.begin(drawMode, vertexFormat);
        this.renderSpace = renderSpace;
        this.useCamera = false;
    }
    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.getBuffer();
        this.buffer.begin(drawMode, vertexFormat);
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = false;
    }
    //Now those with camera!
    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Transform3D renderSpace, Camera camera) {
        this.tessellator = tessellator;
        this.buffer = tessellator.getBuffer();
        this.buffer.begin(drawMode, vertexFormat);
        this.renderSpace = renderSpace;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Camera camera) {
        this.tessellator = tessellator;
        this.buffer = tessellator.getBuffer();
        this.buffer.begin(drawMode, vertexFormat);
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Transform3D renderSpace, Camera camera) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.getBuffer();
        this.buffer.begin(drawMode, vertexFormat);
        this.renderSpace = renderSpace;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Camera camera) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = this.tessellator.getBuffer();
        this.buffer.begin(drawMode, vertexFormat);
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(BufferBuilder buffer, Transform3D renderSpace, Camera camera) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = buffer;
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = true;
        this.camera = camera;
    }
    public LazuliBufferBuilder(BufferBuilder buffer, Transform3D renderSpace) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = buffer;
        this.renderSpace = renderSpace;
        this.useCamera = false;
    }
    public LazuliBufferBuilder(BufferBuilder buffer) {
        this.tessellator = Tessellator.getInstance();
        this.buffer = buffer;
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = false;
    }

    public LazuliBufferBuilder(Tessellator tessellator, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, Transform3D zero) {
        this.tessellator = Tessellator.getInstance();
        this.renderSpace = Transform3D.ZERO;
        this.useCamera = false;
    }

    //====================================================[Done with them :D]====================================================

    public BufferBuilder getRaw() {
        return buffer;
    }

    public void drawAndReset() {
        if (!isEmpty) {
            this.buffer = LapisRenderer.drawAndReset(this.buffer, this.tessellator);
            this.isEmpty = true;
            vertexCount = 0;
        }
    }

    public void draw(){
        if (!isEmpty) {
            BufferBuilder.BuiltBuffer builtBuffer = buffer.end();
            BufferRenderer.drawWithGlobalProgram(builtBuffer);
            this.isEmpty = true;
            vertexCount = 0;
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

        vertexCount++;

        buffer.vertex((float) transformed.x, (float) transformed.y, (float) transformed.z);
        return this;
    }


    public LazuliBufferBuilder color(int r, int g, int b, int a) {
        buffer.color(r, g, b, a);
        lastR = r;
        lastG = g;
        lastB = b;
        lastA = a;
        return this;
    }

    public LazuliBufferBuilder texture(float u, float v) {
        buffer.texture(u, v);
        lastU = u;
        lastV = v;
        return this;
    }

    public LazuliBufferBuilder overlay(int overlay) {
        buffer.overlay(overlay);
        lastOverlay = overlay;
        return this;
    }

    public LazuliBufferBuilder light(int light) {
        buffer.light(light);
        lastLight = light;
        return this;
    }

    public LazuliBufferBuilder normal(float x, float y, float z) {
        buffer.normal(x, y, z);
        lastNormalX = x;
        lastNormalY = y;
        lastNormalZ = z;
        return this;
    }

    public LazuliBufferBuilder normal(Vec3d n) {
        buffer.normal((float) n.x, (float) n.y, (float) n.z);
        lastNormalX = (float) n.x;
        lastNormalY = (float) n.y;
        lastNormalZ = (float) n.z;
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
        if (v.u != null) { lastU = v.u; uvSet = true; };
        if (v.v != null) { lastV = v.v; uvSet = true; };

        if (uvSet){
            texture(lastU, lastV);
        } else {
            texture((float) Math.floor((double) (vertexCount % 4) / 2), (float) Math.floor((double) ((vertexCount + 1) % 4) / 2));
        }




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

        this.buffer.next();

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