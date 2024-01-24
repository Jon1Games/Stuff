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
    public static final NamespacedKey speedIdentifier = new NamespacedKey("stuff", "speed");
    public SpeedCommand() {
        // Create our command
        new CommandAPICommand("speed")
                .withAliases("FlySpeed", "WalkSpeed","Geschwindigkeit")
                .withArguments(new IntegerArgument("Geschwindigkeit", -10 ,10))
                .withOptionalArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
                .withPermission("stuff.speed")
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get("Spieler");
                    float speed = (int) args.get("Geschwindigkeit") / 10.0f;
                    Stuff stuff = new Stuff();
                    var mm = MiniMessage.miniMessage();

                    if (target == null) target = player;

                    target.getPersistentDataContainer().set(speedIdentifier, PersistentDataType.FLOAT, speed);

                    target.setFlySpeed(speed);
                    target.setWalkSpeed(speed);

                    int fullspeed = (int) (speed * 10);

                    if (target == player) {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("NewSpeed"), Placeholder.component("speed", Component.text(fullspeed))));
                    } else {
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("OtherOtherNewSpeed"), Placeholder.component("player", player.teamDisplayName()), Placeholder.component("speed", Component.text(fullspeed))));
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("SelfOtherNewSpeed"), Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed", Component.text(fullspeed))));
                    }
                })
                .register();
    }
}
