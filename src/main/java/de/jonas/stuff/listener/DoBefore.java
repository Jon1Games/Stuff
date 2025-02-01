package de.jonas.stuff.listener;

import java.time.Instant;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.jonas.stuff.Stuff;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class DoBefore implements Listener {

    MiniMessage mm;
    ConfigurationSection sec;

    public DoBefore() {
        mm = MiniMessage.miniMessage();
        reloadConfig();

        var a = sec.getKeys(false).stream()
                .filter((n) -> !n.equalsIgnoreCase("Enabled"))
                .mapToLong(Long::parseLong)
                .filter((l) -> Instant.now().isBefore(Instant.ofEpochSecond(l)))
                .min();

        if (a.isPresent()) {
            ConfigurationSection cmd = sec.getConfigurationSection(String.valueOf(a.getAsLong()));

            boolean freezeTicks = cmd.getBoolean("FreezeTicks");
            if (freezeTicks) {
                Bukkit.getServer().getServerTickManager().setFrozen(true);
            }

            boolean unfreezeTicks = cmd.getBoolean("UnfreezeTicks");
            if (unfreezeTicks) {
                Bukkit.getServer().getServerTickManager().setFrozen(false);
            }

        }

    }

    @EventHandler
    public void doBefore(PlayerJoinEvent e) {
        for (String a : sec.getKeys(false)) {
            if (a.equalsIgnoreCase("Enabled")) {
                continue;
            }

            ConfigurationSection cmd = sec.getConfigurationSection(a);

            if (Instant.now().isBefore(Instant.ofEpochSecond(Long.parseLong(a)))) {
                int changeGamemode = cmd.getInt("ChangeGamemode");
                if (changeGamemode != -1) {
                    GameMode gamemode;
                    switch (changeGamemode) {
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
                        e.getPlayer().setGameMode(gamemode);
                    }
                }

                boolean teleportSpawn = cmd.getBoolean("TeleportSpawn");
                if (teleportSpawn) {
                    e.getPlayer().teleport(Stuff.INSTANCE.getSpawn());
                }

                List<String> commands = cmd.getStringList("Commands");
                commands.forEach(action -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action);
                });

                List<String> broadcast = cmd.getStringList("Broadcast");
                broadcast.forEach(action -> {
                    Bukkit.getServer().sendMessage(mm.deserialize(action));
                });
            }

        }
    }

    public void reloadConfig() {
        sec = Stuff.INSTANCE.getConfig().getConfigurationSection("DoBefore");
    }

}
