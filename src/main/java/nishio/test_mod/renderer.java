package nishio.test_mod;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.*;

public class renderer {
    public static void register(){
        LazuliRenderingRegistry.registerRenderCallback((context, viewProjMatrix, tickDelta) -> {

            LazuliBufferBuilder bb = new LazuliBufferBuilder(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR, viewProjMatrix, context.camera());
            LapisRenderer.disableCull();
            LapisRenderer.setShader(GameRenderer::getPositionColorProgram);

            LazuliVertex v1 = new LazuliVertex().pos(-5, 100, 300).color(1f,1f,0f,1f);
            LazuliVertex v2 = new LazuliVertex().pos(-5, 100, 290).color(1f,1f,0f,1f);
            LazuliVertex v3 = new LazuliVertex().pos(5, 100, 290).color(1f,1f,0f,1f);
            LazuliVertex v4 = new LazuliVertex().pos(5, 100, 300).color(1f,1f,0f,1f);

            bb.addVertex(v1).addVertex(v2).addVertex(v3).addVertex(v4).drawAndReset();
            LazuliGeometryBuilder.buildTexturedSphereRotatedNormal(32, 20f, new Vec3d(0, 100, 340), new Vec3d(0, 1.0, 0), 0, false, 0, bb);
            bb.draw();

        });
    }
}
