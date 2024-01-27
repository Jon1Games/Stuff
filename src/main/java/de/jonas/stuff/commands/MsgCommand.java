package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MsgCommand {

    public MsgCommand() {
        Stuff stuff = Stuff.INSTANCE;
        var mm = MiniMessage.miniMessage();

        CommandAPI.unregister("msg");
        CommandAPI.unregister("w");
        CommandAPI.unregister("tell");

        // Create our command
        new CommandAPICommand("msg")
                .withAliases("message", "nachricht", "wisper", "w", "tell")
                .withArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
                .withArguments(new GreedyStringArgument(stuff.getConfig().getString("MsgCommand.ArgumentName")))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get("Spieler");

                    assert target != null;
                    Component prefixto = mm.deserialize(Objects.requireNonNull(stuff.getConfig().getString("MsgCommand.MessageFormat.To")),
                            Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component("toplayer", target.teamDisplayName()),
                            Placeholder.component("message", Component.text((String) args.get(stuff.getConfig().getString("MsgCommand.ArgumentName")))));
                    Component prefixfrom = mm.deserialize(Objects.requireNonNull(stuff.getConfig().getString("MsgCommand.MessageFormat.From")),
                            Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component("toplayer", target.teamDisplayName()),
                            Placeholder.component("message", Component.text((String) args.get(stuff.getConfig().getString("MsgCommand.ArgumentName")))));

                    target.sendMessage(prefixto);
                    player.sendMessage(prefixfrom);
                })
                .register();
    }
}
