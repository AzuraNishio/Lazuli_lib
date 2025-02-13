package lazuli_lib.lazuli.acess.data_containers;

import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;

public class TriangleBuffer {
    private final List<Triangle> triangles;

    public TriangleBuffer() {
        this.triangles = new ArrayList<>();
    }

    public void addTriangle(Vec3d vertex1, Vec3d vertex2, Vec3d vertex3, int color) {
        triangles.add(new Triangle(vertex1, vertex2, vertex3, color));
    }

    public void addTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void clear() {
        triangles.clear();
    }

    // ✅ Displace (Move) all triangles by a vector
    public void displace(Vec3d displacement) {
        for (Triangle triangle : triangles) {
            triangle.setVertex1(triangle.getVertex1().add(displacement));
            triangle.setVertex2(triangle.getVertex2().add(displacement));
            triangle.setVertex3(triangle.getVertex3().add(displacement));
        }
    }

    // ✅ Resize around a pivot point
    public void resize(Vec3d scale, Vec3d pivot) {
        for (Triangle triangle : triangles) {
            triangle.setVertex1(scaleVertex(triangle.getVertex1(), scale, pivot));
            triangle.setVertex2(scaleVertex(triangle.getVertex2(), scale, pivot));
            triangle.setVertex3(scaleVertex(triangle.getVertex3(), scale, pivot));
        }
    }

    private Vec3d scaleVertex(Vec3d v, Vec3d scale, Vec3d pivot) {
        Vec3d relative = v.subtract(pivot);
        return new Vec3d(relative.x * scale.x, relative.y * scale.y, relative.z * scale.z).add(pivot);
    }

    public void mergeBuffer(TriangleBuffer InBuffer) {
        triangles.addAll(InBuffer.getTriangles());
    }

    // ✅ Rotate around X-axis at a pivot point
    public void rotateX(double angle, Vec3d pivot) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (Triangle triangle : triangles) {
            triangle.setVertex1(rotateX(triangle.getVertex1(), cos, sin, pivot));
            triangle.setVertex2(rotateX(triangle.getVertex2(), cos, sin, pivot));
            triangle.setVertex3(rotateX(triangle.getVertex3(), cos, sin, pivot));
        }
    }

    private Vec3d rotateX(Vec3d v, double cos, double sin, Vec3d pivot) {
        Vec3d relative = v.subtract(pivot);
        return new Vec3d(
                relative.x,
                relative.y * cos - relative.z * sin,
                relative.y * sin + relative.z * cos
        ).add(pivot);
    }

    // ✅ Rotate around Y-axis at a pivot point
    public void rotateY(double angle, Vec3d pivot) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (Triangle triangle : triangles) {
            triangle.setVertex1(rotateY(triangle.getVertex1(), cos, sin, pivot));
            triangle.setVertex2(rotateY(triangle.getVertex2(), cos, sin, pivot));
            triangle.setVertex3(rotateY(triangle.getVertex3(), cos, sin, pivot));
        }
    }

    private Vec3d rotateY(Vec3d v, double cos, double sin, Vec3d pivot) {
        Vec3d relative = v.subtract(pivot);
        return new Vec3d(
                relative.x * cos + relative.z * sin,
                relative.y,
                -relative.x * sin + relative.z * cos
        ).add(pivot);
    }

    // ✅ Rotate around Z-axis at a pivot point
    public void rotateZ(double angle, Vec3d pivot) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        for (Triangle triangle : triangles) {
            triangle.setVertex1(rotateZ(triangle.getVertex1(), cos, sin, pivot));
            triangle.setVertex2(rotateZ(triangle.getVertex2(), cos, sin, pivot));
            triangle.setVertex3(rotateZ(triangle.getVertex3(), cos, sin, pivot));
        }
    }

    private Vec3d rotateZ(Vec3d v, double cos, double sin, Vec3d pivot) {
        Vec3d relative = v.subtract(pivot);
        return new Vec3d(
                relative.x * cos - relative.y * sin,
                relative.x * sin + relative.y * cos,
                relative.z
        ).add(pivot);
    }

    // ✅ Overwrite all colors
    public void overwriteColor(int newColor) {
        for (Triangle triangle : triangles) {
            triangle.setColor(newColor);
        }
    }

    // ✅ Tint all colors
    public void tintColor(int tint) {
        for (Triangle triangle : triangles) {
            triangle.setColor(triangle.getColor() & tint);
        }
    }
}
