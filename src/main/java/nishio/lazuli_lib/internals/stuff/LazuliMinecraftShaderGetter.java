package nishio.lazuli_lib.internals.stuff;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import nishio.lazuli_lib.internals.LazuliLog;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class LazuliMinecraftShaderGetter {

    public static final List<String> ALL_PROGRAM_NAMES = List.of(
            "particle",
            "position",
            "position_color",
            "position_color_lightmap",
            "position_color_tex_lightmap",
            "position_tex",
            "position_tex_color",
            "rendertype_solid",
            "rendertype_cutout",
            "rendertype_cutout_mipped",
            "rendertype_translucent",
            "rendertype_translucent_moving_block",
            "rendertype_tripwire",
            "rendertype_armor_cutout_no_cull",
            "rendertype_armor_entity_glint",
            "rendertype_beacon_beam",
            "rendertype_clouds",
            "rendertype_crumbling",
            "rendertype_end_gateway",
            "rendertype_end_portal",
            "rendertype_entity_alpha",
            "rendertype_entity_cutout",
            "rendertype_entity_cutout_no_cull",
            "rendertype_entity_cutout_no_cull_z_offset",
            "rendertype_entity_decal",
            "rendertype_entity_glint",
            "rendertype_entity_glint_direct",
            "rendertype_entity_no_outline",
            "rendertype_entity_shadow",
            "rendertype_entity_smooth_cutout",
            "rendertype_entity_solid",
            "rendertype_entity_translucent",
            "rendertype_entity_translucent_cull",
            "rendertype_entity_translucent_emissive",
            "rendertype_energy_swirl",
            "rendertype_eyes",
            "rendertype_glint",
            "rendertype_glint_direct",
            "rendertype_glint_translucent",
            "rendertype_gui",
            "rendertype_gui_ghost_recipe_overlay",
            "rendertype_gui_overlay",
            "rendertype_gui_text_highlight",
            "rendertype_item_entity_translucent_cull",
            "rendertype_leash",
            "rendertype_lightning",
            "rendertype_lines",
            "rendertype_outline",
            "rendertype_text",
            "rendertype_text_background",
            "rendertype_text_background_see_through",
            "rendertype_text_intensity",
            "rendertype_text_intensity_see_through",
            "rendertype_text_see_through",
            "rendertype_water_mask",
            "rendertype_breeze_wind"
    );
    @Nullable
    public static ShaderProgram getProgram(String name) {
        return switch (name) {
            case "particle" -> GameRenderer.getParticleProgram();
            case "position" -> GameRenderer.getPositionProgram();
            case "position_color" -> GameRenderer.getPositionColorProgram();
            case "position_color_lightmap" -> GameRenderer.getPositionColorLightmapProgram();
            case "position_color_tex_lightmap" -> GameRenderer.getPositionColorTexLightmapProgram();
            case "position_tex" -> GameRenderer.getPositionTexProgram();
            case "position_tex_color" -> GameRenderer.getPositionTexColorProgram();
            case "rendertype_solid" -> GameRenderer.getRenderTypeSolidProgram();
            case "rendertype_cutout" -> GameRenderer.getRenderTypeCutoutProgram();
            case "rendertype_cutout_mipped" -> GameRenderer.getRenderTypeCutoutMippedProgram();
            case "rendertype_translucent" -> GameRenderer.getRenderTypeTranslucentProgram();
            case "rendertype_translucent_moving_block" -> GameRenderer.getRenderTypeTranslucentMovingBlockProgram();
            case "rendertype_tripwire" -> GameRenderer.getRenderTypeTripwireProgram();
            case "rendertype_armor_cutout_no_cull" -> GameRenderer.getRenderTypeArmorCutoutNoCullProgram();
            case "rendertype_armor_entity_glint" -> GameRenderer.getRenderTypeArmorEntityGlintProgram();
            case "rendertype_beacon_beam" -> GameRenderer.getRenderTypeBeaconBeamProgram();
            case "rendertype_clouds" -> GameRenderer.getRenderTypeCloudsProgram();
            case "rendertype_crumbling" -> GameRenderer.getRenderTypeCrumblingProgram();
            case "rendertype_end_gateway" -> GameRenderer.getRenderTypeEndGatewayProgram();
            case "rendertype_end_portal" -> GameRenderer.getRenderTypeEndPortalProgram();
            case "rendertype_entity_alpha" -> GameRenderer.getRenderTypeEntityAlphaProgram();
            case "rendertype_entity_cutout" -> GameRenderer.getRenderTypeEntityCutoutProgram();
            case "rendertype_entity_cutout_no_cull" -> GameRenderer.getRenderTypeEntityCutoutNoNullProgram();
            case "rendertype_entity_cutout_no_cull_z_offset" -> GameRenderer.getRenderTypeEntityCutoutNoNullZOffsetProgram();
            case "rendertype_entity_decal" -> GameRenderer.getRenderTypeEntityDecalProgram();
            case "rendertype_entity_glint" -> GameRenderer.getRenderTypeEntityGlintProgram();
            case "rendertype_entity_glint_direct" -> GameRenderer.getRenderTypeEntityGlintDirectProgram();
            case "rendertype_entity_no_outline" -> GameRenderer.getRenderTypeEntityNoOutlineProgram();
            case "rendertype_entity_shadow" -> GameRenderer.getRenderTypeEntityShadowProgram();
            case "rendertype_entity_smooth_cutout" -> GameRenderer.getRenderTypeEntitySmoothCutoutProgram();
            case "rendertype_entity_solid" -> GameRenderer.getRenderTypeEntitySolidProgram();
            case "rendertype_entity_translucent" -> GameRenderer.getRenderTypeEntityTranslucentProgram();
            case "rendertype_entity_translucent_cull" -> GameRenderer.getRenderTypeEntityTranslucentCullProgram();
            case "rendertype_entity_translucent_emissive" -> GameRenderer.getRenderTypeEntityTranslucentEmissiveProgram();
            case "rendertype_energy_swirl" -> GameRenderer.getRenderTypeEnergySwirlProgram();
            case "rendertype_eyes" -> GameRenderer.getRenderTypeEyesProgram();
            case "rendertype_glint" -> GameRenderer.getRenderTypeGlintProgram();
            case "rendertype_glint_direct" -> GameRenderer.getRenderTypeGlintDirectProgram();
            case "rendertype_glint_translucent" -> GameRenderer.getRenderTypeGlintTranslucentProgram();
            case "rendertype_gui" -> GameRenderer.getRenderTypeGuiProgram();
            case "rendertype_gui_ghost_recipe_overlay" -> GameRenderer.getRenderTypeGuiGhostRecipeOverlayProgram();
            case "rendertype_gui_overlay" -> GameRenderer.getRenderTypeGuiOverlayProgram();
            case "rendertype_gui_text_highlight" -> GameRenderer.getRenderTypeGuiTextHighlightProgram();
            case "rendertype_item_entity_translucent_cull" -> GameRenderer.getRenderTypeItemEntityTranslucentCullProgram();
            case "rendertype_leash" -> GameRenderer.getRenderTypeLeashProgram();
            case "rendertype_lightning" -> GameRenderer.getRenderTypeLightningProgram();
            case "rendertype_lines" -> GameRenderer.getRenderTypeLinesProgram();
            case "rendertype_outline" -> GameRenderer.getRenderTypeOutlineProgram();
            case "rendertype_text" -> GameRenderer.getRenderTypeTextProgram();
            case "rendertype_text_background" -> GameRenderer.getRenderTypeTextBackgroundProgram();
            case "rendertype_text_background_see_through" -> GameRenderer.getRenderTypeTextBackgroundSeeThroughProgram();
            case "rendertype_text_intensity" -> GameRenderer.getRenderTypeTextIntensityProgram();
            case "rendertype_text_intensity_see_through" -> GameRenderer.getRenderTypeTextIntensitySeeThroughProgram();
            case "rendertype_text_see_through" -> GameRenderer.getRenderTypeTextSeeThroughProgram();
            case "rendertype_water_mask" -> GameRenderer.getRenderTypeWaterMaskProgram();
            case "rendertype_breeze_wind" -> GameRenderer.getRenderTypeBreezeWindProgram();
            default -> {

                yield null;
            }
        };
    }

    @Nullable
    public static ShaderProgram getProgram(Identifier id) {
        return getProgram(id.getPath());
    }

    public static void setVanillaShaderUniforms(String name, int v) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(v);
        }
    }

    public static void setVanillaShaderUniforms(String name, int v1, int v2) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(v1, v2);
        }
    }

    public static void setVanillaShaderUniforms(String name, int v1, int v2, int v3) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(v1, v2, v3);
        }
    }

    public static void setVanillaShaderUniforms(String name, int v1, int v2, int v3, int v4) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(v1, v2, v3, v4);
        }
    }

    public static void setVanillaShaderUniforms(String name, float v) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(v);
        }
    }

    public static void setVanillaShaderUniforms(String name, float v1, float v2) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(v1, v2);
        }
    }

    public static void setVanillaShaderUniforms(String name, float v1, float v2, float v3) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(v1, v2, v3);
        }
    }

    public static void setVanillaShaderUniforms(String name, float v1, float v2, float v3, float v4) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(v1, v2, v3, v4);
        }
    }

    public static void setVanillaShaderUniforms(String name, Vec3d vec) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set((float) vec.x, (float) vec.y, (float) vec.z);
        }
    }

    public static void setVanillaShaderUniforms(String name, Vector3f vec) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(vec);
        }
    }

    public static void setVanillaShaderUniforms(String name, Vector4f vec) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(vec);
        }
    }

    public static void setVanillaShaderUniforms(String name, Matrix3f mat) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(mat);
        }
    }

    public static void setVanillaShaderUniforms(String name, Matrix4f mat) {
        for (String shader : ALL_PROGRAM_NAMES) {
            ShaderProgram program = getProgram(shader);
            if (program == null) continue;
            program.getUniformOrDefault(name).set(mat);
        }
    }

}
