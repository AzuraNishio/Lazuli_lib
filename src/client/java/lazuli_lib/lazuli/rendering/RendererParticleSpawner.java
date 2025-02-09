package lazuli_lib.lazuli.rendering;

import lazuli_lib.lazuli.lazuli_particles.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;


@Environment(EnvType.CLIENT)
public class RendererParticleSpawner {

    public static void register() {
        // Register a tick event listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                client.world.addParticle(ModParticles.RENDERER_PARTICLE, true, 0, 0, 0, 0, 0, 0);

            }
        });
    }
}
