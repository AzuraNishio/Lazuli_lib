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

    public static int[] addArrays(int[] a, int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Arrays must have the same length!");
        }

        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }

    public static int[] multiplyAndRound(int[] array, double multiplier) {
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = (int) Math.round(array[i] * multiplier);
        }
        return result;
    }



}
