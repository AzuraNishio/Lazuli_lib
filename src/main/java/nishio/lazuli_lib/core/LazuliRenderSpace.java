package nishio.lazuli_lib.core;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import java.awt.image.renderable.RenderContext;
import java.util.List;

public class LazuliRenderSpace {
    public List<LazuliVertex> vertices;
    public LazuliBufferBuilder bb;

    public LazuliRenderSpace(LazuliRenderContext context){
        bb = context.getLazuliBB(VertexFormat.DrawMode.QUADS);
    }











}
