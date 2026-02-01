package nishio.lazuli_lib.core;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class LazuliShader {
    public final String vertexPath;
    public final String fragmentPath;
    public final String jsonPath;
    public final String namespace;
    public Map<String, LazuliUniform<?>> Uniforms;
    public VertexFormat vertexFormat;
    public LazuliBlendMode blendMode;
    public List<String> samplers;

    public LazuliShader(String namespace, String vertexPath, String fragmentPath, String jsonPath, LazuliBlendMode blendMode, VertexFormat vertexFormat, Map<String, LazuliUniform<?>> uniforms, List<String> samplers){
        this.namespace = namespace;
        this.Uniforms = uniforms;
        this.vertexPath = vertexPath;
        this.fragmentPath = fragmentPath;
        this.jsonPath = jsonPath;
        this.blendMode = blendMode;
        this.vertexFormat = vertexFormat;
        this.samplers = samplers;
    }

    public void register(){
        LazuliShaderRegistry.registerShader(jsonPath, namespace, vertexFormat);
    }

    public ShaderProgram getProgram(){
        return LazuliShaderRegistry.getShaderFromName(jsonPath);
    };

    public Identifier vertexId(){return Identifier.of(namespace, vertexPath);}
    public Identifier jsonId(){return Identifier.of(namespace, jsonPath);}
    public Identifier fragmentId(){return Identifier.of(namespace, fragmentPath);}

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



}
