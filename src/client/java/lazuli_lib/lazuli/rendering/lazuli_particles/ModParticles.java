package lazuli_lib.lazuli.rendering.lazuli_particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static final DefaultParticleType RENDERER_PARTICLE = FabricParticleTypes.simple();
    public static void registerModParticles() {
        System.out.println("Registering cool particles");

        Registry.register(Registry.PARTICLE_TYPE, new Identifier("lazuli_lib", "render_group_particle"), RENDERER_PARTICLE);

    }
}
