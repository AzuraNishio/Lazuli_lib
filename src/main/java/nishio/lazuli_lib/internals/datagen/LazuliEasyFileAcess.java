package nishio.lazuli_lib.internals.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Optional;

import static net.minecraft.datafixer.fix.BlockEntitySignTextStrictJsonFix.GSON;


public class LazuliEasyFileAcess {
    public static InputStream getPathFromGameAssets(String path) throws IOException {
        if (path.contains("minecraft")) {
            InputStream stream = MinecraftClient.class
                    .getClassLoader()
                    .getResourceAsStream(path);

            if (stream == null) {
                throw new IOException("File not found: " + path);
            }

            return stream;
        } else {
            String[] parts = path.split("[/\\\\]"); // handles both / and \

            String modId = parts[1];

            Optional<ModContainer> containerOpt =
                    FabricLoader.getInstance().getModContainer(modId);

            if (containerOpt.isEmpty()) {
                throw new IOException("Mod not found: " + modId);
            }

            ModContainer container = containerOpt.get();

            Path jarPath = container.getOrigin().getPaths().get(0);

            FileSystem fs = FileSystems.newFileSystem(jarPath, (ClassLoader) null);

            Path resourcePath = fs.getPath(path);

            InputStream stream = Files.newInputStream(resourcePath);

            if (stream == null) {
                throw new IOException("File not found in mod: " + path);
            }

            return stream;
        }
    }

    public static JsonElement getGameAssetsPathJson(String path) throws IOException {
        return JsonParser.parseString(getGameAssetsPathString(path));
    }

    public static String getGameAssetsPathString(String path) throws IOException {
        InputStream stream = getPathFromGameAssets(path);
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static void writeString(Path path, String content) throws IOException {
        if(!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void writeJson(JsonObject json, Path outputPath) throws IOException {
        String jsonString = GSON.toJson(json);
        writeString(outputPath, jsonString);
    }
}
