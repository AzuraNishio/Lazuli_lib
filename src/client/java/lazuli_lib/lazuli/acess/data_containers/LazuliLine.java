package lazuli_lib.lazuli.acess.data_containers;

import net.minecraft.util.math.Vec3d;

public class LazuliLine {
    // Vertex positions (now mutable)
    private Vec3d vertex1;
    private Vec3d vertex2;

    private double width; // Width of the line
    private double z; // Z-depth

    // Color in ARGB format (now mutable)
    private int color;

    // Constructor
    public LazuliLine(Vec3d vertex1, Vec3d vertex2, double width, double z, int color) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.width = width;
        this.z = z;
        this.color = color;
    }

    // Getters
    public Vec3d getVertex1() { return vertex1; }
    public Vec3d getVertex2() { return vertex2; }
    public double getWidth() { return width; }
    public double getZ() { return z; }
    public int getColor() { return color; }

    // Setters (Allows transformation!)
    public void setVertex1(Vec3d vertex1) { this.vertex1 = vertex1; }
    public void setVertex2(Vec3d vertex2) { this.vertex2 = vertex2; }
    public void setWidth(double width) { this.width = width; }
    public void setZ(double z) { this.z = z; }
    public void setColor(int color) { this.color = color; }

    // Converts color to ARGB array
    public int[] getColorAsArray() {
        return new int[]{
                (color >> 16) & 0xFF,  // Red
                (color >> 8)  & 0xFF,  // Green
                color & 0xFF,          // Blue
                (color >> 24) & 0xFF   // Alpha
        };
    }
}
