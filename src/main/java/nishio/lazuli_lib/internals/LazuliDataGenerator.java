package nishio.lazuli_lib.internals;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//Thanks Nico44YT for the datagen code!

public abstract class LazuliDataGenerator implements DataProvider {

    protected final DataOutput.PathResolver shaderFolderPathResolver;

    protected final Map<Identifier, JsonObject> shaderJsons;

    public LazuliDataGenerator(DataOutput output) {
        this.shaderFolderPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "shaders");

        this.shaderJsons = new HashMap<>();

        generate();
    }

    public void generate(){
        test();
    };

    public void registerShader(Identifier shaderId, Identifier vertexId, Identifier fragmentId, String[] attributes, String[] samplers, Uniform[] uniforms) {
        JsonObject shaderJson = new JsonObject();

        // Adding the vertex and fragment shaders
        shaderJson.addProperty("vertex", vertexId.toString());
        shaderJson.addProperty("fragment", fragmentId.toString());

        // Adding Attributes
        JsonArray attributesJson = new JsonArray();
        for (String attribute : attributes) {
            attributesJson.add(attribute);
        }
        shaderJson.add("attributes", attributesJson);

        // Adding Samplers
        JsonArray samplersJson = new JsonArray();
        for (String sampler : samplers) {
            samplersJson.add(sampler);
        }
        shaderJson.add("samplers", samplersJson);

        // Adding Uniforms
        JsonArray uniformJson = new JsonArray();
        for (Uniform uniform : uniforms) {
            uniformJson.add(uniform.toJsonObject());
        }
        shaderJson.add("uniforms", uniformJson);

        // Putting the shader json into the map
        shaderJsons.put(shaderId, shaderJson);
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

    public record Uniform(String name, String uniformType, int count, float[] values) {
        public static final String FLOAT = "float";
        public static final String INT = "float";
        public static final String MATRIX4 = "matrix4x4";
        public static final String MATRIX3 = "matrix3x3";
        public static final String VEC4 = "vec4";
        public static final String VEC3 = "vec3";
        public static final String VEC2 = "vec2";

        public static Uniform of(String name, String uniformType, float[] values) {
            return new Uniform(name, uniformType, values.length, values);
        }

        public static Uniform of(String name, String uniformType, int count) {
            float[] values = new float[count];
            Arrays.fill(values, 0.0f);
            return new Uniform(name, uniformType, count, values);
        }

        public static Uniform of(String name, String uniformType, int count, float[] values) {
            return new Uniform(name, uniformType, count, values);
        }

        public JsonObject toJsonObject() {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("name", name);
            jsonObject.addProperty("type", uniformType);
            jsonObject.addProperty("count", count);

            JsonArray valuesArray = new JsonArray();
            for (float value : values) {
                valuesArray.add(value);
            }
            jsonObject.add("values", valuesArray);

            return jsonObject;
        }
    }


    public void test(){
            registerShader(
                    Lazuli_Lib.id("core/dissolve"),
                    Lazuli_Lib.id("dissolve"),
                    Lazuli_Lib.id("dissolve"),
                    new String[]{
                            "Position",
                            "Color",
                            "UV0", "UV1", "UV2",
                            "Normal"
                    },
                    new String[]{"Sampler0", "Sampler1", "Sampler2"},
                    new Uniform[]{
                            Uniform.of("ModelViewMat", Uniform.MATRIX4, 16),
                            Uniform.of("ProjMat", Uniform.MATRIX4, 16),
                            Uniform.of("IViewRotMat", Uniform.MATRIX3, 9),
                            Uniform.of("ColorModulator", Uniform.FLOAT, 4),
                            Uniform.of("Light0_Direction", Uniform.FLOAT, 3),
                            Uniform.of("Light1_Direction", Uniform.FLOAT, 3),
                            Uniform.of("FogStart", Uniform.FLOAT, 1),
                            Uniform.of("FogEnd", Uniform.FLOAT, 1),
                            Uniform.of("FogColor", Uniform.FLOAT, 1),
                            Uniform.of("FogShape", Uniform.INT, 1),
                            Uniform.of("Dissolve", Uniform.FLOAT, 1)
                    }
            );

    }

}
