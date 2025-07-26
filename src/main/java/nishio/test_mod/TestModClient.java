package nishio.test_mod;
/** Client entry point for the test mod. */

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TestModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Really Cool Mod");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Really cool mod loading");
        TestModShaders.registerShaders();

    }
}