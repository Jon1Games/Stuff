package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class CommandCommand {

    Stuff stuff = Stuff.INSTANCE;

    public CommandCommand() {

        ConfigurationSection sec = stuff.getConfig().getConfigurationSection("CommandCommand");
        for (String a : sec.getKeys(false)) {

            if (a.equalsIgnoreCase("Enabled")) continue;

            ConfigurationSection cmd = sec.getConfigurationSection(a);

            if (!cmd.getBoolean("Enabled")) continue;

            boolean console = cmd.getBoolean("RunAsConsole");
            String command = cmd.getString("Command");

            new CommandAPICommand(("stuff:command:" + a))
                .withPermission(cmd.getString("Permission"))
                .withAliases(cmd.getStringList("Aliases").toArray(num -> new String[num]))
                .executes((sender, args) -> {
                    if (console) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                .replaceAll("<player>", sender.getName()));
                    } else {
                        Bukkit.dispatchCommand(sender, command.replaceAll("<player>", sender.getName()));
                    }
                })
                .register();
        }
    }
}
