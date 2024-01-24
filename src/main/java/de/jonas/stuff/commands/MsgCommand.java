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
        CommandAPI.unregister("msg");
        CommandAPI.unregister("w");
        CommandAPI.unregister("tell");

        // Create our command
        new CommandAPICommand("msg")
                .withAliases("message", "nachricht", "wisper", "w", "tell")
                .withArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
                .withArguments(new GreedyStringArgument("Nachricht"))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get("Spieler");
                    Stuff stuff = new Stuff();
                    var mm = MiniMessage.miniMessage();

                    assert target != null;
                    Component prefixto = mm.deserialize(Objects.requireNonNull(stuff.getConfig().getString("MSGFormatTo")),
                            Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component("toplayer", target.teamDisplayName()),
                            Placeholder.component("message", Component.text((String) args.get("Nachricht"))));
                    Component prefixfrom = mm.deserialize(Objects.requireNonNull(stuff.getConfig().getString("MSGFormatFrom")),
                            Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component("toplayer", target.teamDisplayName()),
                            Placeholder.component("message", Component.text((String) args.get("Nachricht"))));

                    target.sendMessage(prefixto);
                    player.sendMessage(prefixfrom);
                })
                .register();
    }
}
