package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;

public class BroadcastCommand {

    Stuff stuff = new Stuff();
    FileConfiguration conf = stuff.getConfig();
    MiniMessage mm = MiniMessage.miniMessage();

    public BroadcastCommand() {

        new CommandAPICommand("stuff:broadcast")
                .withPermission(conf.getString("BroadcastCommand.Permission"))
                .withAliases(conf.getStringList("BroadcastCommand.Aliases").toArray(num -> new String[num]))
                .withArguments(new GreedyStringArgument(conf.getString("BroadcastCommand.suggestionName.Message")))
                .executes(((sender, args) -> {
                    Component message = mm.deserialize(conf.getString("BroadcastCommand.Message"),
                            Placeholder.component("message",
                                    mm.deserialize((String) args.get(
                                            conf.getString("BroadcastCommand.suggestionName.Message"))
                                    )));
                    stuff.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(message));
                }))
                .register();

    }

}
