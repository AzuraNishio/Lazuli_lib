package lazuli_lib.lazuli.core.rendering;

import lazuli_lib.lazuli.core.lazuli_particles.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class RendererParticleSpawner {

    public static void register() {
        // Register a tick event listener
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && client.player != null) {
                ClientPlayerEntity player = client.player;
                Vec3d pos = player.getPos(); // Get player position

                // Spawn the particle at the player's position
                client.world.addParticle(ModParticles.RENDERER_PARTICLE, true,
                        pos.x, pos.y + 1.5, pos.z, // Position (slightly above the player)
                        0, 0, 0 // Velocity (static particle)
                );
            }
        });
    }
}
