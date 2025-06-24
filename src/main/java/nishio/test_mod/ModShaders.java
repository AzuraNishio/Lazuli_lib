package nishio.test_mod;

import net.minecraft.client.render.VertexFormats;
import nishio.lazuli_lib.Lazuli_Lib;
import nishio.lazuli_lib.core.LazuliShaderRegistry;

import java.io.IOException;

public class ModShaders {
    public static String RENDER_TYPE_ATMOSPHERE = "rendertype_test_atmosphere";

    public static final String ADJACENT_DIFFERENCE = "shaders/post/adjacent_difference_post.json";

    public static void registerShaders() throws IOException {

        LazuliShaderRegistry.registerShader(RENDER_TYPE_ATMOSPHERE, Lazuli_Lib.MOD_ID, VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
        //LazuliShaderRegistry.registerPostProcessingShader(ADJACENT_DIFFERENCE);
    }

}