package lazuli_lib.test_mod;

import lazuli_lib.lazuli.data_containers.LazuliLine;
import lazuli_lib.lazuli.data_containers.Triangle;
import lazuli_lib.lazuli.preset_renders.PortalVfxManager;
import lazuli_lib.lazuli.rendering.LazuliCameraManager;
import lazuli_lib.lazuli.rendering.LazuliHudRenderManager;
import lazuli_lib.lazuli.rendering.LazuliWorldRenderQueueManager;
import lazuli_lib.lazuli.utill.LazuliMathUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class TestVfx {

    private static int tickCounter = 0;
    private static final Random random = new Random();
    private static int seed = 1;

    public static void runTestRenders() {


        LazuliCameraManager.setCameraDisplacement(new Vec3d(0,0,0));
        LazuliCameraManager.setCameraRotationDisplacement(new Quaternion(0, (float) Math.sin(Math.PI / 4), 0, (float) Math.cos(Math.PI / 4)));

        PortalVfxManager.createSingleDimensionalRift(
                new Vec3d(-5,5,-5),
                new Vec3d(5,5,5),
                5,
                10,
                1,
                0.6,
                1,
                2
        );

        LazuliWorldRenderQueueManager.addTriangle(
                new Triangle(
                        new Vec3d(5, 0, 0),
                         new Vec3d(-5, 0, 0),
                         new Vec3d(0, 5, 0),
                        LazuliMathUtils.rgbaToInt(100,100,100,255)
                )
        );


        // Register the tick event listener to add random cracks
        ClientTickEvents.END_WORLD_TICK.register(client -> {
            //===========[start ticking zone]=======================================================
            tickCounter++;



            if (tickCounter<60){
                PortalVfxManager.createRingDimensionalRift(
                        new Vec3d(0,tickCounter* 3 +50,0),
                        70-(((double) tickCounter /8)*((double) tickCounter /8) * 1.1),
                        40,
                        3,
                        0,
                        0
                );
            }

            if (tickCounter<90){
                PortalVfxManager.createRingDimensionalRift(
                        new Vec3d(0,200,0),
                        tickCounter*3,
                        40,
                        3,
                        100,
                        0
                );
            }

            if (tickCounter == 60) {
                int[] color = {4000, 4000, 6000, 2000};
                LazuliHudRenderManager.flashWithForce(color, 0.4);

                color = new int[]{200, 150, 200, 40};
                LazuliHudRenderManager.setScreenTintOverlay(color);

                PortalVfxManager.createSingleDimensionalRift(
                        new Vec3d(-280, 200, -50),
                        new Vec3d(280, 200, 50),
                        30,
                        20,
                        5,
                        3,
                        0,
                        0
                );

            }
            if (tickCounter == 75) {

                    int[] color = {4000, 4000, 6000, 260};
                    LazuliHudRenderManager.flashWithForce(color, 0.3);

                    color = new int[]{200, 150, 200, 30};
                    LazuliHudRenderManager.setScreenTintOverlay(color);

                    seed = 1;
                    PortalVfxManager.createDimensionalRift(
                            new Vec3d(random.nextDouble() * 200 - 100, 100, random.nextDouble() * 200 - 100),
                            seed, 10, 4 + (1.6 * seed), 3 + seed, 0.8, 0.3, seed, seed
                    );

                    seed ++;
                    PortalVfxManager.createDimensionalRift(
                            new Vec3d(random.nextDouble() * 200 - 100, 100, random.nextDouble() * 200 - 100),
                            seed, 7, 4 + (1.6 * seed), 2 + seed, 0.6, 0.3, seed, seed
                    );

                    seed++;
                    PortalVfxManager.createDimensionalRift(
                            new Vec3d(random.nextDouble() * 200 - 100, 100, random.nextDouble() * 200 - 100),
                            seed, 5, 4 + (1.6 * seed), 3 + seed, 0.7, 0.3, seed, seed
                    );

                    seed++;
                    PortalVfxManager.createDimensionalRift(
                            new Vec3d(random.nextDouble() * 200 - 100, 100, random.nextDouble() * 200 - 100),
                            seed, 5, 4 + (1.6 * seed), 3 + seed, 0.9, 0.3, seed, seed
                    );

            }

            if (tickCounter == 100) {
                tickCounter = 80 + seed;

                int[] color = {4000, 4000, 6000, 460};
                LazuliHudRenderManager.flashWithForce(color, 0.2);

                color = new int[]{200, 150, 200, 30};
                LazuliHudRenderManager.setScreenTintOverlay(color);

                seed++;
                PortalVfxManager.createDimensionalRift(
                        new Vec3d(random.nextDouble() * 200 - 100, 100, random.nextDouble() * 200 - 100),
                        seed, 10, 4 + (1.6 * seed), 3 + seed, 0.8, 0.3, seed, seed
                );

                seed ++;
                PortalVfxManager.createDimensionalRift(
                        new Vec3d(random.nextDouble() * 200 - 100, 100, random.nextDouble() * 200 - 100),
                        seed, 7, 4 + (1.6 * seed), 2 + seed, 0.6, 0.3, seed, seed
                );

                seed++;
                PortalVfxManager.createDimensionalRift(
                        new Vec3d(random.nextDouble() * 200 - 100, 100, random.nextDouble() * 200 - 100),
                        seed, 5, 4 + (1.6 * seed), 3 + seed, 0.7, 0.3, seed, seed
                );

                seed++;
                PortalVfxManager.createDimensionalRift(
                        new Vec3d(random.nextDouble() * 200 - 100, 100, random.nextDouble() * 200 - 100),
                        seed, 5, 4 + (1.6 * seed), 3 + seed, 0.9, 0.3, seed, seed
                );

            }


                    if (tickCounter == 2000) {
                        LazuliWorldRenderQueueManager.clearAllQueues();
                    tickCounter = 0;
                    }


            //===========[End ticking zone]=======================================================
        });
    }

    /**
     * Adds a random crack effect by spawning a randomly placed line.
     */
    private static void addRandomCrack(int seed) {


        int[] color = {4000,4000,6000,2000};

        LazuliHudRenderManager.flashWithForce(color,0.2);




    }
}
