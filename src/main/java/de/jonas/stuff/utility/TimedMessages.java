package de.jonas.stuff.utility;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.*;
import de.jonas.stuff.Stuff;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class TimedMessages {

    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();
    MiniMessage mm = MiniMessage.miniMessage();

    public TimedMessages() {
        ConfigurationSection sec = stuff.getConfig().getConfigurationSection("TimedMessages");
        for (String a : sec.getKeys(false)) {
            if (a.equalsIgnoreCase("Enabled")) continue;
            ConfigurationSection cmd = sec.getConfigurationSection(a);
            if (!cmd.getBoolean("Enabled")) continue;

            List<String> messages = cmd.getStringList("Messages");
            boolean random = cmd.getBoolean("Random");
            long period = cmd.getLong("Period") * 20;

            Bukkit.getScheduler().runTaskTimer(stuff, () -> {
                if (random) {
                    int num = new Random().nextInt(messages.size());
                    Component message = mm.deserialize(messages.get(num));
                    Bukkit.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(message));
                }
            }, period, period);


        }
    }

}
