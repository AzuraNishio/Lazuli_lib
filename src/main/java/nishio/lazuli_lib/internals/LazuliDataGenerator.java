package nishio.lazuli_lib.internals;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.LazuliShader;
import nishio.lazuli_lib.core.LazuliUniform;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//Thanks Nico44YT for the datagen code!

public abstract class LazuliDataGenerator implements DataProvider {

    protected List<LazuliShader> shaderList;

    protected final DataOutput.PathResolver shaderFolderPathResolver;

    protected final Map<Identifier, JsonObject> shaderJsons;

    public LazuliDataGenerator(DataOutput output, List<LazuliShader> shaderList) {
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
        JsonObject shaderJson = new JsonObject();

        // Adding the vertex and fragment shaders
        shaderJson.addProperty("vertex", s.vertexId().toString());
        shaderJson.addProperty("fragment", s.fragmentId().toString());

        // Adding Attributes
        JsonArray attributesJson = new JsonArray();
        for (String attribute : s.getVertexAttributesNames()) {
            attributesJson.add(attribute);
        }

        shaderJson.add("attributes", attributesJson);

        // Adding Samplers
        JsonArray samplersJson = new JsonArray();
        for (String sampler : s.samplers) {
            JsonObject samplerJson = new JsonObject();

            samplerJson.addProperty("name", sampler);
            samplersJson.add(samplerJson);
        }
        shaderJson.add("samplers", samplersJson);

        // Adding Uniforms
        JsonArray uniformJson = new JsonArray();
        for (LazuliUniform<?> uniform : s.Uniforms.values()) {
            uniformJson.add(uniform.toJsonObject());
        }
        shaderJson.add("uniforms", uniformJson);

        // Putting the shader json into the map
        shaderJsons.put(Identifier.of(s.namespace, "core/" + s.jsonPath), shaderJson);
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
