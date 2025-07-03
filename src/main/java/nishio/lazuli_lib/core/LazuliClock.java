package nishio.lazuli_lib.core;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple global clock for the client side. Provides cronometers and task scheduling.
 */
public class LazuliClock {

    private static long tickCounter = 0;
    private static final List<ScheduledTask> TASKS = new CopyOnWriteArrayList<>();

    private LazuliClock() {
    }

    /** Register the clock tick handler. */
    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            tickCounter++;
            runTasks();
        });
    }

    /** Current ticks since register was called. */
    public static long ticks() {
        return tickCounter;
    }

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

    private record ScheduledTask(long executeTick, Runnable runnable) {
    }

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

        /** Alias for start(). */
        public void reset() {
            start();
        }

        /** Read elapsed ticks. */
        public long readTicks() {
            return LazuliClock.ticks() - this.startTick;
        }

        /** Read elapsed time in seconds (20 ticks per second). */
        public double readSeconds() {
            return readTicks() / 20.0;
        }

        /** Set the cronometer to a specific tick value. */
        public void set(long ticks) {
            this.startTick = LazuliClock.ticks() - ticks;
        }
    }
}
