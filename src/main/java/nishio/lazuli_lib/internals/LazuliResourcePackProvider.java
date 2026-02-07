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
        // Create and register the profile
        ResourcePackProfile profile = ResourcePackProfile.create(
                "lazuli_generated",  // name
                Text.literal("Lazuli resource packs!"),  // display name
                true,  // always enabled
                (name) -> new DirectoryResourcePack(name, lazuliGenPath, false),  // pack factory
                ResourceType.CLIENT_RESOURCES,  // type
                ResourcePackProfile.InsertionPosition.TOP,  // position
                ResourcePackSource.BUILTIN  // source
        );

        if (profile != null) {
            profileAdder.accept(profile);
            Lazuli_Lib_Client.LOGGER.info("Lazuli resources registered!");
        }
    }
}