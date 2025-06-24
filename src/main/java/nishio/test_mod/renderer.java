package nishio.test_mod;
/** Renderer utility for the test mod. */

import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.*;

import java.util.concurrent.atomic.AtomicReference;

public class renderer {
    public static void register(){
        AtomicReference<Float> time = new AtomicReference<>((float) 0);
        LazuliRenderingRegistry.registerRenderCallback((context, viewProjMatrix, tickDelta) -> {
            time.updateAndGet(v -> new Float((float) (v + tickDelta)));
            float t = time.get();

            LazuliBufferBuilder bb = new LazuliBufferBuilder(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, viewProjMatrix, context.camera());
            LapisRenderer.enableCull();
            LapisRenderer.enableDepthTest();
            LapisRenderer.setShader(LazuliShaderRegistry.getShader(ModShaders.RENDER_TYPE_ATMOSPHERE));

//            LazuliVertex v1 = new LazuliVertex().pos(-5, 100, 300).color(1f,1f,0f,1f);
//            LazuliVertex v2 = new LazuliVertex().pos(-5, 100, 290).color(1f,1f,0f,1f);
//            LazuliVertex v3 = new LazuliVertex().pos(5, 100, 290).color(1f,1f,0f,1f);
//            LazuliVertex v4 = new LazuliVertex().pos(5, 100, 300).color(1f,1f,0f,1f);
//
//            bb.addVertex(v1).addVertex(v2).addVertex(v3).addVertex(v4).drawAndReset();

            LazuliGeometryBuilder.buildTexturedSphereRotatedNormal(32, t, new Vec3d(0, 100, 340), new Vec3d(0, 1.0, 0), 0, true, 0, bb);
            LazuliGeometryBuilder.buildTorus(32,16, 3f,0.8f, new Vec3d(0,110,340), new Vec3d(0,1,0), 0, false, bb);
            LazuliGeometryBuilder.buildBox(
                    2f, 1f, 1f,
                    new Vec3d(20, 100, 340),         // centre
                    new Vec3d(0, 1, 0).rotateZ(0.34906585F), // axle (Y rotated 20°)
                    0,                             // no extra roll
                    false,
                    bb);

            bb.draw();

        });
    }
}
