package nishio.lazuli_lib.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {

    @Inject(
        method = "renderParticles",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;applyModelViewMatrix()V",
            shift = At.Shift.AFTER
        )
    )
    private void injectAfterModelViewMatrix(
        MatrixStack matrices,
        VertexConsumerProvider.Immediate vertexConsumers,
        LightmapTextureManager lightmapTextureManager,
        Camera camera,
        float tickDelta,
        CallbackInfo ci
    ) {

    }
}
