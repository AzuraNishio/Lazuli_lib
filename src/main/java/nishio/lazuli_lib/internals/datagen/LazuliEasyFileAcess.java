package nishio.lazuli_lib.internals.datagen;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil.GSON;

public class LazuliEasyFileAcess {
    public static InputStream getVanillaPath(String path) throws IOException {
        InputStream stream = MinecraftClient.class
                .getClassLoader()
                .getResourceAsStream(path);

        if (stream == null) {
            throw new IOException("File not found: " + path);
        }

        return stream;
    }

    public static JsonElement getVanillaPathJson(String path) throws IOException {
        return JsonParser.parseString(getVanillaPathString(path));
    }

    public static String getVanillaPathString(String path) throws IOException {
        InputStream stream = getVanillaPath(path);
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
