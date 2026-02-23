package nishio.lazuli_lib.core.warp;

import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class LazuliWarpDefaultTargets {

    // All solid blocks, lava, fast-mode leaves, non-translucent falling blocks
    public static final Set<Identifier> WORLD_TERRAIN = new HashSet<>(Set.of(
            Identifier.ofVanilla("rendertype_solid"),
            Identifier.ofVanilla("rendertype_cutout"),
            Identifier.ofVanilla("rendertype_cutout_mipped"),
            Identifier.ofVanilla("rendertype_translucent"),
            Identifier.ofVanilla("rendertype_translucent_moving_block"),
            Identifier.ofVanilla("rendertype_tripwire")
    ));

    // Entities, mobs, armor, block entities
    public static final Set<Identifier> ENTITIES = new HashSet<>(Set.of(
            Identifier.ofVanilla("rendertype_entity_solid"),
            Identifier.ofVanilla("rendertype_entity_cutout"),
            Identifier.ofVanilla("rendertype_entity_cutout_no_cull"),
            Identifier.ofVanilla("rendertype_entity_cutout_no_cull_z_offset"),
            Identifier.ofVanilla("rendertype_entity_translucent"),
            Identifier.ofVanilla("rendertype_entity_translucent_cull"),
            Identifier.ofVanilla("rendertype_entity_translucent_emissive"),
            Identifier.ofVanilla("rendertype_entity_smooth_cutout"),
            Identifier.ofVanilla("rendertype_entity_no_outline"),
            Identifier.ofVanilla("rendertype_entity_shadow"),
            Identifier.ofVanilla("rendertype_entity_alpha")
    ));

    // Items held, in world, in GUI
    public static final Set<Identifier> ITEMS = new HashSet<>(Set.of(
            Identifier.ofVanilla("rendertype_item_entity_translucent_cull"),
            Identifier.ofVanilla("rendertype_entity_glint"),
            Identifier.ofVanilla("rendertype_entity_glint_direct"),
            Identifier.ofVanilla("rendertype_glint"),
            Identifier.ofVanilla("rendertype_glint_direct"),
            Identifier.ofVanilla("rendertype_glint_translucent"),
            Identifier.ofVanilla("rendertype_armor_cutout_no_cull"),
            Identifier.ofVanilla("rendertype_armor_entity_glint")
    ));

    // Sky, sun, moon, horizon
    public static final Set<Identifier> SKY = new HashSet<>(Set.of(
            Identifier.ofVanilla("position"),
            Identifier.ofVanilla("position_color"),
            Identifier.ofVanilla("position_tex"),
            Identifier.ofVanilla("position_tex_color"),
            Identifier.ofVanilla("position_color_tex_lightmap")
    ));

    // Clouds
    public static final Set<Identifier> CLOUDS = new HashSet<>(Set.of(
            Identifier.ofVanilla("rendertype_clouds")
    ));

    // Nether portal, end portal, end gateway
    public static final Set<Identifier> PORTALS = new HashSet<>(Set.of(
            Identifier.ofVanilla("rendertype_end_portal"),
            Identifier.ofVanilla("rendertype_end_gateway"),
            Identifier.ofVanilla("rendertype_portal")
    ));

    // Beacon beam
    public static final Set<Identifier> BEACON = new HashSet<>(Set.of(
            Identifier.ofVanilla("rendertype_beacon_beam")
    ));

    // Block outlines, hit-box debug
    public static final Set<Identifier> OUTLINES = new HashSet<>(Set.of(
            Identifier.ofVanilla("rendertype_lines"),
            Identifier.ofVanilla("rendertype_crumbling"),
            Identifier.ofVanilla("rendertype_outline")
    ));

    // Water overlay, spyglass vignette, etc.
    public static final Set<Identifier> OVERLAYS = new HashSet<>(Set.of(
            Identifier.ofVanilla("rendertype_water_mask"),
            Identifier.ofVanilla("rendertype_leash")
    ));

    // GUI / HUD elements
    public static final Set<Identifier> GUI = new HashSet<>(Set.of(
            Identifier.ofVanilla("position_color_tex"),
            Identifier.ofVanilla("rendertype_text"),
            Identifier.ofVanilla("rendertype_text_background"),
            Identifier.ofVanilla("rendertype_text_see_through"),
            Identifier.ofVanilla("rendertype_gui"),
            Identifier.ofVanilla("rendertype_gui_ghost_recipe_overlay"),
            Identifier.ofVanilla("rendertype_gui_text_highlight")
    ));
}
