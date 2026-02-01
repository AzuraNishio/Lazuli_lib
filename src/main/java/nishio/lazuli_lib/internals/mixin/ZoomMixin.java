package nishio.lazuli_lib.mixin;
/** Mixin adjusting FOV when zooming. */


import net.minecraft.client.render.GameRenderer;
import nishio.lazuli_lib.core.post_processing.LazuliZoom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(GameRenderer.class)
public class ZoomMixin {
	@Inject(at = @At("RETURN"), method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", cancellable = true)
	public void getZoomLevel(CallbackInfoReturnable<Double> callbackInfo) {
		double fov = callbackInfo.getReturnValue();

		if (LazuliZoom.isZooming()) {
			double zoomedFov = fov * LazuliZoom.getZoomFactor();

			// Clamp only the upper bound to avoid upside-down camera
			// (Clamp max FOV just below 180 degrees, so no flip)
			if (zoomedFov >= 179.9) {
				zoomedFov = 179.9;
			}
			// No minimum clamp => allow infinite zoom in (zoomedFov can be very small)

			callbackInfo.setReturnValue(zoomedFov);
		} else {
			callbackInfo.setReturnValue(fov);
		}
	}


}