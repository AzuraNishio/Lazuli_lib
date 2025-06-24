package nishio.lazuli_lib.core;
/** Manages temporary camera transformations. */

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;


public class LazuliCameraManager {
    static Vec3d CameraDisplacement = new Vec3d(0, 0, 0);
    static Vec3d CameraShakeDisplacement = new Vec3d(0, 0, 0);
    static Quaternionf CameraRotationDisplacement = new Quaternionf(0, 0, 0, 0);
    static Quaternionf CameraShakeRotationDisplacement = new Quaternionf(0, 0, 1, 0);
    static double cameraShakeMagnitude = 0;
    static int cameraShakeDuration = 0;
    static int cameraShakeFadeDuration = 0;
    static int screenShakeTickCounter = 0;
    static boolean doWorldRendering = true;



    public static void setCameraDisplacement(Vec3d cameraDisplacement) {
        CameraDisplacement = cameraDisplacement;
    }

    public static Vec3d getCameraDisplacement() {return CameraDisplacement.add(CameraShakeDisplacement);}

    public static Quaternionf getCameraRotation() {
        return CameraRotationDisplacement;
    }

    public static void cameraShake(double cameraShakeMagnitude, int cameraShakeDuration, int cameraShakeFadeDuration){
        LazuliCameraManager.cameraShakeMagnitude = cameraShakeMagnitude;
        LazuliCameraManager.cameraShakeDuration = cameraShakeDuration;
        LazuliCameraManager.cameraShakeFadeDuration = cameraShakeFadeDuration;
        screenShakeTickCounter = 0;
    }

    public static void setDoWorldRendering(boolean doWorldRender){
        doWorldRendering = doWorldRender;
    }

    public static boolean isDoWorldRendering(){
        return  doWorldRendering;
    }

    public static Vec3d getPlayerCameraRotation(PlayerEntity player) {
        Vec3d forward = new Vec3d(0, 0, -1);
        forward = forward.rotateY((float) Math.toRadians(player.getYaw()));
        forward = forward.rotateX((float) Math.toRadians(player.getPitch()));
        return forward;
    }

}
