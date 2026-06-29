package nishio.lazuli_lib.internals.mixin;


import me.jellysquid.mods.sodium.client.gl.shader.GlShader;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderConstants;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.internals.LazuliLog;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


@Mixin(ShaderLoader.class)
public class LazuliSodiumShaderMixin {


	/**
	 * @author Azura Nishio
	 * @reason Make Sodium load shaders from lazuli_gen, if possible
	 */
	@Inject(
			method = "Lme/jellysquid/mods/sodium/client/gl/shader/ShaderLoader;getShaderSource(Lnet/minecraft/util/Identifier;)Ljava/lang/String;",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void getShaderSource(Identifier id, CallbackInfoReturnable<String> cir) {
		String path = String.format("/assets/%s/shaders/%s", id.getNamespace(), id.getPath());
		LazuliLog.Warp.info("Sodium currently loading {}!", id);
		Optional<Resource> shader = MinecraftClient.getInstance().getResourceManager().getResource(Identifier.of(id.getNamespace(), "shaders/%s".formatted(id.getPath())));
		if (shader.isPresent()){
			try (InputStream in = shader.get().getInputStream()) {
				if (in != null) {
					LazuliLog.Warp.info("Redirecting sodium shader %s to LazuliGen!".formatted(id));
					cir.setReturnValue(IOUtils.toString(in, StandardCharsets.UTF_8));
				}
			} catch (IOException e) {
				LazuliLog.Warp.warn("Failed redirect!", e);

			}
		}
	}


}