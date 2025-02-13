package lazuli_lib.lazuli.acess.preset_renders;



import net.minecraft.util.math.Vec3d;
import lazuli_lib.lazuli.acess.LazuliWorldRenderQueueManager;
import lazuli_lib.lazuli.acess.utill.LazuliMathUtils;
import lazuli_lib.lazuli.acess.data_containers.LazuliLine;
import lazuli_lib.lazuli.acess.data_containers.LazuliLineBuffer;


import java.util.Random;

public class PortalVfxManager {

    public static void createDimensionalRift(
            Vec3d center,
            int cracks, int iterations, double defaultStepSize, double variantion, double centerWidth, double tipsWidth, int ID, int seed

    ) {
        LazuliLineBuffer PortalGeometry = new LazuliLineBuffer();

        Random random = new Random(seed);

        for (int crackSeed = 0; crackSeed < cracks; crackSeed++) {

            Vec3d Dir = LazuliMathUtils.ramdomVec3d(random);

            double stepSize = defaultStepSize + random.nextDouble();


            Vec3d point2 = center;

            for (int i = 0; i < iterations; i++) {
                Vec3d ramdomVec3d = new Vec3d((random.nextDouble() - 0.5) * variantion, (random.nextDouble() - 0.5) * variantion, (random.nextDouble() - 0.5) * variantion);

                Vec3d point1 = point2;
                point2 = center.add(Dir.multiply(i * stepSize)).add(ramdomVec3d);

                double width = centerWidth - (((double) i / iterations) * (centerWidth - tipsWidth));

                PortalGeometry.addLine(new LazuliLine(
                        point1,
                        point2,
                        width * 2,
                        -0.03,
                        LazuliMathUtils.rgbaToInt(0, 0, 200, 30)
                ));

                PortalGeometry.addLine(new LazuliLine(
                        point1,
                        point2,
                        width,
                        0,
                        LazuliMathUtils.rgbaToInt(70, 90, 230, 100)
                ));

                PortalGeometry.addLine(new LazuliLine(
                        point1,
                        point2,
                        width * 0.5,
                        0.03,
                        LazuliMathUtils.rgbaToInt(170, 200, 255, 200)
                ));

                PortalGeometry.addLine(new LazuliLine(
                        point1,
                        point2,
                        width * 0.3,
                        0.05,
                        LazuliMathUtils.rgbaToInt(200, 230, 255, 255)
                ));

            }
        }

        LazuliWorldRenderQueueManager.addLongLineBuffer(5000 + ID, PortalGeometry);
    }


    public static void removePortal(int ID) {
        LazuliWorldRenderQueueManager.removeLineBuffer(5000 + ID);
    }

    public static void createSingleDimensionalRift(
            Vec3d start,
            Vec3d end, int iterations, double variantion, double centerWidth, double tipsWidth, int ID, int seed

    ) {
        LazuliLineBuffer PortalGeometry = new LazuliLineBuffer();

        Random random = new Random(seed);


        Vec3d Dir = end.subtract(start).normalize();

        double stepSize = end.distanceTo(start) / iterations;


        Vec3d point2 = start;

        for (int i = 0; i < iterations; i++) {
            Vec3d ramdomVec3d = new Vec3d((random.nextDouble() - 0.5) * variantion, (random.nextDouble() - 0.5) * variantion, (random.nextDouble() - 0.5) * variantion);

           Vec3d point1 = point2;
           point2 = start.add(Dir.multiply(i * stepSize)).add(ramdomVec3d);


            double width = (((double) iterations / 2)-Math.abs(i - ((double) iterations / 2))) * (centerWidth - tipsWidth) + tipsWidth;

            PortalGeometry.addLine(new LazuliLine(
                    point1,
                    point2,
                    width * 2,
                    -0.03,
                    LazuliMathUtils.rgbaToInt(0, 0, 200, 30)
            ));

            PortalGeometry.addLine(new LazuliLine(
                    point1,
                    point2,
                    width,
                    0,
                    LazuliMathUtils.rgbaToInt(70, 90, 230, 100)
            ));

            PortalGeometry.addLine(new LazuliLine(
                    point1,
                    point2,
                    width * 0.7,
                    0.03,
                    LazuliMathUtils.rgbaToInt(170, 200, 255, 200)
            ));

            PortalGeometry.addLine(new LazuliLine(
                    point1,
                    point2,
                    width * 0.5,
                    0.05,
                    LazuliMathUtils.rgbaToInt(200, 230, 255, 255)
            ));

        }

        LazuliWorldRenderQueueManager.addLongLineBuffer(5000 + ID, PortalGeometry);
    }

    public static void createRingDimensionalRift(
            Vec3d center,
            double radius, int iterations, double centerWidth, int ID, int seed

    ) {
        LazuliLineBuffer PortalGeometry = new LazuliLineBuffer();

        Random random = new Random(seed);


        double angle = 0;

        double stepSize = (double) 2 * Math.PI / iterations;


        Vec3d point2 = new Vec3d(Math.sin(angle)*radius,0,Math.cos(angle)*radius).add(center);

        for (int i = 0; i < iterations; i++) {

            angle += stepSize;

            Vec3d point1 = point2;
            point2 = new Vec3d(Math.sin(angle)*radius,0,Math.cos(angle)*radius).add(center);


            double width = centerWidth;

            PortalGeometry.addLine(new LazuliLine(
                    point1,
                    point2,
                    width * 2,
                    -0.03,
                    LazuliMathUtils.rgbaToInt(0, 0, 200, 30)
            ));

            PortalGeometry.addLine(new LazuliLine(
                    point1,
                    point2,
                    width,
                    0,
                    LazuliMathUtils.rgbaToInt(70, 90, 230, 100)
            ));

            PortalGeometry.addLine(new LazuliLine(
                    point1,
                    point2,
                    width * 0.7,
                    0.03,
                    LazuliMathUtils.rgbaToInt(170, 200, 255, 200)
            ));

            PortalGeometry.addLine(new LazuliLine(
                    point1,
                    point2,
                    width * 0.5,
                    0.05,
                    LazuliMathUtils.rgbaToInt(200, 230, 255, 255)
            ));

        }

        LazuliWorldRenderQueueManager.addLongLineBuffer(5000 + ID, PortalGeometry);
    }
}

