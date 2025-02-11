package lazuli_lib.lazuli;

import lazuli_lib.lazuli.rendering.LazuliHudRenderManager;
import lazuli_lib.lazuli.rendering.LazuliHudRenderStep;
import lazuli_lib.lazuli.rendering.RendererParticleSpawner;
import lazuli_lib.lazuli.lazuli_particles.ModClientParticles;
import lazuli_lib.lazuli.lazuli_particles.ModParticles;
import lazuli_lib.lazuli.preset_renders.PortalVfxManager;
import lazuli_lib.test_mod.TestVfx;
import net.fabricmc.api.ClientModInitializer;

public class LazuliClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LazuliHudRenderStep.register(); //thats WIP not a priority
        ModParticles.registerModParticles();
        ModClientParticles.RegisterClientParticles();
		RendererParticleSpawner.register();
		LazuliHudRenderManager.register();

		TestVfx.runTestRenders();
	}
}