package nishio.test_mod;

import net.minecraft.util.Identifier;
import nishio.lazuli_lib.core.events.LazuliRenderEvents;
import nishio.lazuli_lib.core.shaders.LazuliFramebufferShader;

public class TestModShaders2 {
    public static LazuliFramebufferShader OUTLINE;
    public static void register(){
        OUTLINE = new LazuliFramebufferShader(Identifier.of("test_mod", "outline_demo")).addDefaultUniforms().register();

        LazuliRenderEvents.registerPostCallback((a, b, c) ->{
            OUTLINE.renderToScreen();
        });
    }
}
