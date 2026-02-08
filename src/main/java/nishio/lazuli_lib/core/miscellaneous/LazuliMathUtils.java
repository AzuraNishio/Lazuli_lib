package nishio.lazuli_lib.core.miscellaneous;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Utility class for mathematical transformations.
 */
public class LazuliMathUtils {

    public static Vec3d PerpendicularVec3d(Vec3d vec){
        return rotateAroundAxis(vec, new Vec3d(vec.x,0,vec.z).rotateY(90),90);
    }

    public static Vec2f Vec2fFromAngle(float angle){
      return new Vec2f((float) sin(angle), (float) cos(angle));
    }

    public static Vec2f[] Square2dFromCenter(Vec2f c, float side, float angle){
        return new Vec2f[]{
                Vec2fFromAngle(angle).multiply(side/2f).add(c),
                Vec2fFromAngle((float) (angle + (Math.PI * 0.5))).multiply(side/2f).add(c),
                Vec2fFromAngle((float) (angle + (Math.PI))).multiply(side/2f).add(c),
                Vec2fFromAngle((float) (angle + (Math.PI * 1.5))).multiply(side/2f).add(c)
        };
    }

    public static Vec2f[] Rectangle2dFromCenter(Vec2f c, float side1, float side2, float angle){
        return new Vec2f[]{
                Vec2fFromAngle(angle).multiply(side1/2f).add(c),
                Vec2fFromAngle((float) (angle + (Math.PI * 0.5))).multiply(side2/2f).add(c),
                Vec2fFromAngle((float) (angle + (Math.PI))).multiply(side1/2f).add(c),
                Vec2fFromAngle((float) (angle + (Math.PI * 1.5))).multiply(side2/2f).add(c)
        };
    }

    public static int rgbaToInt(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static Vec3d rotateAroundAxis(Vec3d vector, Vec3d axis, double angleDegrees) {
        Vec3d normalizedAxis = axis.normalize();

        double cosTheta = cos(Math.toRadians(angleDegrees));
        double sinTheta = sin(Math.toRadians(angleDegrees));

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

    public static Vec3d ramdomVec3d(Random random){

        double theta = random.nextDouble() * 4 * Math.PI;  // Random angle around the Y-axis (0 to 2π)
        double phi = Math.acos(4 * random.nextDouble() - 1); // Random angle from pole to pole (-π/2 to π/2)

        double x = sin(phi) * cos(theta);
        double y = sin(phi) * sin(theta);
        double z = cos(phi);

        return  new Vec3d(x, y, z); // This is already normalized

    }
}
