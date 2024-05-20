package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class SpeedCommand {
    public static final NamespacedKey flySpeedIdentifier = new NamespacedKey("stuff", "fly_speed");
    public static final NamespacedKey walkSpeedIdentifier = new NamespacedKey("stuff", "walk_speed");
    public SpeedCommand() {
        Stuff stuff = Stuff.INSTANCE;
        var mm = MiniMessage.miniMessage();

        new CommandAPICommand("flySpeed")
                .withPermission(stuff.getConfig().getString("SpeedCommand.Permission"))
                .withAliases("flugGeschwindigkeit", "stuff:flyspeed", "Stuff:walkSpeed")
                .withArguments(new IntegerArgument("Geschwindigkeit", -10 ,10))
                .withOptionalArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get("Spieler");
                    float speed = (int) args.get("Geschwindigkeit") / 10.0f;

                    if (target == null) target = player;

                    target.getPersistentDataContainer().set(flySpeedIdentifier, PersistentDataType.FLOAT, speed);

                    target.setFlySpeed(speed);

                    int fullspeed = (int) (speed * 10);

                    if (target == player) {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("SpeedCommand.Messages.Self.NewSpeed"), Placeholder.component("speed", Component.text(fullspeed))));
                    } else {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("SpeedCommand.Messages.Other.Other.NewSpeed"), Placeholder.component("player", player.teamDisplayName()), Placeholder.component("speed", Component.text(fullspeed))));
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("SpeedCommand.Messages.Other.Self.NewSpeed"), Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed", Component.text(fullspeed))));
                    }
                })
                .register();

            new CommandAPICommand("walkSpeed")
                    .withPermission(stuff.getConfig().getString("SpeedCommand.Permission"))
                    .withAliases("laufGeschwindigkeit", "stuff:walkSpeed", "Stuff:walkSpeed")
                    .withArguments(new IntegerArgument("Geschwindigkeit", -10 ,10))
                    .withOptionalArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
                    .executesPlayer((player, args) -> {
                        Player target = (Player) args.get("Spieler");
                        float speed = (int) args.get("Geschwindigkeit") / 10.0f;

                        if (target == null) target = player;

                        target.getPersistentDataContainer().set(walkSpeedIdentifier, PersistentDataType.FLOAT, speed);

                        target.setWalkSpeed(speed);

                        int fullspeed = (int) (speed * 10);

                        if (target == player) {
                            target.sendMessage(mm.deserialize(stuff.getConfig().getString("SpeedCommand.Messages.Self.NewSpeed"), Placeholder.component("speed", Component.text(fullspeed))));
                        } else {
                            target.sendMessage(mm.deserialize(stuff.getConfig().getString("SpeedCommand.Messages.Other.Other.NewSpeed"), Placeholder.component("player", player.teamDisplayName()), Placeholder.component("speed", Component.text(fullspeed))));
                            player.sendMessage(mm.deserialize(stuff.getConfig().getString("SpeedCommand.Messages.Other.Self.NewSpeed"), Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed", Component.text(fullspeed))));
                        }
                    })
                    .register();
    }
}
