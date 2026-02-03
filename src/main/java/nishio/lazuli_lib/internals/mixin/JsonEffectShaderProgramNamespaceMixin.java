package nishio.lazuli_lib.internals.mixin;

import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.resource.ResourceFactory;
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
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Identifier;ofVanilla(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"
            )
    )
    private static Identifier redirectIdentifierCreation(String path, ResourceFactory resourceFactory, ShaderStage.Type type, String name) {

        if (name.contains(":")) {
            Identifier parsedId = Identifier.of(name);

            if (name.contains("post/")) {
                return Identifier.of(
                        parsedId.getNamespace(),
                        "shaders/" + parsedId.getPath() + type.getFileExtension()
                );
            }

            return Identifier.of(
                    parsedId.getNamespace(),
                    "shaders/program/" + parsedId.getPath() + type.getFileExtension()
            );
        }

        // Fall back to vanilla behavior
        return Identifier.ofVanilla(path);
    }
}