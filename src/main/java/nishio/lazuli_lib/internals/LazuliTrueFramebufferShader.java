package nishio.lazuli_lib.internals;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.image.renderable.RenderContext;
import java.io.IOException;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlProgramManager;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

@Environment(EnvType.CLIENT)
public class LazuliTrueFramebufferShader implements AutoCloseable {
    public final ResourceFactory resourceFactory;
    public final String name;
    public float time;
    public float lastTickDelta;
    private final JsonEffectShaderProgram program;
    private Matrix4f projectionMatrix;
    private final int texFilter;


    public LazuliTrueFramebufferShader(ResourceManager resourceManager, Identifier id) throws IOException, JsonSyntaxException {
        this.resourceFactory = resourceManager;
        this.time = 0.0F;
        this.lastTickDelta = 0.0F;
        this.name = id.toString();
        this.program = new JsonEffectShaderProgram(resourceManager, id.getPath());
        this.texFilter = 9728;

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
        for (GlUniform glUniform : program.uniformData) {
            glUniform.close();
        }

        int progRef = program.getGlRef();

        RenderSystem.assertOnRenderThread();

        //Idk why it need to be twice but it works!
        program.getFragmentShader().release();
        program.getVertexShader()  .release();
        program.getFragmentShader().release();
        program.getVertexShader()  .release();


        GL20.glDeleteProgram(program.getGlRef());
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

        float fov = (float) Math.toRadians(MinecraftClient.getInstance().options.getFov().getValue());
        this.program.getUniformByNameOrDummy("ProjMat").set(this.projectionMatrix);
        this.program.getUniformByNameOrDummy("FOV").set(fov);
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
        out.beginWrite(viewport);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(A.x * W, A.y * H, 500.0F);
        bufferBuilder.vertex(B.x * W, B.y * H, 500.0F);
        bufferBuilder.vertex(C.x * W, C.y * H, 500.0F);
        bufferBuilder.vertex(D.x * W, C.y * H, 500.0F);
        BufferRenderer.draw(bufferBuilder.end());
        RenderSystem.depthFunc(515);
        in.endRead();
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
