package nishio.lazuli_lib.core.framebuffers;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class LazuliFramebufferShader implements AutoCloseable {
    public final ResourceFactory resourceFactory;
    public final String name;
    public float time;
    public float lastTickDelta;
    private final JsonEffectShaderProgram program;
    private Matrix4f projectionMatrix;
    private final int texFilter;


    public LazuliFramebufferShader(ResourceFactory resourceFactory, Identifier id) throws IOException, JsonSyntaxException {
        this.resourceFactory = resourceFactory;
        this.time = 0.0F;
        this.lastTickDelta = 0.0F;
        this.name = id.toString();

        this.program = new JsonEffectShaderProgram(resourceFactory, id.getPath());
        this.texFilter = 9728;

    }


    public void render(float tickDelta, Framebuffer inBuffer, Framebuffer outBuffer) {

        this.time += tickDelta;
        if(this.time > 20.0F) {
            this.time -= 20.0F;
        }

        int i = 9728;

        int j = this.getTexFilter();
        inBuffer.setTexFilter(j);
        outBuffer.setTexFilter(j);
        i = j;

        this.projectionMatrix = (new Matrix4f()).setOrtho(0.0F, (float)outBuffer.textureWidth, 0.0F, (float)outBuffer.textureHeight, 0.1F, 1000.0F);
        test(inBuffer, outBuffer);


        inBuffer.setTexFilter(9728);
        outBuffer.setTexFilter(9728);
    }

    public void close() {
        this.program.close();
    }

    public void test(Framebuffer in, Framebuffer out){
        in.endWrite();
        float f = (float)out.textureWidth;
        float g = (float)out.textureHeight;
        RenderSystem.viewport(0, 0, (int)f, (int)g);

        JsonEffectShaderProgram shaderProgram = this.program;

        Objects.requireNonNull(in);


        shaderProgram.bindSampler("DiffuseSampler", in::getColorAttachment);
        shaderProgram.bindSampler("DepthSampler", in::getDepthAttachment);

        this.program.getUniformByNameOrDummy("ProjMat").set(this.projectionMatrix);
        this.program.getUniformByNameOrDummy("InSize").set((float)in.textureWidth, (float)in.textureHeight);
        this.program.getUniformByNameOrDummy("OutSize").set(f, g);
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        this.program.getUniformByNameOrDummy("ScreenSize").set((float)minecraftClient.getWindow().getFramebufferWidth(), (float)minecraftClient.getWindow().getFramebufferHeight());

        if (minecraftClient.player != null) {
            if(minecraftClient.player.getPos().getY() < 100.1) {
                this.program.getUniformByNameOrDummy("Pos").set((float) minecraftClient.player.getPos().x / 20f, (float) minecraftClient.player.getPos().z / 20f);
            } else {
                this.program.getUniformByNameOrDummy("Pos").set((float) 20f, (float) minecraftClient.player.getPos().z / 20f);

            }
        }
        this.program.enable();
        out.clear(MinecraftClient.IS_SYSTEM_MAC);

        RenderSystem.depthFunc(519);
        RenderSystem.disableDepthTest();
        out.beginWrite(false);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(0.0F, 0.0F, 500.0F);
        bufferBuilder.vertex(f, 0.0F, 500.0F);
        bufferBuilder.vertex(f, g, 500.0F);
        bufferBuilder.vertex(0.0F, g, 500.0F);
        BufferRenderer.draw(bufferBuilder.end());
        RenderSystem.depthFunc(515);
        this.program.disable();
        in.endRead();
    }


    public JsonEffectShaderProgram getProgram() {
        return this.program;
    }

    public int getTexFilter() {
        return this.texFilter;
    }
}
