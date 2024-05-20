package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class FlyCommand {
    public static final NamespacedKey flyAllowidentifier = new NamespacedKey("stuff", "fly_allow");
    public FlyCommand() {
        Stuff stuff = Stuff.INSTANCE;
        var mm = MiniMessage.miniMessage();

        // Create our command
        new CommandAPICommand("fly")
            .withAliases("stuff:fly", "Stuff:fly")
            .withOptionalArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
            .withPermission(stuff.getConfig().getString("FlyCommand.Permission"))
            .executesPlayer((player, args) -> {
                Player target = (Player) args.get("Spieler");

                if (target == null) target = player;

                var a = !target.getAllowFlight();
                target.getPersistentDataContainer().set(flyAllowidentifier, PersistentDataType.BOOLEAN, a);

                target.setAllowFlight(a);

                if (target == player) {
                    if (a) {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Self.EnterFlyMode")));
                    } else target.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Self.ExitFlyMode")));
                } else {
                    if (a) {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Other.Other.EnterFlyMode"), Placeholder.component("player", player.teamDisplayName())));
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Other.Self.EnterFlyMode"), Placeholder.component("player", target.teamDisplayName())));
                    } else {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Other.Other.ExitFlyMode"), Placeholder.component("player", player.teamDisplayName())));
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Other.Self.ExitFlyMode"), Placeholder.component("player", target.teamDisplayName())));
                    }
                }
    })
            .register();
}
}
