package lazuli_lib.lazuli.rendering;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Quaternion;

public class LazuliCameraManager {
    static Vec3d CameraDisplacement = new Vec3d(0, 0, 0);
    static Quaternion CameraRotationDisplacement = new Quaternion(0, 0, 0, 0);

    public static void setCameraDisplacement(Vec3d cameraDisplacement) {
        CameraDisplacement = cameraDisplacement;
    }

    public static void setCameraRotationDisplacement(Quaternion cameraRotationDisplacement) {
        CameraRotationDisplacement = cameraRotationDisplacement;
    }

    public static Vec3d getCameraDisplacement() {
        return CameraDisplacement;
    }

    public static Quaternion getCameraRotation() {
        return CameraRotationDisplacement;
    }
}
