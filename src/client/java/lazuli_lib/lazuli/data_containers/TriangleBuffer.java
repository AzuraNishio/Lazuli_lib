package lazuli_lib.lazuli.data_containers;

import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;

public class TriangleBuffer {

    // List of triangles (each triangle has 3 vertices and a color)
    private final List<Triangle> triangles;

    // Constructor initializes an empty list
    public TriangleBuffer() {
        this.triangles = new ArrayList<>();
    }

    // Adds a new triangle to the buffer
    public void addTriangle(Vec3d vertex1, Vec3d vertex2, Vec3d vertex3, int color) {
        triangles.add(new Triangle(vertex1, vertex2, vertex3, color));
    }

    public void addTriangle(Triangle Triangle) {
        triangles.add(Triangle);
    }

    // Returns the list of all stored triangles
    public List<Triangle> getTriangles() {
        return triangles;
    }

    // Clears the buffer (removes all triangles)
    public void clear() {
        triangles.clear();
    }
}
