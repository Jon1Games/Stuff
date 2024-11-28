package de.jonas.stuff.commands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
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
            .withArguments(new PlayerArgument(conf.getString("Sudo.suggestionName.Player")))
            .withArguments(new GreedyStringArgument(conf.getString("Sudo.suggestionName.Sudo")))
            .executes((sender, args) -> {
                Player player = (Player) args.get(conf.getString("Sudo.suggestionName.Player"));
                String sudo = (String) args.get(conf.getString("Sudo.suggestionName.Sudo"));

                if (player.hasPermission(conf.getString("Sudo.BypassPermission"))) {
                    sender.sendMessage(mm.deserialize(conf.getString("Sudo.Messages.playerHasBypass"),
                        Placeholder.component(conf.getString("Sudo.suggestionName.Player"), player.displayName()),
                        Placeholder.component(conf.getString("Sudo.suggestionName.Sudo"), Component.text(sudo))
                    ));
                    return;
                }

                if (sudo.startsWith("/")) {
                    sender.sendMessage(mm.deserialize(conf.getString("Sudo.Messages.sudoCommand"),
                        Placeholder.component(conf.getString("Sudo.suggestionName.Player"), player.displayName()),
                        Placeholder.component(conf.getString("Sudo.suggestionName.Sudo"), Component.text(sudo))
                    ));
                    Bukkit.getServer().dispatchCommand(player, sudo.substring(1));
                } else {
                    sender.sendMessage(mm.deserialize(conf.getString("Sudo.Messages.sudoMessage"),
                        Placeholder.component(conf.getString("Sudo.suggestionName.Player"), player.displayName()),
                        Placeholder.component(conf.getString("Sudo.suggestionName.Sudo"), Component.text(sudo))
                    ));
                    player.chat(sudo);
                }

            })
        .register();

    }
    
}
