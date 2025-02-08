package lazuli_lib.lazuli.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import lazuli_lib.lazuli.Lazuli;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;


//Work in progress, will be implemented later
//  (\_/)
//  ( ╥‸╥)
//   | |


public class LazuliHudRenderStep {

    private static float totalTickDelta = 0.0f; // For animation over time
    private static final Identifier CUSTOM_TEXTURE = new Identifier("lazuli_lib", "textures/gui/my_image.png");
    public static void register() {
        // Hook into the AFTER_ENTITIES render event
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            // Prepare transformation matrix
            Matrix4f transformationMatrix = new Matrix4f();
            transformationMatrix.loadIdentity();

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            // Enable the shader and set initial color
            //RenderSystem.setShader(GameRenderer::getPositionColorShader);

            // Bind the custom texture
            RenderSystem.setShaderTexture(0, CUSTOM_TEXTURE);


            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            // First Quad
            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
            buffer.vertex(transformationMatrix, 20, 20, 0).color(65, 65, 65, 255).next();
            buffer.vertex(transformationMatrix, 5, 40, 0).color(0, 0, 0, 255).next();
            buffer.vertex(transformationMatrix, 35, 40, 0).color(0, 0, 0, 255).next();
            buffer.vertex(transformationMatrix, 20, 60, 0).color(65, 65, 65, 255).next();
            tessellator.draw(); // Draw the first quad

            // Second Quad
            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
            buffer.vertex(transformationMatrix, 50, 50, 0).color(255, 0, 0, 255).next();
            buffer.vertex(transformationMatrix, 70, 50, 0).color(255, 0, 0, 255).next();
            buffer.vertex(transformationMatrix, 50, 70, 0).color(255, 0, 0, 255).next();
            buffer.vertex(transformationMatrix, 70, 70, 0).color(255, 0, 0, 255).next();
            tessellator.draw(); // Draw the second quad
        });

        Lazuli.LOGGER.info("Custom HUD rendering registered.");
    }
}
