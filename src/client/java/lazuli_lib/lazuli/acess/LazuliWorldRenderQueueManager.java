package lazuli_lib.lazuli.acess;

import lazuli_lib.lazuli.acess.data_containers.LazuliLineBuffer;
import lazuli_lib.lazuli.acess.data_containers.Triangle;
import lazuli_lib.lazuli.acess.data_containers.TriangleBuffer;
import lazuli_lib.lazuli.acess.data_containers.LazuliLine;


import java.util.*;

public class LazuliWorldRenderQueueManager {

    // Fast-changing queues (reset every frame)
    private static final List<TriangleBuffer> FAST_TRIANGLE_QUEUE = new LinkedList<>();
    private static final List<LazuliLineBuffer> FAST_LINE_QUEUE = new LinkedList<>();

    // Long-lasting queues (persist across frames)
    private static final Map<Integer, TriangleBuffer> LONG_TRIANGLE_QUEUE = new HashMap<>();
    private static final Map<Integer, LazuliLineBuffer> LONG_LINE_QUEUE = new HashMap<>();

    // Internal buffers for individual additions
    private static final TriangleBuffer internalFastTriangleBuffer = new TriangleBuffer();
    private static final LazuliLineBuffer internalFastLineBuffer = new LazuliLineBuffer();

    /**
     * Adds a single triangle to the fast queue.
     */
    public static void addTriangle(Triangle triangle) {
        internalFastTriangleBuffer.addTriangle(triangle.getVertex1(), triangle.getVertex2(), triangle.getVertex3(), triangle.getColor());
    }

    /**
     * Adds a full triangle buffer to the fast queue.
     */
    public static void addTriangleBuffer(TriangleBuffer buffer) {
        if (!buffer.getTriangles().isEmpty()) {
            FAST_TRIANGLE_QUEUE.add(buffer);
        }
    }

    /**
     * Adds a full triangle buffer to the long queue with an ID for later removal.
     */
    public static void addLongTriangleBuffer(int bufferId, TriangleBuffer buffer) {
        if (!buffer.getTriangles().isEmpty()) {
            LONG_TRIANGLE_QUEUE.put(bufferId, buffer);
        }
    }

    /**
     * Removes a triangle buffer from the long queue by ID.
     */
    public static void removeTriangleBuffer(int bufferId) {
        LONG_TRIANGLE_QUEUE.remove(bufferId);
    }

    /**
     * Adds a single line to the fast queue.
     */
    public static void addLine(LazuliLine line) {
        internalFastLineBuffer.addLine(line.getVertex1(), line.getVertex2(), line.getWidth(), line.getZ(), line.getColor());
    }

    /**
     * Adds a full line buffer to the fast queue.
     */
    public static void addLineBuffer(LazuliLineBuffer buffer) {
        if (!buffer.getLines().isEmpty()) {
            FAST_LINE_QUEUE.add(buffer);
        }
    }

    /**
     * Adds a full line buffer to the long queue with an ID for later removal.
     */
    public static void addLongLineBuffer(int bufferId, LazuliLineBuffer buffer) {
        if (!buffer.getLines().isEmpty()) {
            LONG_LINE_QUEUE.put(bufferId, buffer);
        }
    }

    /**
     * Removes a line buffer from the long queue by ID.
     */
    public static void removeLineBuffer(int bufferId) {
        LONG_LINE_QUEUE.remove(bufferId);
    }

    /**
     * Flushes the internal fast buffers into the fast queue for rendering.
     * Call this once per frame before rendering.
     */
    public static void flushBuffers() {
        if (!internalFastTriangleBuffer.getTriangles().isEmpty()) {
            FAST_TRIANGLE_QUEUE.add(internalFastTriangleBuffer);
        }
        if (!internalFastLineBuffer.getLines().isEmpty()) {
            FAST_LINE_QUEUE.add(internalFastLineBuffer);
        }
    }

    /**
     * Retrieves the combined triangle queue (fast + long queue).
     */
    public static List<TriangleBuffer> getTriangleQueue() {
        List<TriangleBuffer> combinedQueue = new ArrayList<>(FAST_TRIANGLE_QUEUE);
        combinedQueue.addAll(LONG_TRIANGLE_QUEUE.values());
        return combinedQueue;
    }

    /**
     * Retrieves the combined line queue (fast + long queue).
     */
    public static List<LazuliLineBuffer> getLineQueue() {
        List<LazuliLineBuffer> combinedQueue = new ArrayList<>(FAST_LINE_QUEUE);
        combinedQueue.addAll(LONG_LINE_QUEUE.values());
        return combinedQueue;
    }

    /**
     * Clears only the fast queue (useful for per-frame resets).
     */
    public static void clearFastQueue() {
        FAST_TRIANGLE_QUEUE.clear();
        FAST_LINE_QUEUE.clear();
        internalFastTriangleBuffer.clear();
        internalFastLineBuffer.clear();
    }

    /**
     * Clears everything (both fast & long queues).
     */
    public static void clearAllQueues() {
        clearFastQueue();
        LONG_TRIANGLE_QUEUE.clear();
        LONG_LINE_QUEUE.clear();
    }
}
