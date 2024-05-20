package de.jonas.stuff.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.persistence.PersistentDataType;

public class AdminCommand {

    MiniMessage mm = MiniMessage.miniMessage();

    public AdminCommand() {
        new CommandAPICommand("Stuff:admincommand")
                .withAliases("stuff:admincommand", "admincommand")
                .withPermission(CommandPermission.OP)
                .withSubcommand(new CommandAPICommand("testFlyBoolen")
                        .executesPlayer((player, args) -> {
                            boolean state = false;
                            String stateM;
                            if (player.getPersistentDataContainer().has(FlyCommand.flyAllowidentifier)) {
                                state = player.getPersistentDataContainer().get(FlyCommand.flyAllowidentifier,
                                        PersistentDataType.BOOLEAN);
                            }

                            if (state) {
                                stateM = "true";
                            } else {
                                stateM = "false";
                            }
                            Component message = mm.deserialize("Dein Flugmodus ist <state>.",
                                    Placeholder.component("state", Component.text(stateM)));

                            player.sendMessage(message);
                        })
                )
                .register();
    }
}
