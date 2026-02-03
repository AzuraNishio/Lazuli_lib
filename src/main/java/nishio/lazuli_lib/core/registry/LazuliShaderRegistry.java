package nishio.lazuli_lib.core.registry;
/** Handles registration of shaders and post processors. */

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.framebuffers.LazuliFramebufferShader;
import nishio.lazuli_lib.core.shaders.LazuliShader;
import nishio.lazuli_lib.internals.LazuliPostProcessingRegistry;
import nishio.lazuli_lib.internals.LazuliShaderDatagenManager;
import nishio.lazuli_lib.internals.Lazuli_Lib_Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LazuliShaderRegistry {

    private static final Map<String, ShaderProgram> SHADER_MAP = new HashMap<>();
    private static final Map<String, LazuliFramebufferShader> POST_PROCESSOR_MAP = new HashMap<>();

    private static int resX ;
    private static int resY;

    private static Set<String> namespaces = new HashSet<>();

    private static boolean hasReloaded = false;


    public static void registerShader(String name, String nameSpace, VertexFormat format) {
        Identifier shaderId = Identifier.of(nameSpace, name);
        boolean dataGenerated = false;
        namespaces.add(nameSpace);

        CoreShaderRegistrationCallback.EVENT.register(ctx -> {
            ctx.register(shaderId, format, shaderProgram -> {
                SHADER_MAP.put(name, shaderProgram);
                Lazuli_Lib_Client.LOGGER.info("Shader '{}' registered!", name);
            });
        });
    }

    public static void registerShader(Identifier jsonPath, VertexFormat format) {
        boolean dataGenerated = false;
        namespaces.add(jsonPath.getNamespace());

        CoreShaderRegistrationCallback.EVENT.register(ctx -> {
            ctx.register(jsonPath, format, shaderProgram -> {
                SHADER_MAP.put(jsonPath.getPath(), shaderProgram);
                Lazuli_Lib_Client.LOGGER.info("Shader '{}' registered!", jsonPath.getPath());
            });
        });
    }

    public static void registerShader(LazuliShader shader) {
        LazuliShaderDatagenManager.registerShader(shader);
        namespaces.add(shader.jsonId.getNamespace());
    }

    public static void close(){
        for(String n : namespaces){
            LazuliShaderDatagenManager.genNamespace(n);
        }
        namespaces.clear();
    }

    /**
     * Registers a post-processing shader using the LazuliPostProcessingRegistry.
     * This will defer shader creation until loadPrograms is called.
     */
    public static void registerPostProcessingShader(String name, String nameSpace) {

        Lazuli_Lib_Client.LOGGER.info("Trying to register {}", name);
        LazuliPostProcessingRegistry.register((client, factory) -> {
            Identifier shaderId = Identifier.of(nameSpace, name);
            Framebuffer framebuffer = client.getFramebuffer();

            try {
                LazuliFramebufferShader processor = new LazuliFramebufferShader(factory, shaderId);


                POST_PROCESSOR_MAP.put(name, processor);
                Lazuli_Lib_Client.LOGGER.info("Post-processing shader '{}' registered in callback.", name);

            } catch (IOException e) {
                Lazuli_Lib_Client.LOGGER.error("Failed to load post-processing shader: {} <============================================================================================", name);
                Lazuli_Lib_Client.LOGGER.error("================================================[stack trace]================================================");
                e.printStackTrace();
                Lazuli_Lib_Client.LOGGER.error("================================================[  closing  ]================================================");
            }
        });
    }

    public static void register(){



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
        for (Map.Entry<String, LazuliFramebufferShader> entry : POST_PROCESSOR_MAP.entrySet()) {
            LazuliFramebufferShader processor = entry.getValue();
            if (processor != null) {

            }
        }
    }



    public static ShaderProgram getShaderFromName(String name) {
        return SHADER_MAP.get(name);
    }

    public static LazuliFramebufferShader getPostProcessor(String name) {
        return POST_PROCESSOR_MAP.get(name);
    }
}
