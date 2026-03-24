package nishio.lazuli_lib.internals;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class LazuliTrueFramebufferShader implements AutoCloseable {
    public final ResourceFactory resourceFactory;
    public final String name;

    public final Identifier id;
    public float time;
    public float lastTickDelta;
    private JsonEffectShaderProgram program;
    private Matrix4f projectionMatrix;
    private final int texFilter;


    public LazuliTrueFramebufferShader(ResourceManager resourceManager, Identifier id) throws IOException, JsonSyntaxException {
        this.resourceFactory = resourceManager;
        this.time = 0.0F;
        this.lastTickDelta = 0.0F;
        this.name = id.toString();
        this.id = id;
        this.program = new JsonEffectShaderProgram(resourceManager, id.getPath());
        this.texFilter = 9728;

    }

    public void reload(ResourceManager resourceManager){
        try {
            this.program = new JsonEffectShaderProgram(resourceManager, this.id.getPath());
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    public void render(float tickDelta, Framebuffer outBuffer) {
        this.render(tickDelta, outBuffer, outBuffer, new Vec2f(0,0), new Vec2f(1, 0), new Vec2f(1, 1), new Vec2f(0, 1), false, false);
    }

    public void render(float tickDelta, Framebuffer inBuffer, Framebuffer outBuffer) {
        this.render(tickDelta, inBuffer, outBuffer, new Vec2f(0,0), new Vec2f(1, 0), new Vec2f(1, 1), new Vec2f(0, 1), true, false);
    }

    public void render(float tickDelta, Framebuffer inBuffer, Framebuffer outBuffer, boolean viewport) {
        this.render(tickDelta, inBuffer, outBuffer, new Vec2f(0,0), new Vec2f(1, 0), new Vec2f(1, 1), new Vec2f(0, 1), true, viewport);
    }

    public void render(float tickDelta, Framebuffer outBuffer,  Vec2f A, Vec2f B, Vec2f C, Vec2f D) {
        this.render(tickDelta, outBuffer, outBuffer, A, B, C, D, false, false);
    }

    public void render(float tickDelta, Framebuffer inBuffer, Framebuffer outBuffer,  Vec2f A, Vec2f B, Vec2f C, Vec2f D) {
        this.render(tickDelta, inBuffer, outBuffer, A, B, C, D, true, false);
    }

    public void render(float tickDelta, Framebuffer inBuffer, Framebuffer outBuffer,  Vec2f A, Vec2f B, Vec2f C, Vec2f D, boolean useDefaultInput, boolean viewport) {
        this.time += tickDelta;
        if(this.time > 20.0F) {
            this.time -= 20.0F;
        }

        int i = 9728;

        int j = this.getTexFilter();
        inBuffer.setTexFilter(j);
        outBuffer.setTexFilter(j);

        this.projectionMatrix = (new Matrix4f()).setOrtho(0.0F, (float)outBuffer.textureWidth, 0.0F, (float)outBuffer.textureHeight, 0.1F, 1000.0F);
        apply(inBuffer, outBuffer, A, B, C, D, false, useDefaultInput, viewport);


        inBuffer.setTexFilter(9728);
        outBuffer.setTexFilter(9728);
    }


    public void close() {
        this.program.close();
    }

    public void apply(Framebuffer in, Framebuffer out, Vec2f A, Vec2f B, Vec2f C, Vec2f D, boolean clear, boolean useIn, boolean viewport){
        float W = (float)out.textureWidth;
        float H = (float)out.textureHeight;
        RenderSystem.viewport(0, 0, (int)W, (int)H);

        JsonEffectShaderProgram shaderProgram = this.program;

        Objects.requireNonNull(in);

        if (useIn) {
            in.endWrite();
            shaderProgram.bindSampler("InputColor", in::getColorAttachment);
            shaderProgram.bindSampler("InputDepth", in::getDepthAttachment);
        }

        this.program.getUniformByNameOrDummy("ProjMat").set(this.projectionMatrix);
        this.program.getUniformByNameOrDummy("InSize").set((float)in.textureWidth, (float)in.textureHeight);
        this.program.getUniformByNameOrDummy("OutSize").set(W, H);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        this.program.getUniformByNameOrDummy("ScreenSize").set((float)minecraftClient.getWindow().getFramebufferWidth(), (float)minecraftClient.getWindow().getFramebufferHeight());

        this.program.enable();
        if (clear) {
            out.clear(MinecraftClient.IS_SYSTEM_MAC);
        }
        RenderSystem.depthFunc(519);
        RenderSystem.disableDepthTest();
        out.beginWrite(false);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(A.x * W, A.y * H, 500.0F).next();
        bufferBuilder.vertex(B.x * W, B.y * H, 500.0F).next();
        bufferBuilder.vertex(C.x * W, C.y * H, 500.0F).next();
        bufferBuilder.vertex(D.x * W, C.y * H, 500.0F).next();
        BufferRenderer.draw(bufferBuilder.end());
        RenderSystem.depthFunc(515);
        this.program.disable();

        if (useIn) {
            in.endRead();
        }
    }


    public JsonEffectShaderProgram getProgram() {
        return this.program;
    }

    public int getTexFilter() {
        return this.texFilter;
    }
}
