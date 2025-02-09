package lazuli_lib.lazuli.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

public class LazuliHudRenderStep {

    public static float FlashForce = 0;
    private static float totalTickDelta = 0.0f; // For animation over time
    public static void register() {
        // Hook into the AFTER_ENTITIES render event
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            MinecraftClient client = MinecraftClient.getInstance();


                // Prepare transformation matrix
                Matrix4f transformationMatrix = new Matrix4f();
                transformationMatrix.loadIdentity();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();

                // Enable the shader and set initial color
                RenderSystem.setShader(GameRenderer::getPositionColorShader);

            int[] color = LazuliHudRenderManager.getScreenTintOverlay();
            if (color.length != 4) {
                throw new IllegalArgumentException("Color array must have 4 elements (RGBA).");
            }

            float r = color[0] / 255f;
            float g = color[1] / 255f;
            float b = color[2] / 255f;
            float a = color[3] / 255f;

            RenderSystem.setShaderColor(r, g, b, a);


                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                int xSize = 2000;
                int ySize = 2000;

                //draw overlay
                buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
                buffer.vertex(transformationMatrix, xSize, ySize, 0).color(255, 255, 255, 255).next();
                buffer.vertex(transformationMatrix, 0, 0, 0).color(255, 255, 255, 255).next();
                buffer.vertex(transformationMatrix, xSize, 0, 0).color(255, 255, 255, 255).next();
                buffer.vertex(transformationMatrix, 0, ySize, 0).color(255, 255, 255, 255).next();
                tessellator.draw(); // Draw the first quad



                RenderSystem.disableBlend();
        });
    }
}
