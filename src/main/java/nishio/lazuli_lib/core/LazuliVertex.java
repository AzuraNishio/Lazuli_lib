package nishio.lazuli_lib.core;
/** Fluent vertex builder used by LazuliBufferBuilder. */

import net.minecraft.util.math.Vec3d;

public class LazuliVertex {
    public Double x, y, z;
    public Float u, v;
    public Float r, g, b, a;
    public Integer light;
    public Integer overlay;
    public Float normalX, normalY, normalZ;

    public LazuliVertex pos(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
        return this;
    }

    public LazuliVertex displacePos(Vec3d dis){
        this.x += dis.x;
        this.y += dis.y;
        this.z += dis.z;

        return this;
    }

    public LazuliVertex displacePos(double x, double y, double z){
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public LazuliVertex pos(Vec3d pos) {
        this.x = pos.x; this.y = pos.y; this.z = pos.z;
        return this;
    }

    public LazuliVertex uv(float u, float v) {
        this.u = u; this.v = v;
        return this;
    }

    public LazuliVertex color(float r, float g, float b, float a) {
        this.r = r; this.g = g; this.b = b; this.a = a;
        return this;
    }

    public LazuliVertex color(int r, int g, int b, int a) {
        return color(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public LazuliVertex light(int light) {
        this.light = light;
        return this;
    }

    public LazuliVertex overlay(int overlay) {
        this.overlay = overlay;
        return this;
    }

    public LazuliVertex normal(float x, float y, float z) {
        this.normalX = x; this.normalY = y; this.normalZ = z;
        return this;
    }

    public LazuliVertex normal(Vec3d normal) {
        this.normalX = (float) normal.x; this.normalY = (float) normal.y; this.normalZ = (float) normal.z;
        return this;
    }

    public LazuliVertex copy() {
        LazuliVertex copy = new LazuliVertex();
        copy.x = x; copy.y = y; copy.z = z;
        copy.u = u; copy.v = v;
        copy.r = r; copy.g = g; copy.b = b; copy.a = a;
        copy.light = light;
        copy.overlay = overlay;
        copy.normalX = normalX; copy.normalY = normalY; copy.normalZ = normalZ;
        return copy;
    }

    public Vec3d getPos() {
        if (x == null || y == null || z == null) return null;
        return new Vec3d(x, y, z);
    }

    // Retorna a normal como Vec3d, se definida, senão null
    public Vec3d getNormal() {
        if (normalX == null || normalY == null || normalZ == null) return null;
        return new Vec3d(normalX, normalY, normalZ);
    }
}
