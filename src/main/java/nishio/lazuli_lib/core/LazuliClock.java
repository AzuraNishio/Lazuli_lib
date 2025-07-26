package nishio.lazuli_lib.core;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;   // ← new import

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple global clock for the client side. Provides cronometers and task scheduling.
 */
public class LazuliClock {

    private static long tickCounter = 0;
    private static final List<ScheduledTask> TASKS = new CopyOnWriteArrayList<>();

    /* ─────────────────────────────────────────────
     *  CLOCK CORE
     * ───────────────────────────────────────────── */

    /** Register the clock tick handler. */
    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            tickCounter++;
            runTasks();
        });
    }

    /** Whole ticks since {@link #register()} was called. */
    public static long ticks() {
        return tickCounter;
    }

    /* ─────────────────────────────────────────────
     *  NEW  ►  LERP SUPPORT
     * ───────────────────────────────────────────── */

    /**
     * Partial ticks supplied by the renderer (value in [0, 1) inside a frame).
     */
    private static float tickDelta() {
        return MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
    }

    /**
     * Whole + fractional ticks suitable for smooth rendering.
     */
    public static float lerpedTicks() {
        return tickCounter + tickDelta();
    }

    /**
     * Convenience: render-time seconds (20 ticks = 1 s).
     */
    public static float lerpedSeconds() {
        return (float) (lerpedTicks() / 20.0);
    }

    /* ─────────────────────────────────────────────
     *  SCHEDULING
     * ───────────────────────────────────────────── */

    /** Create a new cronometer bound to this clock. */
    public static Cronometer newCronometer() {
        return new Cronometer();
    }

    /** Schedule a runnable to execute after the given number of ticks. */
    public static void schedule(Runnable runnable, long ticks) {
        TASKS.add(new ScheduledTask(tickCounter + ticks, runnable));
    }

    private static void runTasks() {
        for (ScheduledTask task : TASKS) {
            if (task.executeTick <= tickCounter) {
                task.runnable.run();
                TASKS.remove(task);
            }
        }
    }

    private record ScheduledTask(long executeTick, Runnable runnable) {}

    /* ─────────────────────────────────────────────
     *  CRONOMETER
     * ───────────────────────────────────────────── */

    /** Cronometer measuring time using the LazuliClock. */
    public static class Cronometer {
        private long startTick;

        Cronometer() {
            this.startTick = LazuliClock.ticks();
        }

        /** Start counting from current time. */
        public void start() {
            this.startTick = LazuliClock.ticks();
        }

        /** Alias for {@link #start()}. */
        public void reset() {
            start();
        }

        /** Elapsed whole ticks (logic time). */
        public long readTicks() {
            return LazuliClock.ticks() - this.startTick;
        }

        /** Elapsed whole seconds (logic time). */
        public double readSeconds() {
            return readTicks() / 20.0;
        }

        /** NEW: elapsed ticks, smoothly interpolated for the current frame. */
        public float readLerpedTicks() {
            return LazuliClock.lerpedTicks() - this.startTick;
        }

        /** NEW: elapsed seconds, smoothly interpolated for the current frame. */
        public float readLerpedSeconds() {
            return (float) (readLerpedTicks() / 20.0);
        }

        /** Set the cronometer to a specific tick value. */
        public void set(long ticks) {
            this.startTick = LazuliClock.ticks() - ticks;
        }
    }
}
