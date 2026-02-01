package nishio.lazuli_lib.core;

import net.minecraft.client.render.VertexFormat;

import java.util.List;

public class LazuliRenderSpace {
    public List<LazuliVertex> vertices;
    public LazuliBufferBuilder bb;

    public LazuliRenderSpace(LazuliRenderContext context){
        bb = context.getLazuliBB(VertexFormat.DrawMode.QUADS);
    }











}
