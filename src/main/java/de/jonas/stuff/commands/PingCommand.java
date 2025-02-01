package de.jonas.stuff.commands;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.gaminglounge.configapi.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class PingCommand {

    MiniMessage mm = MiniMessage.miniMessage();
    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();
    String suggestion = conf.getString("MsgCommand.suggestionName.Player");
    List<String> aliases = conf.getStringList("PingCommand.Aliases");

    public PingCommand() {

        new CommandAPICommand("stuff:ping")
                .withAliases(aliases.toArray(new String[aliases.size()]))
                .withOptionalArguments(new PlayerArgument(suggestion))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get(suggestion);

                    if (target == null) target = player;
                    int ping = target.getPing();

                    String message;
                    if (target == player) {
                        message = Language.getValue(Stuff.INSTANCE, target, "ping.self", true);
                    } else {
                        message = Language.getValue(Stuff.INSTANCE, target, "ping.other", true);
                    }

                    player.sendMessage(mm.deserialize(message,
                        Placeholder.component("ping", Component.text(ping)),
                        Placeholder.component("player", target.teamDisplayName())
                        ));
                })
                .register();

    }
}
