package lazuli_lib.lazuli.acess;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class LazuliHudRenderManager {

    // Screen tint overlay color (RGBA)
    private static int[] ScreenTintOverlay = {0, 0, 0, 0};

    // Flash color and intensity
    private static int[] FlashColor = {0, 0, 0, 0};
    private static double FlashForce = 0;
    private static double SquaredFlashForce = 0;

    // Returns the current screen tint, blending with flash if active
    public static int[] getScreenTintOverlay() {
        return new int[]{
                (int) Math.round(ScreenTintOverlay[0] + (FlashColor[0] * SquaredFlashForce)),
                (int) Math.round(ScreenTintOverlay[1] + (FlashColor[1] * SquaredFlashForce)),
                (int) Math.round(ScreenTintOverlay[2] + (FlashColor[2] * SquaredFlashForce)),
                (int) Math.round(ScreenTintOverlay[3] + (FlashColor[3] * SquaredFlashForce))
        };
    }

    public static void flashWithForce(int[] color, double force) {
        FlashForce = force;
        FlashColor = color.clone(); // Ensures a separate copy of the array

        System.out.println("Flash triggered! Color: " + java.util.Arrays.toString(color) + ", Force: " + FlashForce);
    }


    // Sets the screen tint overlay
    public static void setScreenTintOverlay(int[] color) {
        ScreenTintOverlay = color.clone(); // Ensures a separate copy
    }

    // Registers the HUD rendering callback
    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            FlashForce = Math.max(0, FlashForce - (tickDelta * 0.006)); // Slower fade
            SquaredFlashForce = FlashForce * FlashForce;
        });

    }
}
