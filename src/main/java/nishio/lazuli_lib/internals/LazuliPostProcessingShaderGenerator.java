package nishio.lazuli_lib.internals;

import com.google.gson.JsonObject;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.shaders.LazuliShader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//Thanks Nico44YT for the datagen code!

public abstract class LazuliPostProcessingShaderGenerator implements DataProvider {

    protected List<LazuliShader> shaderList;

    protected final DataOutput.PathResolver shaderFolderPathResolver;

    protected final Map<Identifier, JsonObject> shaderJsons;

    public LazuliPostProcessingShaderGenerator(DataOutput output, List<LazuliShader> shaderList) {
        this.shaderFolderPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "shaders");

        this.shaderJsons = new HashMap<>();

        this.shaderList = shaderList;

        generate();
    }

    public void generate(){
        for (LazuliShader s : shaderList){
            registerShader(s);
            s.register();
        }
    };

    public void registerShader(LazuliShader s) {
        shaderJsons.put(s.jsonId.withPrefixedPath("core/"), s.toJson());
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        return CompletableFuture.allOf(
                shaderJsons.entrySet().stream()
                        .map(
                                entry -> DataProvider.writeToPath(
                                        writer,
                                        entry.getValue(),
                                        shaderFolderPathResolver.resolve(entry.getKey(), "json"))
                        ).toArray(CompletableFuture[]::new)

        );

    }
}
