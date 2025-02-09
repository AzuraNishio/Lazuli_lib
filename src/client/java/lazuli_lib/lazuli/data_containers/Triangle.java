package lazuli_lib.lazuli.data_containers;

import net.minecraft.util.math.Vec3d;

public class Triangle {

    // Vertex positions
    private final Vec3d vertex1;
    private final Vec3d vertex2;
    private final Vec3d vertex3;

    // Color in ARGB format
    private final int color;

    // Constructor
    public Triangle(Vec3d vertex1, Vec3d vertex2, Vec3d vertex3, int color) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.color = color;
    }

    // Getters
    public Vec3d getVertex1() {
        return vertex1;
    }

    public Vec3d getVertex2() {
        return vertex2;
    }

    public Vec3d getVertex3() {
        return vertex3;
    }

    public int getColor() {
        return color;
    }

    public int[] getColorAsArray() {
        int a = (color >> 24) & 0xFF; // Extract alpha (highest 8 bits)
        int r = (color >> 16) & 0xFF; // Extract red
        int g = (color >> 8)  & 0xFF; // Extract green
        int b = color & 0xFF;         // Extract blue

        return new int[]{r, g, b, a}; // Return in ARGB order
    }


}
