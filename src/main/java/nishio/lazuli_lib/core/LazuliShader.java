package nishio.lazuli_lib.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class LazuliShader {
    public final Identifier vertexId;
    public final Identifier fragmentId;
    public final Identifier jsonId;
    public Map<String, LazuliUniform<?>> Uniforms;
    public VertexFormat vertexFormat;
    public LazuliBlendMode blendMode;
    public List<String> samplers;

    public LazuliShader(String namespace, String jsonPath, String fragmentPath, String vertexPath, LazuliBlendMode blendMode, VertexFormat vertexFormat, Map<String, LazuliUniform<?>> uniforms, List<String> samplers){
        this.Uniforms = uniforms;
        this.vertexId = Identifier.of(namespace, vertexPath);
        this.fragmentId = Identifier.of(namespace, fragmentPath);
        this.jsonId = Identifier.of(namespace, jsonPath);
        this.blendMode = blendMode;
        this.vertexFormat = vertexFormat;
        this.samplers = samplers;
    }

    public LazuliShader(Identifier jsonPath, Identifier fragmentPath, Identifier vertexPath, LazuliBlendMode blendMode, VertexFormat vertexFormat, Map<String, LazuliUniform<?>> uniforms, List<String> samplers){
        this.Uniforms = uniforms;
        this.vertexId = vertexPath;
        this.fragmentId = fragmentPath;
        this.jsonId = jsonPath;
        this.blendMode = blendMode;
        this.vertexFormat = vertexFormat;
        this.samplers = samplers;
    }

    public void register(){
        LazuliShaderRegistry.registerShader(jsonId, vertexFormat);
    }

    public ShaderProgram getProgram(){
        return LazuliShaderRegistry.getShaderFromName(jsonId.getPath());
    };

    public Identifier vertexId(){return vertexId;}
    public Identifier jsonId(){return jsonId;}
    public Identifier fragmentId(){return fragmentId;}

    public List<String> getVertexAttributesNames() {
        return vertexFormat.getAttributeNames();
    }

    public LazuliShader setSampler(String sampler, Identifier texture){
        LapisRenderer.setShaderTexture(samplers.indexOf(sampler), texture);
        return this;
    }

    public LazuliUniform<?> getUniform(String name) {
        return Uniforms.get(name);
    }

    public LazuliShader setUniform(String name, Object value) {
        Uniforms.get(name).setShaderUniformGeneric(this.getProgram(), value);
        return this;
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
        for (LazuliUniform<?> uniform : this.Uniforms.values()) {
            uniformJson.add(uniform.toJsonObject());
        }
        shaderJson.add("uniforms", uniformJson);

        return shaderJson;
    }
}
