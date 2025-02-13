package lazuli_lib.lazuli.acess.LazuliRegistry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lazuli_lib.lazuli.acess.data_containers.LazuliLine;
import lazuli_lib.lazuli.acess.data_containers.LazuliLineBuffer;
import lazuli_lib.lazuli.acess.data_containers.Triangle;
import lazuli_lib.lazuli.acess.data_containers.TriangleBuffer;
import lazuli_lib.lazuli.acess.utill.LazuliMathUtils;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles primitive registration and JSON loading.
 */
public class LazuliPrimitivesRegistry {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Vec3d.class, (com.google.gson.JsonDeserializer<Vec3d>)
                    (json, type, context) -> {
                        List<Double> list = context.deserialize(json, List.class);
                        return new Vec3d(list.get(0), list.get(1), list.get(2));
                    }).create();

    private static final Map<Identifier, TriangleBuffer> TRIANGLE_PRIMITIVES = new HashMap<>();
    private static final Map<Identifier, LazuliLineBuffer> LINE_PRIMITIVES = new HashMap<>();

    /**
     * Registers a primitive from a JSON file.
     * @param id The unique identifier (e.g., "mymod:sphere")
     * @param jsonPath The path to the JSON file (e.g., "mymod:models/primitives/sphere.json")
     */
    public static void registerPrimitiveFromJson(Identifier id, String jsonPath) {
        try {
            String[] parts = jsonPath.split(":");
            if (parts.length != 2) {
                System.err.println("Invalid JSON path format: " + jsonPath);
                return;
            }

            String namespace = parts[0];
            String path = "assets/" + namespace + "/" + parts[1];

            InputStream inputStream = LazuliPrimitivesRegistry.class.getClassLoader().getResourceAsStream(path);

            if (inputStream == null) {
                System.err.println("Primitive JSON file not found: " + jsonPath);
                return;
            }

            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                Type type = new TypeToken<PrimitiveData>() {}.getType();
                PrimitiveData data = GSON.fromJson(reader, type);

                // Create buffers for the primitive
                TriangleBuffer triangleBuffer = new TriangleBuffer();
                LazuliLineBuffer lineBuffer = new LazuliLineBuffer();

                // Convert triangles
                if (data.triangles != null) {
                    for (TriangleData tri : data.triangles) {
                        int color = LazuliMathUtils.rgbaToInt(tri.color[0], tri.color[1], tri.color[2], tri.color[3]);
                        triangleBuffer.addTriangle(new Triangle(tri.v1, tri.v2, tri.v3, color));
                    }
                }

                // Convert lines
                if (data.lines != null) {
                    for (LineData line : data.lines) {
                        int color = LazuliMathUtils.rgbaToInt(line.color[0], line.color[1], line.color[2], line.color[3]);
                        lineBuffer.addLine(new LazuliLine(line.v1, line.v2, line.width, line.z, color));
                    }
                }

                // Register the primitive
                registerPrimitive(id, triangleBuffer, lineBuffer);
                System.out.println("Registered primitive: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error loading primitive: " + jsonPath);
            e.printStackTrace();
        }
    }

    /**
     * Registers a new primitive manually (alternative to JSON).
     * @param id The unique identifier (e.g., "mymod:sphere")
     * @param triangles The triangle buffer
     * @param lines The line buffer
     */
    public static void registerPrimitive(Identifier id, TriangleBuffer triangles, LazuliLineBuffer lines) {
        TRIANGLE_PRIMITIVES.put(id, triangles);
        LINE_PRIMITIVES.put(id, lines);
    }

    /**
     * Retrieves a registered triangle primitive.
     * @param id The primitive identifier.
     * @return The corresponding TriangleBuffer or null if not found.
     */
    public static TriangleBuffer getTrianglePrimitive(Identifier id) {
        return TRIANGLE_PRIMITIVES.get(id);
    }

    /**
     * Retrieves a registered line primitive.
     * @param id The primitive identifier.
     * @return The corresponding LazuliLineBuffer or null if not found.
     */
    public static LazuliLineBuffer getLinePrimitive(Identifier id) {
        return LINE_PRIMITIVES.get(id);
    }

    // 🔹 Inner classes for JSON parsing
    private static class PrimitiveData {
        List<TriangleData> triangles;
        List<LineData> lines;
    }

    private static class TriangleData {
        Vec3d v1;
        Vec3d v2;
        Vec3d v3;
        int[] color; // RGBA Array
    }

    private static class LineData {
        Vec3d v1;
        Vec3d v2;
        float width;
        float z;
        int[] color; // RGBA Array
    }
}
