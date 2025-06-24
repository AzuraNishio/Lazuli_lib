package nishio.lazuli_lib.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceFactory;
import nishio.lazuli_lib.core.LazuliPostProcessingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GameRenderer.class)
public class PostProcessingRegistryMixin {
    @Inject(method = "loadPrograms",
            at = @At("HEAD"),
            cancellable = true)
    void loadCustomPrograms(ResourceFactory factory, CallbackInfo ci) throws IOException {
        MinecraftClient client = MinecraftClient.getInstance();
        LazuliPostProcessingRegistry.runCallbacks(client, factory);
    }
}
