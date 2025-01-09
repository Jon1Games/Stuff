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

public class DoBefore implements Listener{

    MiniMessage mm;

    public DoBefore() {
        mm = MiniMessage.miniMessage();
    }

    @EventHandler
    public void doBefore(PlayerJoinEvent e) {
        ConfigurationSection sec = Stuff.INSTANCE.getConfig().getConfigurationSection("DoBefore");
        for (String a : sec.getKeys(false)) {
            if (a.equalsIgnoreCase("Enabled")) continue;

            ConfigurationSection cmd = sec.getConfigurationSection(a);

            System.err.println("" + a);
            if (Instant.now().isBefore(Instant.ofEpochSecond(Long.parseLong(a)))) {
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
        
                boolean teleportSpawn = cmd.getBoolean("TeleportSpawn");
                if (teleportSpawn) {
                    Bukkit.getOnlinePlayers().forEach(action -> action.teleport(Stuff.INSTANCE.getSpawn()));
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
}
