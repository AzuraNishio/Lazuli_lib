package nishio.lazuli_lib.core.shaders;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.world_rendering.LapisRenderer;
import nishio.lazuli_lib.internals.LazuliShaderTop;
import nishio.lazuli_lib.internals.LazulidefaultUniforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LazuliShader extends LazuliShaderTop<LazuliShader> {

    public LazuliShader(String namespace, String jsonPath, String fragmentPath, String vertexPath,
                        VertexFormat vertexFormat, Map<String, LazuliUniform<?>> uniforms, List<String> samplers) {
        super(namespace, jsonPath, fragmentPath, vertexPath, vertexFormat, uniforms, samplers);
    }

    public LazuliShader(Identifier jsonPath, Identifier fragmentPath, Identifier vertexPath,
                        VertexFormat vertexFormat, Map<String, LazuliUniform<?>> uniforms, List<String> samplers) {
        super(jsonPath, fragmentPath, vertexPath, vertexFormat, uniforms, samplers);
    }

    public LazuliShader(Identifier jsonPath, Identifier fragmentPath, Identifier vertexPath,
                        VertexFormat vertexFormat) {
        super(jsonPath, fragmentPath, vertexPath, vertexFormat, new HashMap<>(), new ArrayList<>());
    }

    public LazuliShader(Identifier jsonPath, Identifier fragmentPath, Identifier vertexPath) {
        super(jsonPath, fragmentPath, vertexPath, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, new HashMap<>(), new ArrayList<>());
    }

    public LazuliShader(Identifier fragmentPath, Identifier vertexPath) {
        super(fragmentPath, fragmentPath, vertexPath, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, new HashMap<>(), new ArrayList<>());
    }

    public LazuliShader(Identifier fragmentPath) {
        super(fragmentPath, fragmentPath, Identifier.ofVanilla("position_color_tex_lightmap"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, new HashMap<>(), new ArrayList<>());
    }

    @Override
    public LazuliShader addDefaultUniforms() {
        for(LazuliUniform<?> u : LazulidefaultUniforms.defaultUniforms){
            uniforms.put(u.name, u);
        }
        return this;
    }

    @Override
    public void minecraftRegister() {
        LazuliShaderRegistry.registerShader(jsonId, vertexFormat);
    }

    public LazuliShader register(){
        LazuliShaderRegistry.registerShader(this);
        return this;
    }

    @Override
    public String jsonPath(){return "core/";}

    public ShaderProgram getProgram() {
        return LazuliShaderRegistry.getShaderFromName(jsonId.getPath());
    }

    public LazuliShader setSampler(String sampler, Identifier texture) {
        LapisRenderer.setShaderTexture(samplers.indexOf(sampler), texture);
        return this;
    }

    public LazuliShader setUniform(String name, Object value) {
        uniforms.get(name).setShaderUniformGeneric(this.getProgram(), value);
        return this;
    }
}