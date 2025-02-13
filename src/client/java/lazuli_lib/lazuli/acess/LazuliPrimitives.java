package lazuli_lib.lazuli.acess;

import lazuli_lib.lazuli.acess.LazuliRegistry.LazuliPrimitivesRegistry;
import lazuli_lib.lazuli.acess.data_containers.LazuliLineBuffer;
import lazuli_lib.lazuli.acess.data_containers.TriangleBuffer;
import net.minecraft.util.Identifier;

public class LazuliPrimitives {
    // ✅ Register and Retrieve Primitives
    public static final TriangleBuffer SPHERE_TRIANGLES = registerTrianglePrimitive("sphere");
//    public static final LazuliLineBuffer SPHERE_LINES = registerLinePrimitive("sphere");

    public static final TriangleBuffer CUBE_TRIANGLES = registerTrianglePrimitive("cube");
//    public static final LazuliLineBuffer CUBE_LINES = registerLinePrimitive("cube");
//
//    public static final TriangleBuffer PYRAMID_TRIANGLES = registerTrianglePrimitive("pyramid");
//    public static final LazuliLineBuffer PYRAMID_LINES = registerLinePrimitive("pyramid");

    /**
     * Registers and retrieves a triangle primitive.
     * @param name The primitive name (e.g., "sphere").
     * @return The TriangleBuffer or null if not found.
     */
    private static TriangleBuffer registerTrianglePrimitive(String name) {
        Identifier id = new Identifier("lazuli_lib", name);
        String jsonPath = "lazuli_lib:models/primitives/" + name + ".json";

        // Register from JSON (if not already registered)
        LazuliPrimitivesRegistry.registerPrimitiveFromJson(id, jsonPath);

        // Retrieve and return the registered triangle buffer
        return LazuliPrimitivesRegistry.getTrianglePrimitive(id);
    }

    /**
     * Registers and retrieves a line primitive.
     * @param name The primitive name (e.g., "sphere").
     * @return The LazuliLineBuffer or null if not found.
     */
    private static LazuliLineBuffer registerLinePrimitive(String name) {
        Identifier id = new Identifier("lazuli_lib", name);
        String jsonPath = "lazuli_lib:models/primitives/" + name + ".json";

        // Register from JSON (if not already registered)
        LazuliPrimitivesRegistry.registerPrimitiveFromJson(id, jsonPath);

        // Retrieve and return the registered line buffer
        return LazuliPrimitivesRegistry.getLinePrimitive(id);
    }

    /**
     * Call this in your mod's initialization to ensure primitives are loaded.
     */
    public static void registerPrimitives() {
        System.out.println("Registering Lazuli Primitives...");
    }
}
