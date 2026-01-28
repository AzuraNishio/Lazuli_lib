package nishio.test_mod;

import net.minecraft.client.render.VertexFormats;
import nishio.lazuli_lib.core.LazuliShaderRegistry;
import nishio.lazuli_lib.internals.Lazuli_Lib;

public class TestModShaders {
    public static String RENDER_TYPE_ATMOSPHERE = "rendertype_test_atmosphere";

    public static final String CONTRAST = "shaders/post/contrast.json";
    public static final String EASY_CONTRAST = "shaders/program/contrast.json";


    public static void registerShaders() {

        LazuliShaderRegistry.registerShader(RENDER_TYPE_ATMOSPHERE, Lazuli_Lib.MOD_ID, VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
        LazuliShaderRegistry.registerPostProcessingShader(CONTRAST, Lazuli_Lib.MOD_ID);
    }

}