package lazuli_lib.lazuli;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import lazuli_lib.lazuli.data_containers.LazuliLine;
import lazuli_lib.lazuli.rendering.LazuliHudRenderManager;
import lazuli_lib.lazuli.rendering.LazuliHudRenderStep;
import lazuli_lib.lazuli.rendering.RendererParticleSpawner;
import lazuli_lib.lazuli.rendering.LazuliWorldRenderQueueManager;
import lazuli_lib.lazuli.lazuli_particles.ModClientParticles;
import lazuli_lib.lazuli.lazuli_particles.ModParticles;
import lazuli_lib.lazuli.data_containers.Triangle;
import lazuli_lib.lazuli.utill.LazuliMathUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class LazuliClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LazuliHudRenderStep.register(); //thats WIP not a priority
        ModParticles.registerModParticles();
        ModClientParticles.RegisterClientParticles();
		RendererParticleSpawner.register();
		LazuliHudRenderManager.register();


if(false) {
	LazuliWorldRenderQueueManager.addTriangle(new Triangle(
			new Vec3d(0, 0, 1),
			new Vec3d(0, 1, 1),
			new Vec3d(1, 1, 1),
			0xFFFFFF00 // yellow
	));

	LazuliWorldRenderQueueManager.addTriangle(new Triangle(
			new Vec3d(0, 0, 1),
			new Vec3d(1, 1, 1),
			new Vec3d(1, 0, 1),
			0xFFFF0000 //red
	));

	LazuliWorldRenderQueueManager.addLine(new LazuliLine(
			new Vec3d(5, 5, 5),
			new Vec3d(-5, 6, 6),
			0.15,
			0,
			LazuliMathUtils.rgbaToInt(255, 0, 0, 200)
	));

	LazuliWorldRenderQueueManager.addLine(new LazuliLine(
			new Vec3d(5, 5, 5),
			new Vec3d(-5, 6, 6),
			0.07,
			0.05,
			LazuliMathUtils.rgbaToInt(255, 40, 40, 255)
	));

	LazuliHudRenderManager.flashWithForce(new int[]{150, 0, 0, 255}, 1);
	LazuliHudRenderManager.setScreenTintOverlay(new int[]{0, 0, 30, 30});
}

	}
}