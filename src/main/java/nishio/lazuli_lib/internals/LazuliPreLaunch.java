package nishio.lazuli_lib.internals;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazuliPreLaunch implements PreLaunchEntrypoint {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("LazuliPreLaunch");
    
    @Override
    public void onPreLaunch() {
        LOGGER.info("========================================");
        LOGGER.info("LazuliLib PreLaunch - Running BEFORE everything!");
        LOGGER.info("========================================");
        
        // Generate shader files here
        LazuliShaderDatagenManager.gen();
        
        LOGGER.info("PreLaunch complete!");
    }
}