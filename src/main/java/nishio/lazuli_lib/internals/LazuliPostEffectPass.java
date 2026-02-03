//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nishio.lazuli_lib.internals;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.world_rendering.LapisRenderer;
import nishio.test_mod.TestModShaders;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class LazuliPostEffectPass implements AutoCloseable {
    private final JsonEffectShaderProgram program;
    public final Framebuffer input;
    public final Framebuffer output;
    private final List<IntSupplier> samplerValues = Lists.newArrayList();
    private final List<String> samplerNames = Lists.newArrayList();
    private final List<Integer> samplerWidths = Lists.newArrayList();
    private final List<Integer> samplerHeights = Lists.newArrayList();
    private Matrix4f projectionMatrix;
    private final int texFilter;
    public Framebuffer testOut; //= new SimpleFramebuffer(128, 128, false, false);
    public Framebuffer testLast;// = new SimpleFramebuffer(128, 128, false, false);
    public boolean beggining = false;
    public int count = 0;

    public LazuliPostEffectPass(ResourceFactory resourceFactory, String programName, Framebuffer input, Framebuffer output, boolean linear) throws IOException {
        this.program = new JsonEffectShaderProgram(resourceFactory, programName);
        this.input = input;
        this.output = output;
        this.texFilter = linear ? 9729 : 9728;

        testOut = new SimpleFramebuffer(input.textureWidth, input.textureHeight, true, false);
        testLast = new SimpleFramebuffer(input.textureWidth, input.textureHeight, true, false);


    }

    public void resizeStuff(int w, int h){
        testOut = new SimpleFramebuffer(w, h, true, false);
        testLast = new SimpleFramebuffer(w, h, true, false);
    }

    public void close() {
        this.program.close();
    }

    public final String getName() {
        return this.program.getName();
    }

    public void addAuxTarget(String name, IntSupplier valueSupplier, int width, int height) {
        this.samplerNames.add(this.samplerNames.size(), name);
        this.samplerValues.add(this.samplerValues.size(), valueSupplier);
        this.samplerWidths.add(this.samplerWidths.size(), width);
        this.samplerHeights.add(this.samplerHeights.size(), height);
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public void render(float time) {

        test(testLast, testOut);
        test(testOut, testLast);
        //test(testOut, testLast);
    }

    public void test(Framebuffer a, Framebuffer b){

        a.endWrite();
        float f = (float)b.textureWidth;
        float g = (float)b.textureHeight;
        RenderSystem.viewport(0, 0, (int)f, (int)g);

        JsonEffectShaderProgram shaderProgram = this.program;

        Objects.requireNonNull(a);


        shaderProgram.bindSampler("DiffuseSampler", a::getColorAttachment);
        shaderProgram.bindSampler("DepthSampler", a::getDepthAttachment);

        for(int i = 0; i < this.samplerValues.size(); ++i) {
            this.program.bindSampler((String)this.samplerNames.get(i), (IntSupplier)this.samplerValues.get(i));
            this.program.getUniformByNameOrDummy("AuxSize" + i).set((float)(Integer)this.samplerWidths.get(i), (float)(Integer)this.samplerHeights.get(i));
        }

        if (beggining) {
            beggining = false;
            this.program.getUniformByNameOrDummy("state").set(1f, 0f, 0f);
        } else {
            this.program.getUniformByNameOrDummy("state").set(0f, 0f, 0f);
        }

        this.program.getUniformByNameOrDummy("ProjMat").set(this.projectionMatrix);
        this.program.getUniformByNameOrDummy("InSize").set((float)a.textureWidth, (float)a.textureHeight);
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
        //b.clear(MinecraftClient.IS_SYSTEM_MAC);

        RenderSystem.depthFunc(519);
        RenderSystem.disableDepthTest();
        b.beginWrite(false);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(0.0F, 0.0F, 500.0F);
        bufferBuilder.vertex(f, 0.0F, 500.0F);
        bufferBuilder.vertex(f, g, 500.0F);
        bufferBuilder.vertex(0.0F, g, 500.0F);
        BufferRenderer.draw(bufferBuilder.end());
        RenderSystem.depthFunc(515);
        this.program.disable();
        a.endRead();



        for(Object object : this.samplerValues) {
            if (object instanceof Framebuffer) {
                ((Framebuffer)object).endRead();
            }
        }

    }


    public JsonEffectShaderProgram getProgram() {
        return this.program;
    }

    public int getTexFilter() {
        return this.texFilter;
    }
}
