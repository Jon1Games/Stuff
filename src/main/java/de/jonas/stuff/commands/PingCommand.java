package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public class PingCommand {

    MiniMessage mm = MiniMessage.miniMessage();
    Stuff stuff = Stuff.INSTANCE;

    public PingCommand() {

        new CommandAPICommand("Stuff:ping")
                .withAliases("stuff:ping", "ping")
                .withOptionalArguments(new PlayerArgument("Spieler"))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get("Spieler");

                    if (target == null) target = player;
                    int ping = target.getPing();

                    String message;
                    if (target == player) {
                        message = stuff.getConfig().getString("PingCommand.Messages.self");
                    } else {
                        message = stuff.getConfig().getString("PingCommand.Messages.other");
                    }

                    player.sendMessage(mm.deserialize(message,
                            Placeholder.component("ping", Component.text(ping)),
                            Placeholder.component("player", target.teamDisplayName())
                            ));
                })
                .register();

    }
}
