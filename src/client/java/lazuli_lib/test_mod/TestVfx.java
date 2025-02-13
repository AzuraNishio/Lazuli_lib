package lazuli_lib.test_mod;

import lazuli_lib.lazuli.acess.LazuliPrimitives;
import lazuli_lib.lazuli.acess.data_containers.LazuliLine;
import lazuli_lib.lazuli.acess.data_containers.Triangle;
import lazuli_lib.lazuli.acess.data_containers.TriangleBuffer;
import lazuli_lib.lazuli.acess.preset_renders.PortalVfxManager;
import lazuli_lib.lazuli.acess.LazuliCameraManager;
import lazuli_lib.lazuli.acess.LazuliHudRenderManager;
import lazuli_lib.lazuli.acess.LazuliWorldRenderQueueManager;
import lazuli_lib.lazuli.acess.utill.LazuliMathUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
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

        TriangleBuffer cube = LazuliPrimitives.SPHERE_TRIANGLES;

        // Register the tick event listener to add random cracks
        ClientTickEvents.END_WORLD_TICK.register(client -> {

            if (tickCounter == 2) {
                cube.displace(new Vec3d(0.5, 1.5, 0.5));
              //  cube.overwriteColor(LazuliMathUtils.rgbaToInt(200,20,20,150));
                LazuliWorldRenderQueueManager.addLongTriangleBuffer(0, cube);
            }
            if (tickCounter > 2) {
                  //  cube.rotateY(0.1,new Vec3d(0.5,1.5,0.5));
                  //  cube.rotateX(0.0235,new Vec3d(0.5,1.5,0.5));
                    LazuliWorldRenderQueueManager.addLongTriangleBuffer(0, cube);
            }



            tickCounter++;
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
