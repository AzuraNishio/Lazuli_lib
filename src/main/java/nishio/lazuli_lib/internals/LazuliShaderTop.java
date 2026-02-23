package nishio.lazuli_lib.internals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.shaders.LazuliUniform;
import nishio.lazuli_lib.core.world_rendering.LazuliVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LazuliShaderTop<T extends LazuliShaderTop<T>> {
    public Identifier vertexId;
    public Identifier fragmentId;
    protected final Identifier jsonId;
    protected final Map<String, LazuliUniform<?>> uniforms;
    protected final VertexFormat vertexFormat;
    protected final List<String> samplers;

    protected LazuliShaderTop(String namespace, String jsonPath, String fragmentPath, String vertexPath,
                              VertexFormat vertexFormat, Map<String, LazuliUniform<?>> uniforms, List<String> samplers) {
        this.uniforms = uniforms;
        this.vertexId = Identifier.of(namespace, vertexPath);
        this.fragmentId = Identifier.of(namespace, fragmentPath);
        this.jsonId = Identifier.of(namespace, jsonPath);
        this.vertexFormat = vertexFormat;
        this.samplers = samplers;
    }

    protected LazuliShaderTop(Identifier jsonPath, Identifier fragmentPath, Identifier vertexPath,
                              VertexFormat vertexFormat, Map<String, LazuliUniform<?>> uniforms, List<String> samplers) {
        this.uniforms = uniforms;
        this.vertexId = vertexPath;
        this.fragmentId = fragmentPath;
        this.jsonId = jsonPath;
        this.vertexFormat = vertexFormat;
        this.samplers = samplers;
    }

    protected LazuliShaderTop(JsonObject shaderJson, Identifier jsonPath) {
        this.vertexId = Identifier.tryParse(shaderJson.get("vertex").getAsString());
        this.fragmentId = Identifier.of(shaderJson.get("fragment").getAsString());
        this.jsonId = jsonPath;
        this.vertexFormat = VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL;
        this.samplers = new ArrayList<>();
        this.uniforms = new HashMap<>();
        for (JsonElement samplerElement :shaderJson.get("samplers").getAsJsonArray().asList()){
            this.samplers.add(samplerElement.getAsJsonObject().get("name").getAsString());
        }

        for (JsonElement uniformElement : shaderJson.get("uniforms").getAsJsonArray().asList()) {
            JsonObject uniformObject = uniformElement.getAsJsonObject();
            String name = uniformObject.get("name").getAsString();
            String type = uniformObject.get("type").getAsString();
            int count = uniformObject.get("count").getAsInt();
            float[] values = new float[count];
            List<JsonElement> valueList = uniformObject.get("values").getAsJsonArray().asList();
            for (int i = 0; i < valueList.size(); i++) {
                values[i] = valueList.get(i).getAsFloat();
            }
            LazuliUniform<?> uniform = new LazuliUniform<>(name, type, count, values);
            this.uniforms.put(name, uniform);
        }
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public T addUniform(LazuliUniform<?> u){
        this.uniforms.put(u.name, u);
        return self();
    }

    public abstract T addDefaultUniforms();

    public T addSampler(String s){
        this.samplers.add(s);
        return self();
    }

    public abstract void minecraftRegister();

    public Identifier vertexId() { return vertexId; }
    public Identifier jsonId() { return jsonId; }
    public Identifier fragmentId() { return fragmentId; }

    public abstract String jsonPath();

    public List<String> getVertexAttributesNames() {
        return vertexFormat.getAttributeNames();
    }

    public LazuliUniform<?> getUniform(String name) {
        return uniforms.get(name);
    }


    public JsonObject toJson() {
        JsonObject shaderJson = new JsonObject();

        // Adding the vertex and fragment shaders
        shaderJson.addProperty("vertex", this.vertexId().toString());
        shaderJson.addProperty("fragment", this.fragmentId().toString());

        // Adding Attributes
        JsonArray attributesJson = new JsonArray();
        for (String attribute : this.getVertexAttributesNames()) {
            attributesJson.add(attribute);
        }
        shaderJson.add("attributes", attributesJson);

        // Adding Samplers
        JsonArray samplersJson = new JsonArray();
        for (String sampler : this.samplers) {
            JsonObject samplerJson = new JsonObject();
            samplerJson.addProperty("name", sampler);
            samplersJson.add(samplerJson);
        }
        shaderJson.add("samplers", samplersJson);

        // Adding Uniforms
        JsonArray uniformJson = new JsonArray();
        for (LazuliUniform<?> uniform : this.uniforms.values()) {
            uniformJson.add(uniform.toJsonObject());
        }
        shaderJson.add("uniforms", uniformJson);

        return shaderJson;
    }
}