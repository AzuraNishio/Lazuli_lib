package nishio.lazuli_lib.internals.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import nishio.lazuli_lib.internals.LazuliLog;

import java.util.HashMap;
import java.util.Map;

public class LazuliSodiumResourcePackCompat {
    private static boolean isSodiumLoaded;
    private static String sodiumVersion;

    public static boolean isIsSodiumLoaded(){
        return isSodiumLoaded;
    }
    public static final Map<Identifier, Identifier> SODIUM_SHADER_WARP_REDIRECTS = new HashMap<>(Map.of(
            Identifier.of("minecraft", "rendertype_solid"),                    Identifier.of("sodium", "blocks/block_layer_opaque"),
            Identifier.of("minecraft", "rendertype_cutout"),                   Identifier.of("sodium", "blocks/block_layer_opaque"),
            Identifier.of("minecraft", "rendertype_cutout_mipped"),            Identifier.of("sodium", "blocks/block_layer_opaque"),
            Identifier.of("minecraft", "rendertype_translucent"),              Identifier.of("sodium", "blocks/block_layer_opaque"),
            Identifier.of("minecraft", "rendertype_translucent_moving_block"), Identifier.of("sodium", "blocks/block_layer_opaque"),
            Identifier.of("minecraft", "rendertype_tripwire"),                 Identifier.of("sodium", "blocks/block_layer_opaque")
    ));

    public static void checkForSodium(){
        if (!isSodiumLoaded && FabricLoader.getInstance().isModLoaded("sodium")){
            isSodiumLoaded = true;
            LazuliLog.Compat.info("Applying patches for Na (Sodium) compat!");
        }
        sodiumVersion = (isSodiumLoaded)? FabricLoader.getInstance().getModContainer("sodium").get().getMetadata().getVersion().toString() : "";
    }



    public static Identifier filterSodiumTargets(Identifier id){
        return (isSodiumLoaded)? SODIUM_SHADER_WARP_REDIRECTS.getOrDefault(id, id) : id;
    }

    public static JsonObject createSodiumVersionsFile(){

        //{
        //  "supported-versions": {
        //    "1.21": [ "0.5.11+mc1.21" ],
        //    "1.21.1": [ "0.5.11+mc1.21" ]
        //  }
        //}

        JsonObject meta = new JsonObject();
        JsonObject suportedVersions = new JsonObject();
        JsonArray versions = new JsonArray();
        versions.add(new JsonPrimitive(sodiumVersion));

        suportedVersions.add(
                FabricLoader.getInstance().getRawGameVersion(),
                versions
        );
        meta.add("supported-versions", suportedVersions);
        return meta;
    }


}
