package lazuli_lib.lazuli.utill;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.*;

import java.lang.reflect.Field;

/**
 * Utility class for mathematical transformations.
 */
public class LazuliMathUtils {

    /**
     * Converts world space coordinates to screen space using FOV, camera rotation, camera position, and screen dimensions.
     *
     * @param worldPos The world space coordinates to transform.
     * @return A Vec3d containing screen space coordinates (x, y, depth).
     */
    public static Vec3d worldSpaceToScreenSpace(Vec3d worldPos) {

        // =========[ Retrieve Camera Properties ]===========
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null || client.gameRenderer == null) {
            return null;
        }

        Camera camera = client.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();
        Quaternion cameraRotation = camera.getRotation();
        int screenWidth = client.getWindow().getWidth();
        int screenHeight = client.getWindow().getHeight();
        float aspectRatio = (float) screenWidth / screenHeight;
        float fov = (float) Math.toRadians(client.options.getFov().getValue());

        // =========[ Transform World Position to Camera Space ]===========
        Vec3d relativePos = worldPos.subtract(cameraPos);
        Vec3f relativePosVec3f = toVec3f(relativePos);

        Quaternion cameraRotationCopy = new Quaternion(cameraRotation);
        cameraRotationCopy.conjugate();
        relativePosVec3f.rotate(cameraRotationCopy);

        // =========[ Apply Perspective Projection ]===========
        float halfHeight = (float) Math.tan(fov / 2);
        float halfWidth = halfHeight * aspectRatio;

        float x = relativePosVec3f.getX() / -relativePosVec3f.getZ(); // Perspective division
        float y = relativePosVec3f.getY() / -relativePosVec3f.getZ();

        x = (x / halfWidth) * (screenWidth / 2.0f) + (screenWidth / 2.0f); // Map to screen coordinates
        y = (y / halfHeight) * (screenHeight / 2.0f) + (screenHeight / 2.0f);

        // =========[ Return Screen Position ]===========
//        if (relativePosVec3f.getZ() > 0) {
//            return null; // Behind the camera
//        }

        return new Vec3d(x, y, 0);
    }


    /**
     * Checks if a screen space coordinate (x, y, depth) is visible on the screen.
     *
     * @param pos The (x, y, depth) coordinates to check.
     * @return True if the coordinates are within the visible screen range.
     */
    public static boolean screenSpaceCoordinateIsVisible(Vec3d pos) {
        return pos != null && pos.z > -1 && pos.z < 1;
    }


    public static Vec3f toVec3f(Vec3d vector) {return new Vec3f((float) vector.x,(float) vector.y,(float) vector.z);}
    public static Vec3d toVec3d(Vec3f vector) {return new Vec3d(vector.getX(),vector.getY(),vector.getZ());}
}
