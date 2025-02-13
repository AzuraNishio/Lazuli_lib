package lazuli_lib.lazuli;

import lazuli_lib.lazuli.acess.LazuliHudRenderManager;
import lazuli_lib.lazuli.acess.LazuliPrimitives;
import lazuli_lib.lazuli.core.rendering.LazuliHudRenderStep;
import lazuli_lib.lazuli.core.rendering.RendererParticleSpawner;
import lazuli_lib.lazuli.core.lazuli_particles.ModClientParticles;
import lazuli_lib.lazuli.core.lazuli_particles.ModParticles;
import lazuli_lib.test_mod.TestVfx;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Lazy;

public class LazuliClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LazuliHudRenderStep.register(); //thats WIP not a priority
        ModParticles.registerModParticles();
        ModClientParticles.RegisterClientParticles();
		RendererParticleSpawner.register();
		LazuliHudRenderManager.register();
		LazuliPrimitives.registerPrimitives();

		TestVfx.runTestRenders();
	}
}