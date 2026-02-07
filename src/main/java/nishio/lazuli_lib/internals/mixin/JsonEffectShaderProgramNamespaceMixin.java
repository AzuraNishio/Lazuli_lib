package nishio.lazuli_lib.internals.mixin;

import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(JsonEffectShaderProgram.class)
public class JsonEffectShaderProgramNamespaceMixin {

    @Redirect(
            method = "loadEffect",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/util/Identifier;",
                    ordinal = 0
            )
    )
    private static Identifier redirectIdentifierCreation(String id, ResourceManager resourceManager, ShaderStage.Type type, String name) {

        if (name.contains(":")) {
            Identifier parsedId = new Identifier(name);

            if (name.contains("post/")) {
                return new Identifier(
                        parsedId.getNamespace(),
                        "shaders/" + parsedId.getPath() + type.getFileExtension()
                );
            }

            return new Identifier(
                    parsedId.getNamespace(),
                    "shaders/program/" + parsedId.getPath() + type.getFileExtension()
            );
        }

        // Fall back to vanilla behavior
        return new Identifier(id);
    }
}