package nishio.lazuli_lib.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

public class LazuliZoom {
    /** Current zoom multiplier (e.g. 0.25 for 4× zoom) */
    private static double zoomFactor = 1.0;

    /** Are we currently zoom‑mode on? */
    private static boolean zooming = false;

    /** Sensitivity before zoom so we can restore it */
    private static double oldSensitivity = 0.0;

    /**
     * Toggle zoom on/off.  Uses the last set zoomFactor.
     * On enter: stores old sensitivity and applies zoomFactor.
     * On exit: restores original sensitivity.
     */
    public static void toggleZoom() {
        MinecraftClient client = MinecraftClient.getInstance();
        SimpleOption<Double> sensOpt = client.options.getMouseSensitivity();

        if (!zooming) {
            // Entering zoom
            oldSensitivity = sensOpt.getValue();
            sensOpt.setValue(oldSensitivity / (zoomFactor*2));
        } else {
            // Exiting zoom
            sensOpt.setValue(oldSensitivity);
        }

        zooming = !zooming;
    }

   public static void startZoom (){
        if (!zooming){
            toggleZoom();
        }
   }

    public static void stopZoom (){
        if (zooming){
            toggleZoom();
        }
    }

    public static void setZoom(double factor) {
        zoomFactor = factor;
    }

    public static boolean isZooming() {
        return zooming;
    }

    public static double getZoomFactor() {
        return 1.0 / zoomFactor;
    }
}
