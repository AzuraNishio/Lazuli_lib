package nishio.test_mod;
/** Client entry point for the test mod. */

import net.fabricmc.api.ClientModInitializer;
import nishio.lazuli_lib.core.LazuliRenderingRegistry;
import nishio.lazuli_lib.core.LazuliShaderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class test_mod_client implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Really Cool Mod");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Really cool mod loading");
		renderer.register();
	}
}