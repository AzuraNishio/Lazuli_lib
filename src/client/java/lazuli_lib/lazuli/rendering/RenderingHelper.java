package lazuli_lib.lazuli.rendering;


import com.mojang.blaze3d.systems.RenderSystem;
import lazuli_lib.lazuli.utill.LazuliMathUtils;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Vec3d;

/**
 * A helper class for rendering quads with a VertexConsumer.
 */
public class RenderingHelper {

    public static void renderQuadDoubleSided(
        VertexConsumer vertexConsumer, Camera camera,
        Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, 
        float minU, float minV, float maxU, float maxV, 
        int[] color, int light
    ) {
        if (color.length != 4) {
            throw new IllegalArgumentException("Color array must have 4 elements (RGBA).");
        }


        Vec3d cameraPos = camera.getPos();
        v1 = v1.subtract(cameraPos);
        v2 = v2.subtract(cameraPos);
        v3 = v3.subtract(cameraPos);
        v4 = v4.subtract(cameraPos);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

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
            VertexConsumer vertexConsumer, Camera camera,
            Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4,
            float minU, float minV, float maxU, float maxV,
            int[] color, int light
    ) {
        if (color.length != 4) {
            throw new IllegalArgumentException("Color array must have 4 elements (RGBA).");
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        Vec3d cameraPos = camera.getPos();
        v1 = v1.subtract(cameraPos);
        v2 = v2.subtract(cameraPos);
        v3 = v3.subtract(cameraPos);
        v4 = v4.subtract(cameraPos);

        // Add vertices in the correct order (counterclockwise)
        vertex(vertexConsumer, v1, minU, minV, color, light);
        vertex(vertexConsumer, v2, minU, maxV, color, light);
        vertex(vertexConsumer, v3, maxU, maxV, color, light);
        vertex(vertexConsumer, v4, maxU, minV, color, light);
    }

    public static void renderPlayerFacingQuad(
            VertexConsumer vertexConsumer,
            Vec3d v1, Vec3d v2, double lineWidth, double Z,
            float minU, float minV, float maxU, float maxV,
            int[] color, int light, Camera camera
    ) {
        if (color.length != 4) {
            throw new IllegalArgumentException("Color array must have 4 elements (RGBA).");
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);


        // Get camera forward direction
        Vec3d cameraDir = getDirectionFromCamera(camera);

        // Get camera right vector (perpendicular to forward and up)
        Vec3d right = cameraDir.crossProduct(new Vec3d(0, 1, 0)).normalize();

        // Up & down are perpendicular to the right vector
        Vec3d up = right.multiply(lineWidth);
        Vec3d down = up.multiply(-1);

        Vec3d lineDir = v1.subtract(v2).normalize();
        Vec3d cameraDifferenceV1 = v1.subtract(camera.getPos());
        Vec3d cameraDifferenceV2 = v2.subtract(camera.getPos());

        Vec3d ScaledNormalV1 = LazuliMathUtils.rotateAroundAxis(cameraDifferenceV1,lineDir,90).normalize().multiply(lineWidth);
        Vec3d ScaledNormalV2 = LazuliMathUtils.rotateAroundAxis(cameraDifferenceV1,lineDir,90).normalize().multiply(lineWidth);

        Vec3d v3 = v2.add(ScaledNormalV2).subtract(cameraDifferenceV2.multiply(Z));
        Vec3d v4 = v1.add(ScaledNormalV1).subtract(cameraDifferenceV1.multiply(Z));

        v1 = v1.subtract(ScaledNormalV1).subtract(cameraDifferenceV1.multiply(Z));
        v2 = v2.subtract(ScaledNormalV2).subtract(cameraDifferenceV2.multiply(Z));

      renderQuadDoubleSided(vertexConsumer,camera, v1,v2,v3,v4,minU,minV,maxU,maxV,color,light);
    }



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


    public static Vec3d getDirectionFromCamera(Camera camera) {
        float yaw = camera.getYaw();
        float pitch = camera.getPitch();

        // Convert degrees to radians for trigonometric functions
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);

        // Calculate direction vector
        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vec3d(x, y, z).normalize(); // Normalize to unit vector
    }
}
