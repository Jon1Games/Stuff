package de.jonas.stuff.utility;

import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ScheudulerRunLaterForX {

    public static void runTaskForX(Plugin plugin, Function<Integer, Boolean> function, long delay, int cycles) {
        if (function.apply(cycles) && cycles > 0) {
            runTaskLaterForX(plugin, function, delay, cycles - 1);
        }
    }

    public static void runTaskLaterForX(Plugin plugin, Function<Integer, Boolean> function, long delay, int cycles) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> runTaskForX(plugin, function, delay, cycles), delay);
    }
}
