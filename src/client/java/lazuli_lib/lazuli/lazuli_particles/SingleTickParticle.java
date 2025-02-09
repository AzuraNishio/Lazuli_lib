package lazuli_lib.lazuli.lazuli_particles;

import lazuli_lib.lazuli.data_containers.LazuliLine;
import lazuli_lib.lazuli.rendering.RenderingHelper;
import lazuli_lib.lazuli.rendering.LazuliWorldRenderQueueManager;
import lazuli_lib.lazuli.data_containers.Triangle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.Camera;

@Environment(EnvType.CLIENT)
public class SingleTickParticle extends SpriteBillboardParticle {
	private final Vec3d start;
	private final Vec3d end;

	protected SingleTickParticle(ClientWorld level, double xCoord, double yCoord, double zCoord,
								 SpriteProvider spriteSet, double xd, double yd, double zd,
								 Vec3d start, Vec3d end) {
		super(level, xCoord, yCoord, zCoord, xd, yd, zd);

		this.start = start;
		this.end = end;

		this.velocityMultiplier = 0F; // Prevents velocity from affecting movement
		this.scale = 0.08f;
		this.maxAge = 1;
		this.setSpriteForAge(spriteSet);

		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;
	}

	@Override
	public void tick() {
		super.tick();
		this.markDead(); // Ensures the particle lasts only one tick
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {


		// Texture UV coordinates
		float minU = this.getMinU();
		float maxU = this.getMaxU();
		float minV = this.getMinV();
		float maxV = this.getMaxV();

		// Full brightness
		int light = 15728880;

		// Iterate through the queued triangles and render each
		for (Triangle tri : LazuliWorldRenderQueueManager.getTriangleQueue()) {
			int[] color = tri.getColorAsArray();

			// Render the triangle using the helper
			RenderingHelper.renderQuadDoubleSided(
					vertexConsumer,
					camera,
					tri.getVertex1(),
					tri.getVertex2(),
					tri.getVertex3(),
					tri.getVertex3(), // Repeated last vertex (since it's a quad function)
					minU, minV, maxU, maxV, color, light
			);
		}
		for (LazuliLine line : LazuliWorldRenderQueueManager.getLineQueue()) {
			int[] color = line.getColorAsArray();

			// Render the triangle using the helper
			RenderingHelper.renderPlayerFacingQuad(
					vertexConsumer,
					line.getVertex1(),
					line.getVertex2(),
					line.getWidth(), line.getZ(),
					minU, minV, maxU, maxV, color, light, camera
			);
		}
	}



	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider sprites;

		public Factory(SpriteProvider spriteSet) {
			this.sprites = spriteSet;
		}

		@Override
		public Particle createParticle(DefaultParticleType particleType, ClientWorld level, double x, double y, double z,
									   double dx, double dy, double dz) {
			// Use default start and end as placeholders
			Vec3d start = new Vec3d(0, 0, 0);
			Vec3d end = new Vec3d(1, 1, 1);

			// Pass custom arguments (e.g., start and end positions) to the particle
			return new SingleTickParticle(level, x, y, z, this.sprites, dx, dy, dz, start, end);
		}
	}
}
