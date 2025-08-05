package de.jonas.stuff.commands;

import org.bukkit.configuration.file.FileConfiguration;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class BroadcastCommand {

    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();
    MiniMessage mm = MiniMessage.miniMessage();

    public BroadcastCommand() {

        new CommandAPICommand("stuff:broadcast")
                .withPermission(conf.getString("BroadcastCommand.Permission"))
                .withAliases(conf.getStringList("BroadcastCommand.Aliases").toArray(num -> new String[num]))
                .withArguments(new GreedyStringArgument("message"))
                .executes(((sender, args) -> {
                    Component message = mm.deserialize(conf.getString("BroadcastCommand.Message"),
                            Placeholder.component("message", mm.deserialize((String) args.get(
                                "message")
                                )));
                    stuff.getServer().getOnlinePlayers().forEach(player -> player.sendMessage(message));
                }))
                .register();

    }

}
