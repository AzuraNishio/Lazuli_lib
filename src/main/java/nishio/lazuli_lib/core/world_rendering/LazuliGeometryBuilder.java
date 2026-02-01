package nishio.lazuli_lib.core.world_rendering;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.miscellaneous.LazuliMathUtils;
import org.joml.Vector3f;

/* Lazuli */


import static java.lang.Math.*;

/**
 * Geometry helpers that emit vertices through {@link LazuliBufferBuilder}.
 * Legacy Matrix4f / vanilla BufferBuilder usage removed – 2025-06-24.
 */
public class LazuliGeometryBuilder {

    /* --------------------------------------------------------------------- */
    /*  Global render-space manipulation                                     */
    /* --------------------------------------------------------------------- */
    private static Vec3d  mainDisplacement      = Vec3d.ZERO;
    private static double pitch                 = 0;
    private static double yaw                   = 0;
    private static double roll                  = 0;
    private static boolean doLongRangeClamping  = true;

    /* --------------------------------------------------------------------- */
    /*  PUBLIC BUILDERS                                                      */
    /* --------------------------------------------------------------------- */

    /** Textured sphere (world-relative normals). Signature kept, matrix removed. */
    public static void buildTexturedSphere(
            int     res,            float  radius,
            Vec3d   center,         Vec3d axle,
            float   roll,           boolean flipNormals,
            LazuliBufferBuilder bufferBuilder
    ) {
        float angle2 = 0f;
        Vec3d xAxle = (axle.x == 0 && axle.z == 0) ? new Vec3d(1, 0, 0)
                : new Vec3d(axle.x, 0, axle.z).normalize().rotateY(90);
        Vec3d zAxle = LazuliMathUtils.rotateAroundAxis(xAxle, axle, 90);

        for (int p = 0; p < res; p++) {
            float nextAngle2    = angle2 + (float) (PI / res);
            float thisRingRad   = (float) sin(angle2)   * radius;
            float nextRingRad   = (float) sin(nextAngle2) * radius;
            double thisRingY    = cos(angle2)   * radius;
            double nextRingY    = cos(nextAngle2) * radius;
            angle2 = nextAngle2;

            float angle = roll;
            for (int i = 0; i < res * 2; i++) {
                Vec3d v1 = new Vec3d( sin(angle) * thisRingRad, thisRingY, cos(angle) * thisRingRad );
                Vec3d v2 = new Vec3d( sin(angle) * nextRingRad, nextRingY, cos(angle) * nextRingRad );
                angle += (float) (PI / res);
                Vec3d v3 = new Vec3d( sin(angle) * nextRingRad, nextRingY, cos(angle) * nextRingRad );
                Vec3d v4 = new Vec3d( sin(angle) * thisRingRad, thisRingY, cos(angle) * thisRingRad );

                // sphere-local → world
                v1 = axle.multiply(v1.y).add(xAxle.multiply(v1.x)).add(zAxle.multiply(v1.z));
                v2 = axle.multiply(v2.y).add(xAxle.multiply(v2.x)).add(zAxle.multiply(v2.z));
                v3 = axle.multiply(v3.y).add(xAxle.multiply(v3.x)).add(zAxle.multiply(v3.z));
                v4 = axle.multiply(v4.y).add(xAxle.multiply(v4.x)).add(zAxle.multiply(v4.z));

                // UVs
                double U1 = (angle - roll          ) / (2 * PI);
                double U2 = (angle - roll + PI / res) / (2 * PI);
                double V1 = nextAngle2 / PI;
                double V2 = (nextAngle2 - PI / res) / PI;

                // Emit quad
                if (flipNormals) {
                    addVertexTextureNormal(v4.add(center), U2, V2, v4, bufferBuilder);
                    addVertexTextureNormal(v3.add(center), U2, V1, v3, bufferBuilder);
                    addVertexTextureNormal(v2.add(center), U1, V1, v2, bufferBuilder);
                    addVertexTextureNormal(v1.add(center), U1, V2, v1, bufferBuilder);
                } else {
                    addVertexTextureNormal(v1.add(center), U1, V2, v1, bufferBuilder);
                    addVertexTextureNormal(v2.add(center), U1, V1, v2, bufferBuilder);
                    addVertexTextureNormal(v3.add(center), U2, V1, v3, bufferBuilder);
                    addVertexTextureNormal(v4.add(center), U2, V2, v4, bufferBuilder);
                }
            }
        }
    }

    /** Textured sphere with axle-relative normal roll (Lazuli-native since you wrote it). */
    public static void buildTexturedSphereRotatedNormal(
            int res, float radius, Vec3d center, Vec3d axle,
            float roll, boolean flipNormals, float normalsRoll,
            LazuliBufferBuilder bufferBuilder
    ) {
        Transform3D originalSpace = bufferBuilder.getRenderSpace().copy();
        Transform3D sphereSpace   = Transform3D.fromPosition(center).rotateAround(axle, roll);
        Transform3D normalRot     = Transform3D.ZERO.rotateAround(axle, normalsRoll);

        bufferBuilder.setRenderSpace(sphereSpace);

        float angle2 = 0f;
        for (int p = 0; p < res; p++) {
            float nextAngle2   = angle2 + (float) (PI / res);
            float thisRingRad  = (float) sin(angle2)   * radius;
            float nextRingRad  = (float) sin(nextAngle2) * radius;
            double thisRingY   = cos(angle2)   * radius;
            double nextRingY   = cos(nextAngle2) * radius;
            angle2 = nextAngle2;

            float angle = 0f;
            for (int i = 0; i < res * 2; i++) {
                Vec3d v1 = new Vec3d( sin(angle) * thisRingRad, thisRingY, cos(angle) * thisRingRad );
                Vec3d v2 = new Vec3d( sin(angle) * nextRingRad, nextRingY, cos(angle) * nextRingRad );
                angle += (float) (PI / res);
                Vec3d v3 = new Vec3d( sin(angle) * nextRingRad, nextRingY, cos(angle) * nextRingRad );
                Vec3d v4 = new Vec3d( sin(angle) * thisRingRad, thisRingY, cos(angle) * thisRingRad );

                double U1 = (angle - PI / res) / (2 * PI);
                double U2 = angle / (2 * PI);
                double V1 = nextAngle2 / PI;
                double V2 = (nextAngle2 - PI / res) / PI;

                Vec3d n1 = normalRot.transformPoint(v1.normalize());
                Vec3d n2 = normalRot.transformPoint(v2.normalize());
                Vec3d n3 = normalRot.transformPoint(v3.normalize());
                Vec3d n4 = normalRot.transformPoint(v4.normalize());

                LazuliVertex[] quad = {
                        new LazuliVertex().pos(v1).uv((float)U1, (float)V2).normal(n1),
                        new LazuliVertex().pos(v2).uv((float)U1, (float)V1).normal(n2),
                        new LazuliVertex().pos(v3).uv((float)U2, (float)V1).normal(n3),
                        new LazuliVertex().pos(v4).uv((float)U2, (float)V2).normal(n4)
                };
                if (flipNormals) {
                    bufferBuilder.addVertex(quad[3]); bufferBuilder.addVertex(quad[2]);
                    bufferBuilder.addVertex(quad[1]); bufferBuilder.addVertex(quad[0]);
                } else {
                    bufferBuilder.addVertex(quad[0]); bufferBuilder.addVertex(quad[1]);
                    bufferBuilder.addVertex(quad[2]); bufferBuilder.addVertex(quad[3]);
                }
            }
        }
        bufferBuilder.setRenderSpace(originalSpace);
    }

    /** Flat ring (disc) around {@code center} in {@code axle} plane. */
    public static void buildRing(
            float innerRadius, float outerRadius, int segments,
            Vec3d center,      Camera camera,
            LazuliBufferBuilder bufferBuilder
    ) {
        Vec3d displacement = camera.getPos().subtract(center);

        double twoPi = PI * 2;
        for (int i = 0; i < segments; i++) {
            double a1 = twoPi *  i      / segments;
            double a2 = twoPi * (i + 1) / segments;

            Vec3d inner1 = new Vec3d(cos(a1)*innerRadius, 0, sin(a1)*innerRadius);
            Vec3d inner2 = new Vec3d(cos(a2)*innerRadius, 0, sin(a2)*innerRadius);
            Vec3d outer1 = new Vec3d(cos(a1)*outerRadius, 0, sin(a1)*outerRadius);
            Vec3d outer2 = new Vec3d(cos(a2)*outerRadius, 0, sin(a2)*outerRadius);

            Vec3d v1 = inner1.subtract(displacement);
            Vec3d v2 = outer1.subtract(displacement);
            Vec3d v3 = outer2.subtract(displacement);
            Vec3d v4 = inner2.subtract(displacement);

            float u1 = (float) i      / segments;
            float u2 = (float) (i+1)  / segments;
            Vec3d n  = new Vec3d(0, 1, 0);

            addVertexTextureNormal(v1, u1, 0.0, n, bufferBuilder);
            addVertexTextureNormal(v2, u1, 1.0, n, bufferBuilder);
            addVertexTextureNormal(v3, u2, 1.0, n, bufferBuilder);
            addVertexTextureNormal(v4, u2, 0.0, n, bufferBuilder);
        }
    }

    /** Camera-facing billboard (unit quad). */
    public static void buildSpriteBillboard(
            Vec3d center, Camera camera,
            LazuliBufferBuilder bufferBuilder
    ) {
        Vec3d displacement = camera.getPos().subtract(center);
        Vec3d n = displacement.normalize();   // normal facing camera

        addVertexTextureNormal(Vec3d.ZERO.subtract(displacement), 1, 1, n, bufferBuilder);
        addVertexTextureNormal(Vec3d.ZERO.subtract(displacement), 0, 1, n, bufferBuilder);
        addVertexTextureNormal(Vec3d.ZERO.subtract(displacement), 0, 0, n, bufferBuilder);
        addVertexTextureNormal(Vec3d.ZERO.subtract(displacement), 1, 0, n, bufferBuilder);
    }

    /* --------------------------------------------------------------------- */
    /*  INTERNAL VERTEX HELPER                                               */
    /* --------------------------------------------------------------------- */

    private static void addVertexTextureNormal(
            Vec3d pos, double u, double v, Vec3d normal,
            LazuliBufferBuilder bufferBuilder
    ) {
        normal = normal.normalize();
        Vec3d pos2    = pos.add(mainDisplacement)
                .rotateZ((float)yaw).rotateX((float)roll).rotateY((float)pitch);
        Vec3d normal2 = normal
                .rotateZ((float)yaw).rotateX((float)roll).rotateY((float)pitch);

        float clamp = 600f;
        if (pos2.length() > clamp && doLongRangeClamping) {
            pos2 = pos2.normalize().multiply(clamp + 0.01 * (pos2.length() - clamp));
        }

        bufferBuilder.addVertex(
                new LazuliVertex()
                        .pos(pos2)
                        .uv((float)u, (float)v)
                        .normal(normal2)
        );
    }

    /* --------------------------------------------------------------------- */
    /*  RENDER-SPACE HELPERS (unchanged interface)                           */
    /* --------------------------------------------------------------------- */

    public static void displaceRenderingSpacePos(Vec3d d)                    { mainDisplacement = mainDisplacement.add(d); }
    public static void setRenderingSpacePos(Vec3d d)                         { mainDisplacement = d; }
    public static void rotatedSpaceDisplaceRenderingSpacePos(Vec3d d)        { mainDisplacement = mainDisplacement
            .add(d.rotateZ((float)-yaw)
                    .rotateX((float)-roll)
                    .rotateY((float)-pitch)); }
    public static void displaceRenderingSpaceDir(double dp, double dy, double dr){ pitch += dp; yaw += dy; roll += dr; }
    public static void setRenderingSpaceDir(double p, double y, double r)        { pitch = p;  yaw = y;  roll = r; }

    public static void enableLongRangeClamping()         { doLongRangeClamping = true;  }
    public static void disableLongRangeClamping()        { doLongRangeClamping = false; }
    public static void setLongRangeClamping(boolean b)   { doLongRangeClamping = b;     }

    /* --------------------------------------------------------------------- */
    /*  VISIBILITY HELPERS (direct copy)                                     */
    /* --------------------------------------------------------------------- */

    public static boolean checkIfSphereIsVisible(Vec3d pos, double radius, Camera camera) {
        Vec3d camPos = camera.getPos();
        Vec3d toObj  = pos.subtract(camPos);
        double dist2 = toObj.lengthSquared();
        if (dist2 < 1e-4) return true;

        Vector3f camDir = new Vector3f(0,0,-1); camDir.rotate(camera.getRotation());
        double dot = camDir.x * toObj.x + camDir.y * toObj.y + camDir.z * toObj.z;
        if (dot <= 0) return false;

        double visThres = 0.5 - radius / Math.sqrt(dist2);
        return (dot / Math.sqrt(dist2)) > visThres;
    }

    public static boolean checkIfVisible(Vec3d pos, Camera camera) {
        Vec3d camPos = camera.getPos();
        Vec3d toObj  = pos.subtract(camPos);
        double dist2 = toObj.lengthSquared();
        if (dist2 < 1e-4) return true;

        Vector3f camDir = new Vector3f(0,0,-1); camDir.rotate(camera.getRotation());
        double dot = camDir.x * toObj.x + camDir.y * toObj.y + camDir.z * toObj.z;
        if (dot <= 0) return false;

        double visThres = 0.5 / Math.sqrt(dist2);
        return (dot / Math.sqrt(dist2)) > visThres;
    }

    /**
     * Oriented box.
     *
     * <pre>
     * ─ sizeX, sizeY, sizeZ … full edge lengths
     * ─ center              … world-space centre of the box
     * ─ axle                … world vector the box’s <b>local +Y axis</b> will align to
     * ─ roll                … extra rotation <i>around</i> that axle (radians)
     * ─ flipNormals         … emit inward-facing normals if true
     * </pre>
     */
    public static void buildBox(
            float sizeX, float sizeY, float sizeZ,
            Vec3d center, Vec3d axle, float roll,
            boolean flipNormals,
            LazuliBufferBuilder bb
    ) {
        /* -------------------------------------------------- setup local space */
        Transform3D old = bb.getRenderSpace().copy();
        bb.setRenderSpace(Transform3D.fromPosition(center)           // translate to centre
                .rotateAround(axle, roll));    // spin about axle

        float hx = sizeX * 0.5f,  // half-extents
                hy = sizeY * 0.5f,
                hz = sizeZ * 0.5f;

        /* -------------------------------------------------- orthonormal basis */
        Vec3d yAxle = axle.normalize();
        Vec3d xAxle = (Math.abs(yAxle.x) < 1e-4 && Math.abs(yAxle.z) < 1e-4)
                ? new Vec3d(1, 0, 0)                                            // pole-case fallback
                : new Vec3d(yAxle.x, 0, yAxle.z).normalize().rotateY(90);
        Vec3d zAxle = LazuliMathUtils.rotateAroundAxis(xAxle, yAxle, 90);

        /* -------------------------------------------------- eight corners */
        Vec3d[] c = new Vec3d[8];
        for (int xi = 0; xi < 2; xi++)
            for (int yi = 0; yi < 2; yi++)
                for (int zi = 0; zi < 2; zi++) {
                    int idx = (xi<<2)|(yi<<1)|zi;
                    c[idx] =  xAxle.multiply( (xi==1 ?  hx : -hx) )
                            .add(yAxle.multiply( (yi==1 ?  hy : -hy) ))
                            .add(zAxle.multiply( (zi==1 ?  hz : -hz) ));
                }

        /* order: +X, −X, +Y, −Y, +Z, −Z  (each as 4-corner quad) */
        int[][] faces = {
                {4,5,7,6}, {1,0,2,3},
                {2,6,7,3}, {0,1,5,4},
                {1,4,6,2}, {5,0,3,7}
        };
        Vec3d[] norms = {
                xAxle, xAxle.multiply(-1),
                yAxle, yAxle.multiply(-1),
                zAxle, zAxle.multiply(-1)
        };

        /* -------------------------------------------------- emit vertices */
        for (int f = 0; f < 6; f++) {
            Vec3d n = norms[f];
            int[] v = faces[f];
            LazuliVertex[] quad = {
                    new LazuliVertex().pos(c[v[0]]).uv(0,0).normal(n),
                    new LazuliVertex().pos(c[v[1]]).uv(1,0).normal(n),
                    new LazuliVertex().pos(c[v[2]]).uv(1,1).normal(n),
                    new LazuliVertex().pos(c[v[3]]).uv(0,1).normal(n)
            };
            if (flipNormals) {
                bb.addVertex(quad[3]); bb.addVertex(quad[2]); bb.addVertex(quad[1]); bb.addVertex(quad[0]);
            } else {
                bb.addVertex(quad[0]); bb.addVertex(quad[1]); bb.addVertex(quad[2]); bb.addVertex(quad[3]);
            }
        }

        bb.setRenderSpace(old);
    }


    /**
     * Cylinder (open or capped) whose axis is {@code axle}.
     *
     * @param segments  angular resolution (≥ 3)
     * @param height    full height along the axle
     * @param radius    cylinder radius
     * @param capTop    whether to close the +axle face
     * @param capBottom whether to close the -axle face
     */
    public static void buildCylinder(
            int segments, float radius, float height,
            Vec3d center, Vec3d axle, float roll,
            boolean capTop, boolean capBottom,
            boolean flipNormals,
            LazuliBufferBuilder bb
    ) {
        Transform3D old = bb.getRenderSpace().copy();
        Transform3D cyl = Transform3D.fromPosition(center).rotateAround(axle, roll);
        bb.setRenderSpace(cyl);

        Vec3d yAxle = axle.normalize();
        Vec3d xAxle = (abs(yAxle.x) < 1e-4 && abs(yAxle.z) < 1e-4)
                ? new Vec3d(1,0,0)
                : new Vec3d(yAxle.x,0,yAxle.z).normalize().rotateY(90);
        Vec3d zAxle = LazuliMathUtils.rotateAroundAxis(xAxle,yAxle,90);

        float half = height*0.5f;
        double twoPi = PI*2;
        for (int i=0;i<segments;i++){
            double a1 = twoPi*i/segments;
            double a2 = twoPi*(i+1)/segments;

            Vec3d rim1 = xAxle.multiply(cos(a1)*radius)
                    .add(zAxle.multiply(sin(a1)*radius));
            Vec3d rim2 = xAxle.multiply(cos(a2)*radius)
                    .add(zAxle.multiply(sin(a2)*radius));

            Vec3d v1 = rim1.add(yAxle.multiply(-half));
            Vec3d v2 = rim2.add(yAxle.multiply(-half));
            Vec3d v3 = rim2.add(yAxle.multiply( half));
            Vec3d v4 = rim1.add(yAxle.multiply( half));

            // Quad side
            if (flipNormals){
                addVertexTextureNormal(v4, (double)(i+1)/segments,1, rim1.normalize(), bb);
                addVertexTextureNormal(v3, (double)(i+1)/segments,0, rim2.normalize(), bb);
                addVertexTextureNormal(v2, (double)i/segments,     0, rim2.normalize(), bb);
                addVertexTextureNormal(v1, (double)i/segments,     1, rim1.normalize(), bb);
            }else{
                addVertexTextureNormal(v1, (double)i/segments,     1, rim1.normalize(), bb);
                addVertexTextureNormal(v2, (double)i/segments,     0, rim2.normalize(), bb);
                addVertexTextureNormal(v3, (double)(i+1)/segments,0, rim2.normalize(), bb);
                addVertexTextureNormal(v4, (double)(i+1)/segments,1, rim1.normalize(), bb);
            }

            /* caps ------------------------------------------------------------ */
            if (capTop){
                Vec3d topCenter = yAxle.multiply(half);
                Vec3d n = yAxle;
                if (flipNormals) n = n.multiply(-1);
                addVertexTextureNormal(topCenter,0.5,0.5,n,bb);
                addVertexTextureNormal(rim2.add(yAxle.multiply(half)), cos(a2)*0.5+0.5, sin(a2)*0.5+0.5, n, bb);
                addVertexTextureNormal(rim1.add(yAxle.multiply(half)), cos(a1)*0.5+0.5, sin(a1)*0.5+0.5, n, bb);
            }
            if (capBottom){
                Vec3d botCenter = yAxle.multiply(-half);
                Vec3d n = yAxle.multiply(-1);
                if (flipNormals) n = n.multiply(-1);
                addVertexTextureNormal(botCenter,0.5,0.5,n,bb);
                addVertexTextureNormal(rim1.add(yAxle.multiply(-half)), cos(a1)*0.5+0.5, sin(a1)*0.5+0.5, n, bb);
                addVertexTextureNormal(rim2.add(yAxle.multiply(-half)), cos(a2)*0.5+0.5, sin(a2)*0.5+0.5, n, bb);
            }
        }
        bb.setRenderSpace(old);
    }

    /**
     * Cone (tip → base).
     *
     * @param height height along {@code axle}
     * @param radius base radius
     */
    public static void buildCone(
            int segments, float radius, float height,
            Vec3d tip, Vec3d axle,
            boolean flipNormals,
            LazuliBufferBuilder bb
    ) {
        Transform3D old = bb.getRenderSpace().copy();
        Transform3D space = Transform3D.fromPosition(tip).rotateAround(axle,0);
        bb.setRenderSpace(space);

        Vec3d yAxle = axle.normalize();
        Vec3d xAxle = (abs(yAxle.x)<1e-4 && abs(yAxle.z)<1e-4)
                ? new Vec3d(1,0,0)
                : new Vec3d(yAxle.x,0,yAxle.z).normalize().rotateY(90);
        Vec3d zAxle = LazuliMathUtils.rotateAroundAxis(xAxle,yAxle,90);

        Vec3d baseCenter = yAxle.multiply(height);
        double twoPi = PI*2;
        for (int i=0;i<segments;i++){
            double a1 = twoPi*i/segments;
            double a2 = twoPi*(i+1)/segments;

            Vec3d rim1 = baseCenter.add(xAxle.multiply(cos(a1)*radius))
                    .add(zAxle.multiply(sin(a1)*radius));
            Vec3d rim2 = baseCenter.add(xAxle.multiply(cos(a2)*radius))
                    .add(zAxle.multiply(sin(a2)*radius));

            Vec3d n1 = rim1.subtract(Vec3d.ZERO).crossProduct(rim2.subtract(Vec3d.ZERO)).normalize();
            if (flipNormals) n1 = n1.multiply(-1);

            // Side triangle (tip, rim1, rim2)
            if (flipNormals){
                addVertexTextureNormal(rim2, 1,0, n1, bb);
                addVertexTextureNormal(rim1, 0,0, n1, bb);
                addVertexTextureNormal(Vec3d.ZERO, 0.5,1, n1, bb);
            }else{
                addVertexTextureNormal(Vec3d.ZERO, 0.5,1, n1, bb);
                addVertexTextureNormal(rim1, 0,0, n1, bb);
                addVertexTextureNormal(rim2, 1,0, n1, bb);
            }

            // Base fan
            Vec3d nBase = yAxle;
            if (flipNormals) nBase = nBase.multiply(-1);
            addVertexTextureNormal(baseCenter,0.5,0.5,nBase,bb);
            addVertexTextureNormal(rim1, cos(a1)*0.5+0.5, sin(a1)*0.5+0.5, nBase, bb);
            addVertexTextureNormal(rim2, cos(a2)*0.5+0.5, sin(a2)*0.5+0.5, nBase, bb);
        }
        bb.setRenderSpace(old);
    }

    /**
     * Torus (doughnut) centred at {@code center} with main radius R and tube radius r.
     */
    public static void buildTorus(
            int resMajor, int resTube,
            float majorR, float tubeR,
            Vec3d center, Vec3d axle, float roll,
            boolean flipNormals,
            LazuliBufferBuilder bb
    ) {
        Transform3D old = bb.getRenderSpace().copy();
        Transform3D space = Transform3D.fromPosition(center).rotateAround(axle, roll);
        bb.setRenderSpace(space);

        Vec3d yAxle = axle.normalize();
        Vec3d xAxle = (abs(yAxle.x)<1e-4 && abs(yAxle.z)<1e-4)
                ? new Vec3d(1,0,0)
                : new Vec3d(yAxle.x,0,yAxle.z).normalize().rotateY(90);
        Vec3d zAxle = LazuliMathUtils.rotateAroundAxis(xAxle,yAxle,90);

        double twoPi = PI*2;
        for (int m=0;m<resMajor;m++){
            double a0 = twoPi*m/resMajor;
            double a1 = twoPi*(m+1)/resMajor;
            Vec3d c0 = xAxle.multiply(cos(a0)*majorR).add(zAxle.multiply(sin(a0)*majorR));
            Vec3d c1 = xAxle.multiply(cos(a1)*majorR).add(zAxle.multiply(sin(a1)*majorR));
            for (int t=0;t<resTube;t++){
                double b0 = twoPi*t/resTube;
                double b1 = twoPi*(t+1)/resTube;

                Vec3d n00 = xAxle.multiply(cos(a0)*cos(b0))
                        .add(zAxle.multiply(sin(a0)*cos(b0)))
                        .add(yAxle.multiply(sin(b0)));
                Vec3d n01 = xAxle.multiply(cos(a0)*cos(b1))
                        .add(zAxle.multiply(sin(a0)*cos(b1)))
                        .add(yAxle.multiply(sin(b1)));
                Vec3d n10 = xAxle.multiply(cos(a1)*cos(b0))
                        .add(zAxle.multiply(sin(a1)*cos(b0)))
                        .add(yAxle.multiply(sin(b0)));
                Vec3d n11 = xAxle.multiply(cos(a1)*cos(b1))
                        .add(zAxle.multiply(sin(a1)*cos(b1)))
                        .add(yAxle.multiply(sin(b1)));

                Vec3d v00 = c0.add(n00.multiply(tubeR));
                Vec3d v01 = c0.add(n01.multiply(tubeR));
                Vec3d v10 = c1.add(n10.multiply(tubeR));
                Vec3d v11 = c1.add(n11.multiply(tubeR));

                double U0 = (double)m/resMajor;
                double U1 = (double)(m+1)/resMajor;
                double V0 = (double)t/resTube;
                double V1 = (double)(t+1)/resTube;

                if (flipNormals){
                    addVertexTextureNormal(v11, U1, V1, n11, bb);
                    addVertexTextureNormal(v10, U1, V0, n10, bb);
                    addVertexTextureNormal(v00, U0, V0, n00, bb);
                    addVertexTextureNormal(v01, U0, V1, n01, bb);
                }else{
                    addVertexTextureNormal(v00, U0, V0, n00, bb);
                    addVertexTextureNormal(v10, U1, V0, n10, bb);
                    addVertexTextureNormal(v11, U1, V1, n11, bb);
                    addVertexTextureNormal(v01, U0, V1, n01, bb);
                }
            }
        }
        bb.setRenderSpace(old);
    }

    /**
     * Capsule = cylinder + two hemispheres.
     */
    public static void buildCapsule(
            int res, float radius, float height,
            Vec3d center, Vec3d axle, float roll,
            boolean flipNormals,
            Camera camera,                 // for the hemi optimisation
            LazuliBufferBuilder bb
    ) {
        // body (plain cylinder)
        buildCylinder(res, radius, height-2*radius, center, axle, roll,
                false,false, flipNormals, bb);

        // hemispheres
        Vec3d topCenter    = center.add(axle.normalize().multiply((height-radius*2)*0.5f + radius));
        Vec3d bottomCenter = center.add(axle.normalize().multiply(-(height-radius*2)*0.5f - radius));

        // Use half-spheres facing outwards: just call existing textured sphere
        buildTexturedSphere(res, radius, topCenter, axle, roll, flipNormals, bb);
        buildTexturedSphere(res, radius, bottomCenter, axle.multiply(-1), roll, flipNormals, bb);
    }

}
