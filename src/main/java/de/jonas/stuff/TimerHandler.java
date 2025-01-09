package de.jonas.stuff;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;

import de.jonas.stuff.utility.Timing;
import de.jonas.stuff.utility.Timing.TaskHolder;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class TimerHandler {

    List<BossbarDisplayTimer> timers;
    BossBar bar; 
    int index;
    boolean barAktive;

    public TimerHandler() {
        bar = BossBar.bossBar(Component.text(""), 1, Color.BLUE, Overlay.PROGRESS);
        timers = new ArrayList<>();
        index = 0;
        loadConfig();
    }

    private void loadConfig() {
        MiniMessage mm = MiniMessage.miniMessage();
        ConfigurationSection sec = Stuff.INSTANCE.getConfig().getConfigurationSection("Timings");
        for (String a : sec.getKeys(false)) {
            if (a.equalsIgnoreCase("Enabled")) continue;

            ConfigurationSection cmd = sec.getConfigurationSection(a);
            
            if (!cmd.getBoolean("Enabled")) continue;

            Instant end = Instant.ofEpochSecond(cmd.getLong("Time"));
            String text = cmd.getString("BossBar");

            timers.add(new BossbarDisplayTimer(
                end,
                (duration, bossBar) -> {
                    long days = duration.toDays();
                    duration = duration.minusDays(days);
        
                    long hours = duration.toHours();
                    duration = duration.minusHours(hours);
        
                    long minutes = duration.toMinutes();
                    duration = duration.minusMinutes(minutes);
        
                    long seconds = duration.getSeconds();
                    bossBar.name(
                        mm.deserialize(text, 
                            Placeholder.component("days", Component.text(days)),
                            Placeholder.component("hours", Component.text(hours)),
                            Placeholder.component("minutes", Component.text(minutes)),
                            Placeholder.component("seconds", Component.text(seconds))
                            )
                        );
                },
                (duration, bossBar) -> {
                    bossBar.progress(1);
                    bossBar.color(Color.RED);
                    bossBar.overlay(Overlay.PROGRESS);
                })
                );

            Timing.runTaskLater(end, Stuff.INSTANCE, () -> {
                boolean teleportSpawn = cmd.getBoolean("TeleportSpawn");
                if (teleportSpawn) {
                    Bukkit.getOnlinePlayers().forEach(action -> action.teleport(Stuff.INSTANCE.getSpawn()));
                }

                int changeGamemode = cmd.getInt("ChangeGamemode");
                if (changeGamemode != -1) {
                    GameMode gamemode;
                    switch (changeGamemode){
                        case 0:
                            gamemode = GameMode.SURVIVAL;
                            break;
                        case 1:
                            gamemode = GameMode.CREATIVE;
                            break;
                        case 2:
                            gamemode = GameMode.ADVENTURE;
                            break;
                        case 3:
                            gamemode = GameMode.SPECTATOR;
                            break;
                        default:
                            gamemode = null;
                            break;
                        }

                    if (gamemode != null) {
                        Bukkit.getOnlinePlayers().forEach(action -> action.setGameMode(gamemode));
                    }
                }

                boolean freezeTicks = cmd.getBoolean("FreezeTicks");
                if (freezeTicks) {
                    Bukkit.getServer().getServerTickManager().setFrozen(true);
                }

                boolean unfreezeTicks = cmd.getBoolean("UnfreezeTicks");
                if (unfreezeTicks) {
                    Bukkit.getServer().getServerTickManager().setFrozen(false);
                }

                List<String> commands = cmd.getStringList("Commands");
                commands.forEach(action -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
                });

                List<String> broadcast = cmd.getStringList("Broadcast");
                broadcast.forEach(action -> {
                    Bukkit.getServer().sendMessage(mm.deserialize(action));
                });
            });
        }
        Stuff.INSTANCE.getLogger().log(Level.CONFIG, "Loaded TimerHandler");
        bossBar();
    }

    private void bossBar() {  
        barAktive = true;

        Timing.TaskHolder a = Timing.runTaskTimer(Duration.ofSeconds(1), Stuff.INSTANCE, () -> {
            if (timers.isEmpty()) { return; }
            if (index >= timers.size()) index = 0;
            BossbarDisplayTimer b = timers.get(index);

            b.format.accept(Duration.between(Instant.now(), b.end), bar);

        });

        TaskHolder b = new Timing.TaskHolder();
        Timing.runTaskTimer(Duration.ofSeconds(5), Stuff.INSTANCE, () -> {
            if (timers.isEmpty()) {
                a.task.cancel();
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.hideBossBar(bar);
                });
                barAktive = false;
                b.task.cancel();
                return;
            }
            if (index >= timers.size()) {index = 0;}

            BossbarDisplayTimer c = timers.get(index);

            while (Instant.now().isAfter(c.end)) {
                timers.remove(index);
                if (index >= timers.size()) index = 0;
                if (timers.isEmpty()) return;
                c = timers.get(index);
            }

            c.formatTakeOver.accept(Duration.between(Instant.now(), c.end), bar);

            index++;
        }, b);

        Stuff.INSTANCE.getLogger().log(Level.ALL, "Loaded BossBar");   

    }

    public static record BossbarDisplayTimer(Instant end, BiConsumer<Duration, BossBar> format, BiConsumer<Duration, BossBar> formatTakeOver) {}
    
    public boolean barAktive() {
        return barAktive;
    }

    public BossBar getBar() {
        if (barAktive()) {
            return bar;
        } else {
            return null;
        }
    }

}
