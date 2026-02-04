package nishio.lazuli_lib.internals;

import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.function.Consumer;

public class LazuliResourcePackProvider implements ResourcePackProvider {
    
    private final Path lazuliGenPath;
    
    public LazuliResourcePackProvider(Path lazuliGenPath) {
        this.lazuliGenPath = lazuliGenPath;
    }
    
    public static ResourcePackProvider create(Path path) {
        return new LazuliResourcePackProvider(path);
    }
    
    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        // Create ResourcePackInfo
        ResourcePackInfo info = new ResourcePackInfo(
                "lazuli_generated",  // id
                Text.literal("Lazuli resource packs!"),  // title
                ResourcePackSource.BUILTIN,  // source
                java.util.Optional.empty()  // overlay parent
        );

        // Create PackFactory
        ResourcePackProfile.PackFactory factory = new ResourcePackProfile.PackFactory() {
            @Override
            public ResourcePack open(ResourcePackInfo info) {
                return new DirectoryResourcePack(info, lazuliGenPath);
            }
            
            @Override
            public ResourcePack openWithOverlays(ResourcePackInfo info, ResourcePackProfile.Metadata metadata) {
                return new DirectoryResourcePack(info, lazuliGenPath);
            }
        };

        // Create position (always enabled, at top priority)
        ResourcePackPosition position = new ResourcePackPosition(
                true,  // required
                ResourcePackProfile.InsertionPosition.TOP,  // default position
                true   // fixed position
        );
        
        // Create and register the profile
        ResourcePackProfile profile = ResourcePackProfile.create(
                info,
                factory,
                ResourceType.CLIENT_RESOURCES,
                position
        );
        
        if (profile != null) {
            profileAdder.accept(profile);
            Lazuli_Lib_Client.LOGGER.info("Lazuli resources registered!");
        }
    }
}