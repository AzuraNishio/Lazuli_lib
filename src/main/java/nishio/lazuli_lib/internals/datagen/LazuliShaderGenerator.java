package nishio.lazuli_lib.internals.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.internals.LazuliShaderTop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//Thanks Nico44YT for the datagen code!

public abstract class LazuliShaderGenerator implements DataProvider {

    protected List<LazuliShaderTop<?>> shaderList;

    protected final DataOutput.PathResolver shaderFolderPathResolver;

    protected final Map<Identifier, JsonObject> shaderJsons;

    public LazuliShaderGenerator(DataOutput output, List<LazuliShaderTop<?>> shaderList) {
        this.shaderFolderPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "shaders");

        this.shaderJsons = new HashMap<>();

        this.shaderList = shaderList;

        generate();
    }

    public void generate(){
        for (LazuliShaderTop s : shaderList){
            registerShader(s);
            s.minecraftRegister();
        }
    };

    public void registerShader(LazuliShaderTop s) {
        shaderJsons.put(s.jsonId().withPrefixedPath(s.jsonPath()), s.toJson());
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
