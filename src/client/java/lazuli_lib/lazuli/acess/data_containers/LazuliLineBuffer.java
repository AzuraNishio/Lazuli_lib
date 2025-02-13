package lazuli_lib.lazuli.acess.data_containers;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class LazuliLineBuffer {
    private final List<LazuliLine> lines;

    public LazuliLineBuffer() {
        this.lines = new ArrayList<>();
    }

    public void addLine(Vec3d vertex1, Vec3d vertex2, double width, double z, int color) {
        lines.add(new LazuliLine(vertex1, vertex2, width, z, color));
    }

    public void addLine(LazuliLine line) {
        lines.add(line);
    }

    public List<LazuliLine> getLines() {
        return lines;
    }

    public void clear() {
        lines.clear();
    }

    // ✅ Displace (Move) all lines by a vector
    public void displace(Vec3d displacement) {
        for (LazuliLine line : lines) {
            line.setVertex1(line.getVertex1().add(displacement));
            line.setVertex2(line.getVertex2().add(displacement));
        }
    }

    // ✅ Resize in XYZ
    public void resize(double scaleX, double scaleY, double scaleZ) {
        for (LazuliLine line : lines) {
            line.setVertex1(new Vec3d(line.getVertex1().x * scaleX, line.getVertex1().y * scaleY, line.getVertex1().z * scaleZ));
            line.setVertex2(new Vec3d(line.getVertex2().x * scaleX, line.getVertex2().y * scaleY, line.getVertex2().z * scaleZ));
        }
    }

    // ✅ Resize using a vector
    public void resize(Vec3d scale) {
        resize(scale.x, scale.y, scale.z);
    }

    // ✅ Rotate around X axis
    public void rotateX(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (LazuliLine line : lines) {
            line.setVertex1(rotateX(line.getVertex1(), cos, sin));
            line.setVertex2(rotateX(line.getVertex2(), cos, sin));
        }
    }

    private Vec3d rotateX(Vec3d v, double cos, double sin) {
        return new Vec3d(v.x, v.y * cos - v.z * sin, v.y * sin + v.z * cos);
    }

    // ✅ Rotate around Y axis
    public void rotateY(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (LazuliLine line : lines) {
            line.setVertex1(rotateY(line.getVertex1(), cos, sin));
            line.setVertex2(rotateY(line.getVertex2(), cos, sin));
        }
    }

    private Vec3d rotateY(Vec3d v, double cos, double sin) {
        return new Vec3d(v.x * cos + v.z * sin, v.y, -v.x * sin + v.z * cos);
    }

    // ✅ Rotate around Z axis
    public void rotateZ(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (LazuliLine line : lines) {
            line.setVertex1(rotateZ(line.getVertex1(), cos, sin));
            line.setVertex2(rotateZ(line.getVertex2(), cos, sin));
        }
    }

    private Vec3d rotateZ(Vec3d v, double cos, double sin) {
        return new Vec3d(v.x * cos - v.y * sin, v.x * sin + v.y * cos, v.z);
    }

    // ✅ Overwrite all colors
    public void overwriteColor(int newColor) {
        for (LazuliLine line : lines) {
            line.setColor(newColor);
        }
    }

    // ✅ Tint all colors (Multiply with a tint color)
    public void tintColor(int tint) {
        for (LazuliLine line : lines) {
            line.setColor(line.getColor() & tint);
        }
    }
}
