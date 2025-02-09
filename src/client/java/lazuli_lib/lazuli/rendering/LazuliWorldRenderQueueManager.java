package lazuli_lib.lazuli.rendering;

import lazuli_lib.lazuli.data_containers.LazuliLine;
import lazuli_lib.lazuli.data_containers.Triangle;

import java.util.ArrayList;
import java.util.List;

public class LazuliWorldRenderQueueManager {

    // Triangle list to hold all the triangles to render
    private static final List<Triangle> triangleQueue = new ArrayList<>();

    // Triangle list to hold all the triangles to render
    private static final List<LazuliLine> LAZULI_LINE_QUEUE = new ArrayList<>();

    // Method to add a triangle to the queue
    public static void addTriangle(Triangle triangle) {
        triangleQueue.add(triangle);
    }

    // Method to get the current queue
    public static List<Triangle> getTriangleQueue() {
        return triangleQueue;
    }

    // Method to add a LazuliLine to the queue
    public static void addLine(LazuliLine LazuliLine) {
        LAZULI_LINE_QUEUE.add(LazuliLine);
    }

    public static List<LazuliLine> getLineQueue() {
        return LAZULI_LINE_QUEUE;
    }

    // Method to clear the queue (useful between frames)
    public static void clearQueue() {
        triangleQueue.clear();
        LAZULI_LINE_QUEUE.clear();
    }
}
