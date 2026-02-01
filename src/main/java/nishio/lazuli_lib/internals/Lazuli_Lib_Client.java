package nishio.lazuli_lib.internals;
/** Client side initialization of Lazuli library. */

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.core.LazuliRenderingRegistry;
import nishio.lazuli_lib.core.LazuliShaderRegistry;
import nishio.lazuli_lib.core.LazuliClock;
import nishio.test_mod.TestModClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

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
		LazuliRenderingRegistry.registerLazuliRenderPhases();
		LazuliShaderRegistry.register();
		LazuliClock.register();

	}


}