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
        // Create our command
        new CommandAPICommand("fly")
            .withOptionalArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
            .withPermission("stuff.fly")
            .executesPlayer((player, args) -> {
                Player target = (Player) args.get("Spieler");
                Stuff stuff = new Stuff();
                var mm = MiniMessage.miniMessage();

                if (target == null) target = player;

                var a = !target.getAllowFlight();
                target.getPersistentDataContainer().set(flyAllowidentifier, PersistentDataType.BOOLEAN, a);

                target.setAllowFlight(a);

                if (target == player) {
                    if (a) {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("EnterFlyMode")));
                    } else target.sendMessage(mm.deserialize(stuff.getConfig().getString("ExitFlyMode")));
                } else {
                    if (a) {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("OtherOtherEnterFlyMode"), Placeholder.component("player", player.teamDisplayName())));
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("SelfOtherEnterFlyMode"), Placeholder.component("player", target.teamDisplayName())));
                    } else {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("OtherOtherExitFlyMode"), Placeholder.component("player", player.teamDisplayName())));
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("SelfOtherExitFlyMode"), Placeholder.component("player", target.teamDisplayName())));
                    }
                }
    })
            .register();
}
}
