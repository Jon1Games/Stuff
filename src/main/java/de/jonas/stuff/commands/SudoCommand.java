// The line `package de.jonas.stuff.commands;` at the beginning of a Java file is declaring the package
// to which the Java class belongs. In this case, the class is part of the `commands` package within
// the `de.jonas.stuff` package hierarchy. This helps organize and categorize classes into different
// namespaces to avoid naming conflicts and provide a clear structure for the project.
package de.jonas.stuff.commands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.gaminglounge.configapi.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class SudoCommand {

    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();
    MiniMessage mm = MiniMessage.miniMessage();

    public SudoCommand() {

        new CommandAPICommand("stuff:sudo")
            .withPermission(conf.getString("Sudo.Permission"))
            .withAliases(conf.getStringList("Sudo.Aliases").toArray(num -> new String[num]))
            .withArguments(new PlayerArgument("player"))
            .withArguments(new GreedyStringArgument("sudo"))
            .executes((sender, args) -> {
                Player player = (Player) args.get("player");
                String sudo = (String) args.get("sudo");

                if (sender instanceof Player p) {
                    if (player.hasPermission(conf.getString("Sudo.BypassPermission"))) {
                        sender.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, p, "key", true),
                            Placeholder.component("player", player.displayName()),
                            Placeholder.component("sudo", Component.text(sudo))
                        ));
                        return;
                    }
    
                    if (sudo.startsWith("/")) {
                        sender.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, p, "key", true),
                            Placeholder.component("player", player.displayName()),
                            Placeholder.component("sudo", Component.text(sudo))
                        ));
                        Bukkit.getServer().dispatchCommand(player, sudo.substring(1));
                    } else {
                        sender.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, p, "key", true),
                            Placeholder.component("player", player.displayName()),
                            Placeholder.component("sudo", Component.text(sudo))
                        ));
                        player.chat(sudo);
                    }                    
                } else {
                    if (player.hasPermission(conf.getString("Sudo.BypassPermission"))) {
                        sender.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, "en_US", "key", true),
                            Placeholder.component("player", player.displayName()),
                            Placeholder.component("sudo", Component.text(sudo))
                        ));
                        return;
                    }
    
                    if (sudo.startsWith("/")) {
                        sender.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, "en_US", "key", true),
                            Placeholder.component("player", player.displayName()),
                            Placeholder.component("sudo", Component.text(sudo))
                        ));
                        Bukkit.getServer().dispatchCommand(player, sudo.substring(1));
                    } else {
                        sender.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, "en_US", "key", true),
                            Placeholder.component("player", player.displayName()),
                            Placeholder.component("sudo", Component.text(sudo))
                        ));
                        player.chat(sudo);
                    }
                }



            })
        .register();

    }
    
}
