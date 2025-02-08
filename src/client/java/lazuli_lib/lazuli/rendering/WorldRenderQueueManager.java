package lazuli_lib.lazuli.rendering;

import lazuli_lib.lazuli.utill.Triangle;

import java.util.ArrayList;
import java.util.List;

public class WorldRenderQueueManager {

    // Triangle list to hold all the triangles to render
    private static final List<Triangle> triangleQueue = new ArrayList<>();

    // Method to add a triangle to the queue
    public static void addTriangle(Triangle triangle) {
        triangleQueue.add(triangle);
    }

    // Method to get the current queue
    public static List<Triangle> getTriangleQueue() {
        return triangleQueue;
    }

    // Method to clear the queue (useful between frames)
    public static void clearQueue() {
        triangleQueue.clear();
    }
}
