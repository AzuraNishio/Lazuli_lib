package lazuli_lib.lazuli;

import lazuli_lib.lazuli.rendering.LazuliHudRenderStep;
import lazuli_lib.lazuli.rendering.RendererParticleSpawner;
import lazuli_lib.lazuli.rendering.WorldRenderQueueManager;
import lazuli_lib.lazuli.rendering.lazuli_particles.ModClientParticles;
import lazuli_lib.lazuli.rendering.lazuli_particles.ModParticles;
import lazuli_lib.lazuli.utill.Triangle;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.math.Vec3d;

public class LazuliClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//LazuliHudRenderStep.register(); //thats WIP not a priority
        ModParticles.registerModParticles();
        ModClientParticles.RegisterClientParticles();
		RendererParticleSpawner.register();

		WorldRenderQueueManager.addTriangle(new Triangle(
				new Vec3d(0, 0, 1),
				new Vec3d(0, 1, 1),
				new Vec3d(1, 1, 1),
				0xFFFFFF00 // yellow
		));

		WorldRenderQueueManager.addTriangle(new Triangle(
				new Vec3d(0, 0, 1),
				new Vec3d(1, 1, 1),
				new Vec3d(1, 0, 1),
				0xFFFF0000 //red
		));
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}