package nishio.lazuli_lib.internals.mixin;


import me.jellysquid.mods.sodium.client.gl.shader.uniform.*;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkShaderOptions;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.internals.compat.LazuliSodiumResourcePackCompat;
import nishio.test_mod.TestModShaders;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Mixin(ChunkShaderInterface.class)
public class LazuliSodiumChunkShaderMixin {


	/**
	 * @author Azura Nishio
	 * @reason Register custom uniforms in shader
	 */
	@Inject(
			method = "<init>(Lme/jellysquid/mods/sodium/client/render/chunk/shader/ShaderBindingContext;Lme/jellysquid/mods/sodium/client/render/chunk/shader/ChunkShaderOptions;)V",
			at = @At("HEAD")
	)
	private static void getShaderSource(ShaderBindingContext context, ChunkShaderOptions options, CallbackInfo ci) {
		for (LazuliUniform<?> u : LazuliSodiumResourcePackCompat.sodiumLazuliUniforms.values()){
			List<GlUniform<?>> uniforms = LazuliSodiumResourcePackCompat.sodiumGlUniforms.getOrDefault(u.name, new ArrayList<>());

			switch (u.type) {
				case INT -> uniforms.add(
                        context.bindUniform(u.name, GlUniformInt::new)
                );
				case FLOAT -> uniforms.add(
                        context.bindUniform(u.name, GlUniformFloat::new)
                );
				case VEC3D -> uniforms.add(
                        context.bindUniform(u.name, GlUniformFloat3v::new)
                );
				case COLOR, VEC4FJ -> uniforms.add(
                        context.bindUniform(u.name, GlUniformFloat4v::new)
                );
				case MAT4F -> uniforms.add(
                        context.bindUniform(u.name, GlUniformMatrix4f::new)
                );
				case FREE -> {
					switch (u.freeType) {
						case "float" -> uniforms.add(context.bindUniform(u.name, GlUniformFloat::new));
						case "vec3"  -> uniforms.add(context.bindUniform(u.name, GlUniformFloat3v::new));
						case "vec4"  -> uniforms.add(context.bindUniform(u.name, GlUniformFloat4v::new));
						case "mat4"  -> uniforms.add(context.bindUniform(u.name, GlUniformMatrix4f::new));
					}
				}
			}
			LazuliSodiumResourcePackCompat.sodiumGlUniforms.put(u.name, uniforms);
		}
	}


	/**
	 * @author Azura Nishio
	 * @reason Register custom uniforms in shader
	 */
	@SuppressWarnings("target")
	@Inject(
			method = "setProjectionMatrix",
			at = @At("HEAD"),
			remap = false
	)
	private static void uniformSetCallback(Matrix4fc matrix, CallbackInfo ci) {
		LazuliSodiumResourcePackCompat.scheduledUniformFloat.forEach((u, v) -> {
			for (GlUniform<?> uniform : LazuliSodiumResourcePackCompat.sodiumGlUniforms.get(u)){
				if (v.length == 1){
					((GlUniformFloat) uniform).set(v[0]);
				}
				else{
					((GlUniform<float[]>) uniform).set(v);
				}
			}
		});
	}

}