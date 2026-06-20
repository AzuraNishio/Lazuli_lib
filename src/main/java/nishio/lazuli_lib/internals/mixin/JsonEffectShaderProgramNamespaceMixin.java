package nishio.lazuli_lib.internals.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModDependency;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.internals.Lazuli_Lib;
import nishio.lazuli_lib.internals.Lazuli_Lib_Client;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;


@Mixin(value = JsonEffectShaderProgram.class, priority = 1000)
public class JsonEffectShaderProgramNamespaceMixin {
    @Unique
    private static final Set<String> OPT_IN_MODS = FabricLoader.getInstance()
            .getAllMods()
            .stream()
            .filter(
                    it -> it.getMetadata()
                            .getDependencies()
                            .stream()
                            .anyMatch(dep -> dep.getKind().equals(ModDependency.Kind.DEPENDS) && dep.getModId().equals("<mod_id_here>"))
            )
            .map(it -> it.getMetadata().getId())
            .collect(Collectors.toSet());

    @WrapOperation(
            method = "loadEffect",
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/util/Identifier;"
            )
    )
    private static Identifier redirectIdentifierCreation(String id, Operation<Identifier> original) {

        //okay I wanted to say this for anyone who reads this code but genuinely the fabric discord was so helpful to me on this -matscalle
        if (id.contains(":")) {
            Identifier parsedId = new Identifier(id.replace("shaders/program/", ""));
            if (!OPT_IN_MODS.contains(parsedId.getNamespace())) {
                if (id.contains("post/")) {
                    return Identifier.of(
                            parsedId.getNamespace(),
                            "shaders/" + parsedId.getPath()
                    );
                }
                return Identifier.of(
                        parsedId.getNamespace(),
                        "shaders/program/" + parsedId.getPath()
                );
            }

            return new Identifier(id);

        }

        // Fall back to vanilla behavior
        return new Identifier(id);
    }
}