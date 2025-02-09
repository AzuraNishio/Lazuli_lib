package lazuli_lib.lazuli.utill;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.*;

import java.lang.reflect.Field;

/**
 * Utility class for mathematical transformations.
 */
public class LazuliMathUtils {

    public static int rgbaToInt(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static Vec3d rotateAroundAxis(Vec3d vector, Vec3d axis, double angleDegrees) {
        Vec3d normalizedAxis = axis.normalize();

        double cosTheta = Math.cos(Math.toRadians(angleDegrees));
        double sinTheta = Math.sin(Math.toRadians(angleDegrees));

        Vec3d parallelComponent = normalizedAxis.multiply(vector.dotProduct(normalizedAxis));
        Vec3d perpendicularComponent = vector.subtract(parallelComponent);
        Vec3d crossProductComponent = normalizedAxis.crossProduct(vector);

        return parallelComponent
                .add(perpendicularComponent.multiply(cosTheta))
                .add(crossProductComponent.multiply(sinTheta));
    }

}
