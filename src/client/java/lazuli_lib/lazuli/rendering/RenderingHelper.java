package lazuli_lib.lazuli.rendering;


import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Vec3d;

/**
 * A helper class for rendering quads with a VertexConsumer.
 */
public class RenderingHelper {

    /**
     * Renders a textured quad using four Vec3d points.
     *
     * @param vertexConsumer The VertexConsumer used to render.
     * @param v1 The first vertex.
     * @param v2 The second vertex.
     * @param v3 The third vertex.
     * @param v4 The fourth vertex.
     * @param minU The minimum U texture coordinate.
     * @param minV The minimum V texture coordinate.
     * @param maxU The maximum U texture coordinate.
     * @param maxV The maximum V texture coordinate.
     * @param color The RGBA color as an integer array (e.g., {255, 255, 255, 255}).
     * @param light The lightmap value.
     */
    public static void renderQuadDoubleSided(
        VertexConsumer vertexConsumer,
        Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, 
        float minU, float minV, float maxU, float maxV, 
        int[] color, int light
    ) {
        if (color.length != 4) {
            throw new IllegalArgumentException("Color array must have 4 elements (RGBA).");
        }

        // Add vertices in the correct order (counterclockwise)
        vertex(vertexConsumer, v1, minU, minV, color, light);
        vertex(vertexConsumer, v2, minU, maxV, color, light);
        vertex(vertexConsumer, v3, maxU, maxV, color, light);
        vertex(vertexConsumer, v4, maxU, minV, color, light);

        //render second quad clockwise to make it double sided
        vertex(vertexConsumer, v4, maxU, minV, color, light);
        vertex(vertexConsumer, v3, maxU, maxV, color, light);
        vertex(vertexConsumer, v2, minU, maxV, color, light);
        vertex(vertexConsumer, v1, minU, minV, color, light);


    }

    public static void renderQuad(
            VertexConsumer vertexConsumer,
            Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4,
            float minU, float minV, float maxU, float maxV,
            int[] color, int light
    ) {
        if (color.length != 4) {
            throw new IllegalArgumentException("Color array must have 4 elements (RGBA).");
        }

        // Add vertices in the correct order (counterclockwise)
        vertex(vertexConsumer, v1, minU, minV, color, light);
        vertex(vertexConsumer, v2, minU, maxV, color, light);
        vertex(vertexConsumer, v3, maxU, maxV, color, light);
        vertex(vertexConsumer, v4, maxU, minV, color, light);
    }

    /**
     * Helper method to add a vertex to the buffer.
     */
    private static void vertex(
        VertexConsumer vertexConsumer, 
        Vec3d pos, float u, float v, int[] color, int light
    ) {
        vertexConsumer.vertex((float) pos.x, (float) pos.y, (float) pos.z)
                      .texture(u, v)
                      .color(color[0], color[1], color[2], color[3])
                      .light(light)
                      .next();
    }
}
