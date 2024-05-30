package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommands {

    Stuff stuff = Stuff.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();

    public InfoCommands() {

        ConfigurationSection sec = stuff.getConfig().getConfigurationSection("InfoCommands");
        for (String a : sec.getKeys(false)) {
            if (a.equalsIgnoreCase("Enabled")) continue;

            ConfigurationSection cmd = sec.getConfigurationSection(a);

            if (!cmd.getBoolean("Enabled")) continue;

            List<String> aliases = cmd.getStringList("Aliases");

            new CommandAPICommand(("stuff:info:") + a)
                    .withAliases(aliases.toArray(new String[aliases.size()]))
                    .executes((sender, args) -> {
                        sender.sendMessage(mm.deserialize(cmd.getString("Message"),
                                Placeholder.component("player", sender instanceof Player p ? p.teamDisplayName() :
                                        sender.name())));
                    })
                    .register();
        }
    }
}
