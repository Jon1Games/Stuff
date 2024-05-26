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
            .executes((executor, args) -> {
                Player target = (Player) args.get("Spieler");

                if (executor instanceof Player && target == null) target = (Player) executor;

                var a = !target.getAllowFlight();
                target.getPersistentDataContainer().set(flyAllowidentifier, PersistentDataType.BOOLEAN, a);

                target.setAllowFlight(a);

                if (target == executor || !(executor instanceof Player)) {
                    if (a) {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Self.EnterFlyMode")));
                    } else target.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Self.ExitFlyMode")));
                } else {
                    if (a) {
                        Player executorP = (Player) executor;
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Other.Other.EnterFlyMode"), Placeholder.component("player", executorP.teamDisplayName())));
                        executor.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Other.Self.EnterFlyMode"), Placeholder.component("player", target.teamDisplayName())));
                    } else {
                        Player executorP = (Player) executor;
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Other.Other.ExitFlyMode"), Placeholder.component("player", executorP.teamDisplayName())));
                        executor.sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Other.Self.ExitFlyMode"), Placeholder.component("player", target.teamDisplayName())));
                    }
                }
    })
            .register();
}
}
