package lazuli_lib.lazuli.lazuli_particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;


public class ModClientParticles {
    public static void RegisterClientParticles(){
        ParticleFactoryRegistry.getInstance().register(ModParticles.RENDERER_PARTICLE, SingleTickParticle.Factory::new);

    }
}
