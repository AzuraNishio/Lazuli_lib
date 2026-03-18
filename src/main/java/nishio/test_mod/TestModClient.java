package nishio.test_mod;
/** Client entry point for the test mod. */

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nishio.lazuli_lib.core.tools.LazuliShaderDevTools;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Really Cool Mod");
	public static final String MOD_ID = "test_mod";

	public static final KeyBinding TEST = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.meoow-inccident.test",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_Y,
			"key.yeh2"
	));

	@Override
	public void onInitializeClient() {
		LOGGER.info("Really cool mod loading");
		TestRenderer2.register();
        TestModShaders.registerShaders();

		LazuliShaderDevTools.enableFastShaderReloading();
    }
}