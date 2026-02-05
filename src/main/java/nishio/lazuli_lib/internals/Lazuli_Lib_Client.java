package nishio.lazuli_lib.internals;
/** Client side initialization of Lazuli library. */

import net.fabricmc.api.ClientModInitializer;
import nishio.lazuli_lib.core.events.LazuliRenderEvents;
import nishio.lazuli_lib.core.framebuffers.LazuliFramebufferUtills;
import nishio.lazuli_lib.core.registry.LazuliShaderRegistry;
import nishio.lazuli_lib.core.miscellaneous.LazuliClock;
import nishio.lazuli_lib.internals.datagen.LazuliShaderDatagenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lazuli_Lib_Client implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(Lazuli_Lib.MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("""
				
				=====[ Proceed with caution!: Risk of pwetty sparks and "OHHHHHHH's" ]=====
				Azura hopes u have a lot of fun Nyaaaaaa~~
				<3
				===========================================================================
			""");

		//Calling all parts of the lib:
		LazuliRenderEvents.registerLazuliRenderPhases();
		LazuliShaderRegistry.register();
		LazuliClock.register();
		LazuliShaderDatagenManager.initialize();
		LazuliFramebufferUtills.register();
	}


}