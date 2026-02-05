package nishio.lazuli_lib.core.shaders;

import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.framebuffers.LazuliFramebufferUtills;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.world_rendering.LapisRenderer;
import nishio.lazuli_lib.internals.LazuliShaderTop;
import nishio.lazuli_lib.internals.LazuliTrueFramebufferShader;
import nishio.lazuli_lib.internals.Lazuli_Lib;
import nishio.lazuli_lib.internals.LazulidefaultUniforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LazuliFramebufferShader extends LazuliShaderTop<LazuliFramebufferShader> {
    private LazuliBlendMode blendMode;

    public LazuliFramebufferShader(Identifier fragmentPath, Identifier vertexPath, LazuliBlendMode blendMode,
                                   Map<String, LazuliUniform<?>> uniforms, List<String> samplers) {
        super(fragmentPath, fragmentPath, vertexPath, VertexFormats.POSITION, uniforms, samplers);
        this.blendMode = blendMode;
        this.samplers.add("InputColor");
        this.samplers.add("InputDepth");
    }

    public LazuliFramebufferShader(Identifier fragmentPath, Identifier vertexPath, LazuliBlendMode blendMode) {
        super(fragmentPath, fragmentPath, vertexPath, VertexFormats.POSITION, new HashMap<>(), new ArrayList<>());
        this.blendMode = blendMode;
        this.samplers.add("InputColor");
        this.samplers.add("InputDepth");
    }

    public LazuliFramebufferShader(Identifier fragmentPath, Identifier vertexPath) {
        super(fragmentPath, fragmentPath, vertexPath, VertexFormats.POSITION, new HashMap<>(), new ArrayList<>());
        this.blendMode = LazuliBlendMode.DEFAULT;
        this.samplers.add("InputColor");
        this.samplers.add("InputDepth");
    }


    public LazuliFramebufferShader(Identifier fragmentPath) {
        super(fragmentPath, fragmentPath, Identifier.of(Lazuli_Lib.MOD_ID, "full_screen"), VertexFormats.POSITION, new HashMap<>(), new ArrayList<>());
        this.blendMode = LazuliBlendMode.DEFAULT;
        this.samplers.add("InputColor");
        this.samplers.add("InputDepth");
    }

    @Override
    public LazuliFramebufferShader addDefaultUniforms() {
        for(LazuliUniform<?> u : LazulidefaultUniforms.defaultFramebufferUniforms){
            uniforms.put(u.name, u);
        }
        return this;
    }

    @Override
    public void minecraftRegister() {
        LazuliShaderRegistry.registerPostProcessingShader(fragmentId.getPath(), fragmentId.getNamespace());
    }

    public LazuliFramebufferShader register(){
        LazuliShaderRegistry.registerShader(this);
        return this;
    }

    @Override
    public String jsonPath(){return "program/";}

    @Override
    public Identifier jsonId(){return Identifier.ofVanilla(fragmentId.getPath());}

    public LazuliTrueFramebufferShader getProgram() {
        return LazuliShaderRegistry.getPostProcessor(fragmentId.getPath());
    }

    public LazuliFramebufferShader setBlendMode(LazuliBlendMode mode){
        this.blendMode = mode;
        return this;
    }

    public Uniform getUniformByNameOrDummy(String name){
        return getProgram().getProgram().getUniformByNameOrDummy(name);
    }

    public void renderToFramebuffer(float tickDelta, Framebuffer inBuffer, Framebuffer outBuffer) {
        getProgram().render(tickDelta, inBuffer, outBuffer);
    }

    public void renderToScreen(float tickDelta) {
        Framebuffer main = MinecraftClient.getInstance().getFramebuffer();
        LazuliFramebufferUtills.copyToSwap(main);
        getProgram().render(tickDelta, LazuliFramebufferUtills.getSwapBuffer(), main);
    }

    public LazuliFramebufferShader setSampler(String sampler, Identifier texture) {
        LapisRenderer.setShaderTexture(samplers.indexOf(sampler), texture);
        return this;
    }


    @Override
    public JsonObject toJson() {
        JsonObject shaderJson = super.toJson();

        JsonObject blendModeJson = new JsonObject();
        blendModeJson.addProperty("func", blendMode.equation());
        blendModeJson.addProperty("srcrgb", blendMode.src());
        blendModeJson.addProperty("dstrgb", blendMode.dst());

        shaderJson.add("blend", blendModeJson);

        return shaderJson;
    }
}