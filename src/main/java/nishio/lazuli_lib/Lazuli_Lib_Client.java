package nishio.lazuli_lib;
/** Client side initialization of Lazuli library. */

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.LazuliRenderingRegistry;
import nishio.lazuli_lib.core.LazuliShaderRegistry;
import nishio.lazuli_lib.core.LazuliClock;
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
				===========================================================================""");

		//Calling all parts of the lib:
                LazuliRenderingRegistry.registerLazuliRenderPhases();
                LazuliShaderRegistry.register();
                LazuliClock.register();
        }
}