package de.jonas.stuff.utility;

import java.time.Duration;
import java.time.Instant;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class Timing {

    private static void runTaskLater(Instant time, Plugin plugin, Runnable task, TaskHolder holder) {
        float tickrate = Bukkit.getServer().getServerTickManager().getTickRate();
        long delay = time.toEpochMilli() - System.currentTimeMillis();
        float ticks = ((float) delay / 1_000f) * tickrate;

        if (ticks < 1) {
            task.run();
        } else {
            if (ticks > 20) {
                // 20 doesn't mean 1 second
                ticks = Math.max(ticks / 20, 20f);
                // Every 5 minutes we recalculate because of different tps
                ticks = Math.min(ticks, tickrate * 300);
            }

            holder.task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                runTaskLater(time, plugin, task);
            }, (long) ticks);
        }
    }

    public static TaskHolder runTaskLater(Instant time, Plugin plugin, Runnable task) {
        TaskHolder holder = new TaskHolder();
        runTaskLater(time, plugin, task, holder);
        return holder;
    }

    public static void runTaskTimer(Duration delay, Plugin plugin, Runnable task, TaskHolder holder) {
        float tickrate = Bukkit.getServer().getServerTickManager().getTickRate();
        float ticks = ((float) delay.toMillis() / 1_000f) * tickrate;

        if (ticks < 1) {
            // prevent an infinit Loop in same Tick
            throw new IllegalStateException("Can't run a Timer With Period of <1 Tick; delay=" + delay.toString() + ", plugin=" + plugin.getName());
        }

        holder.task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            runTaskTimer(delay, plugin, task, holder);
            task.run();
        }, (long) ticks);
    }

    public static TaskHolder runTaskTimer(Duration delay, Plugin plugin, Runnable task) {
        TaskHolder holder = new TaskHolder();
        runTaskTimer(delay, plugin, task, holder);
        return holder;
    }

    public static class TaskHolder {
        public BukkitTask task;
    }
}
