package nishio.lazuli_lib.core.registry;
/* Handles registration of shaders and post processors. */

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.internals.*;
import nishio.lazuli_lib.internals.datagen.LazuliShaderDatagenManager;
import nishio.lazuli_lib.internals.datagen.LazuliWarpManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LazuliShaderRegistry {

    private static final Map<String, ShaderProgram> SHADER_MAP = new HashMap<>();
    private static final Map<String, LazuliTrueFramebufferShader> POST_PROCESSOR_MAP = new HashMap<>();

    private static int resX ;
    private static int resY;


    public static void registerShader(String name, String nameSpace, VertexFormat format) {
        Identifier shaderId = Identifier.of(nameSpace, name);
        boolean dataGenerated = false;

        CoreShaderRegistrationCallback.EVENT.register(ctx -> {
            ctx.register(shaderId, format, shaderProgram -> {
                SHADER_MAP.put(name, shaderProgram);
                LazuliLog.Shaders.info("Shader '{}' registered!", name);
            });
        });
    }

    public static void registerShader(Identifier jsonPath, VertexFormat format) {
        boolean dataGenerated = false;

        CoreShaderRegistrationCallback.EVENT.register(ctx -> {
            ctx.register(jsonPath, format, shaderProgram -> {
                SHADER_MAP.put(jsonPath.getPath(), shaderProgram);
                LazuliLog.Shaders.info("Shader '{}' registered!", jsonPath.getPath());
            });
        });
    }

    public static void registerShader(LazuliShaderTop shader) {
        LazuliShaderDatagenManager.registerShader(shader);
    }

    public static void close(){
        LazuliWarpManager.generate();
        LazuliShaderDatagenManager.gen();
    }

    /**
     * Registers a post-processing shader using the LazuliPostProcessingRegistry.
     * This will defer shader creation until loadPrograms is called.
     */
    public static void registerPostProcessingShader(String name, String nameSpace) {

        LazuliLog.Shaders.info("Trying to register {}", name);
        LazuliPostProcessingRegistry.register((client, factory) -> {
            Identifier shaderId = Identifier.of(nameSpace, name);
            Framebuffer framebuffer = client.getFramebuffer();

            try {
                LazuliTrueFramebufferShader processor = new LazuliTrueFramebufferShader(MinecraftClient.getInstance().getResourceManager(), shaderId);


                POST_PROCESSOR_MAP.put(name, processor);
                LazuliLog.Shaders.info("Post-processing shader '{}' registered in callback.", name);

            } catch (IOException e) {
                LazuliLog.Shaders.error("Failed to load post-processing shader: {} <============================================================================================", name);
                LazuliLog.Shaders.error("================================================[stack trace]================================================");
                e.printStackTrace();
                LazuliLog.Shaders.error("================================================[  closing  ]================================================");
            }
        });
    }

    public static void register(){
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            //close();
        });

        ClientTickEvents.START_CLIENT_TICK.register((t) ->{

            Window window = MinecraftClient.getInstance().getWindow();

            if (resX != window.getFramebufferWidth() || resY != window.getFramebufferHeight()) {
                windowResized(window.getFramebufferHeight(), window.getFramebufferWidth());
            }
            resX = window.getFramebufferWidth();
            resY = window.getFramebufferHeight();

        });

    }

    private static void windowResized(int height, int width) {
        for (Map.Entry<String, LazuliTrueFramebufferShader> entry : POST_PROCESSOR_MAP.entrySet()) {
            LazuliTrueFramebufferShader processor = entry.getValue();
            if (processor != null) {

            }
        }
    }



    public static ShaderProgram getShaderFromName(String name) {
        return SHADER_MAP.get(name);
    }

    public static LazuliTrueFramebufferShader getPostProcessor(String name) {
        return POST_PROCESSOR_MAP.get(name);
    }
}
