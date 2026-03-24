package nishio.lazuli_lib.core.warp;

import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class LazuliWarpDefaultTargets {

    // All solid blocks, lava, fast-mode leaves, non-translucent falling blocks
    public static final Set<Identifier> WORLD_TERRAIN = new HashSet<>(Set.of(
            Identifier.of("minecraft", "rendertype_solid"),
            Identifier.of("minecraft", "rendertype_cutout"),
            Identifier.of("minecraft", "rendertype_cutout_mipped"),
            Identifier.of("minecraft", "rendertype_translucent"),
            Identifier.of("minecraft", "rendertype_translucent_moving_block"),
            Identifier.of("minecraft", "rendertype_tripwire")
    ));

    // Entities, mobs, armor, block entities
    public static final Set<Identifier> ENTITIES = new HashSet<>(Set.of(
            Identifier.of("minecraft", "rendertype_entity_solid"),
            Identifier.of("minecraft", "rendertype_entity_cutout"),
            Identifier.of("minecraft", "rendertype_entity_cutout_no_cull"),
            Identifier.of("minecraft", "rendertype_entity_cutout_no_cull_z_offset"),
            Identifier.of("minecraft", "rendertype_entity_translucent"),
            Identifier.of("minecraft", "rendertype_entity_translucent_cull"),
            Identifier.of("minecraft", "rendertype_entity_translucent_emissive"),
            Identifier.of("minecraft", "rendertype_entity_smooth_cutout"),
            Identifier.of("minecraft", "rendertype_entity_no_outline"),
            Identifier.of("minecraft", "rendertype_entity_shadow"),
            Identifier.of("minecraft", "rendertype_entity_alpha")
    ));

    // Items held, in world, in GUI
    public static final Set<Identifier> ITEMS = new HashSet<>(Set.of(
            Identifier.of("minecraft", "rendertype_item_entity_translucent_cull"),
            Identifier.of("minecraft", "rendertype_entity_glint"),
            Identifier.of("minecraft", "rendertype_entity_glint_direct"),
            Identifier.of("minecraft", "rendertype_glint"),
            Identifier.of("minecraft", "rendertype_glint_direct"),
            Identifier.of("minecraft", "rendertype_glint_translucent"),
            Identifier.of("minecraft", "rendertype_armor_cutout_no_cull"),
            Identifier.of("minecraft", "rendertype_armor_entity_glint")
    ));

    // Sky, sun, moon, horizon
    public static final Set<Identifier> SKY = new HashSet<>(Set.of(
            Identifier.of("minecraft", "position"),
            Identifier.of("minecraft", "position_color"),
            Identifier.of("minecraft", "position_tex"),
            Identifier.of("minecraft", "position_tex_color"),
            Identifier.of("minecraft", "position_color_tex_lightmap")
    ));


    // Nether portal, end portal, end gateway
    public static final Set<Identifier> PORTALS = new HashSet<>(Set.of(
            Identifier.of("minecraft", "rendertype_end_portal"),
            Identifier.of("minecraft", "rendertype_end_gateway"),
            Identifier.of("minecraft", "rendertype_portal")
    ));

    // Beacon beam
    public static final Set<Identifier> BEACON = new HashSet<>(Set.of(
            Identifier.of("minecraft", "rendertype_beacon_beam")
    ));

    // Block outlines, hit-box debug
    public static final Set<Identifier> OUTLINES = new HashSet<>(Set.of(
            Identifier.of("minecraft", "rendertype_lines"),
            Identifier.of("minecraft", "rendertype_crumbling"),
            Identifier.of("minecraft", "rendertype_outline")
    ));

    // Water overlay, spyglass vignette, etc.
    public static final Set<Identifier> OVERLAYS = new HashSet<>(Set.of(
            Identifier.of("minecraft", "rendertype_water_mask"),
            Identifier.of("minecraft", "rendertype_leash")
    ));

    // GUI / HUD elements
    public static final Set<Identifier> GUI = new HashSet<>(Set.of(
            Identifier.of("minecraft", "position_color_tex"),
            Identifier.of("minecraft", "rendertype_text"),
            Identifier.of("minecraft", "rendertype_text_background"),
            Identifier.of("minecraft", "rendertype_text_see_through"),
            Identifier.of("minecraft", "rendertype_gui"),
            Identifier.of("minecraft", "rendertype_gui_ghost_recipe_overlay"),
            Identifier.of("minecraft", "rendertype_gui_text_highlight")
    ));
}
