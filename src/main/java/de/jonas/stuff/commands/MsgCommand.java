package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class MsgCommand {

    public MsgCommand() {
        Stuff stuff = Stuff.INSTANCE;
        FileConfiguration conf = stuff.getConfig();
        String suggestion = conf.getString("MsgCommand.suggestionName.Player");
        List<String> aliases = conf.getStringList("MsgCommand.Aliases");
        var mm = MiniMessage.miniMessage();

        for (String a : conf.getStringList("MsgCommand.unregister")) {
            CommandAPI.unregister(a);
        }

        new CommandAPICommand("stuff:msg")
                .withAliases(aliases.toArray(new String[aliases.size()]))
                .withArguments(new PlayerArgument(suggestion))
                .withArguments(new GreedyStringArgument(conf.getString("MsgCommand.suggestionName.Message")))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get(suggestion);

                    if (player == target) {
                        player.sendMessage(mm.deserialize(conf.getString("MsgCommand.Messages.same")));
                        return;
                    }

                    assert target != null;
                    Component prefixto = mm.deserialize(Objects.requireNonNull(stuff.getConfig().getString
                                    ("MsgCommand.Messages.To")),
                            Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component
                                    ("toplayer", target.teamDisplayName()),
                            Placeholder.component("message", Component.text((String) args.get(stuff.getConfig().
                                    getString("MsgCommand.ArgumentName")))));
                    Component prefixfrom = mm.deserialize(Objects.requireNonNull(stuff.getConfig().getString
                                    ("MsgCommand.Messages.From")),
                            Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component
                                    ("toplayer", target.teamDisplayName()),
                            Placeholder.component("message", Component.text((String) args.get(stuff.getConfig()
                                    .getString("MsgCommand.ArgumentName")))));

                    target.sendMessage(prefixto);
                    player.sendMessage(prefixfrom);
                })
                .register();
    }
}
