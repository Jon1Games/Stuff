package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class GamemodeCommand {
    public GamemodeCommand() {

        Stuff stuff = Stuff.INSTANCE;
        var mm = MiniMessage.miniMessage();

        CommandAPI.unregister("gamemode");
        // Create our command
        new CommandAPICommand("gamemode")
                .withAliases("gm")
                .withSubcommand(new CommandAPICommand("creative")
                        .withAliases("1")
                        .withPermission(stuff.getConfig().getString("GamemodeCommand.messages.Creative.Permission"))
                        .withOptionalArguments(new PlayerArgument("Spieler"))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get("Spieler");

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.setGameMode(GameMode.CREATIVE);
                            target.setFlying(true);
                            target.setAllowFlight(true);

                            if (executor == target || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Creative.Self")));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Creative.Other.Other"),
                                        Placeholder.component("player", executorP.teamDisplayName())));
                                executor.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Creative.Other.Self"),
                                        Placeholder.component("player", target.teamDisplayName())));
                            }
                        })
                ).withSubcommand(new CommandAPICommand("spectator")
                        .withAliases("3")
                        .withPermission(stuff.getConfig().getString("GamemodeCommand.messages.Spectator.Permission"))
                        .withOptionalArguments(new PlayerArgument("Spieler"))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get("Spieler");

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.setGameMode(GameMode.SPECTATOR);
                            target.setFlying(true);
                            target.setAllowFlight(true);


                            if (executor == target || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Spectator.Self")));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Spectator.Other.Other"),
                                        Placeholder.component("player", executorP.teamDisplayName())));
                                executor.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Spectator.Other.Self"),
                                        Placeholder.component("player", target.teamDisplayName())));
                            }
                        })
                ).withSubcommand(new CommandAPICommand("adventure")
                        .withAliases("2")
                        .withPermission(stuff.getConfig().getString("GamemodeCommand.messages.Adventure.Permission"))
                        .withOptionalArguments(new PlayerArgument("Spieler"))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get("Spieler");

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.setGameMode(GameMode.ADVENTURE);
                            if (target.getPersistentDataContainer().get(FlyCommand.flyAllowidentifier, PersistentDataType.BOOLEAN) == true) {
                                target.setAllowFlight(true);
                                target.setFlying(true);
                            }

                            if (executor == target || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Adventure.Self")));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Adventure.Other.Other"),
                                        Placeholder.component("player", executorP.teamDisplayName())));
                                executor.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Adventure.Other.Self"),
                                        Placeholder.component("player", target.teamDisplayName())));
                            }
                        })
                ).withSubcommand(new CommandAPICommand("survival")
                        .withAliases("0")
                        .withPermission(stuff.getConfig().getString("GamemodeCommand.messages.Survival.Permission"))
                        .withOptionalArguments(new PlayerArgument("Spieler"))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get("Spieler");

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.setGameMode(GameMode.SURVIVAL);
                            if (target.getPersistentDataContainer().get(FlyCommand.flyAllowidentifier, PersistentDataType.BOOLEAN) == true) {
                                target.setAllowFlight(true);
                                target.setFlying(true);
                            }

                            if (executor == target || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Survival.Self")));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Survival.Other.Other"),
                                        Placeholder.component("player", executorP.teamDisplayName())));
                                executor.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCommand.messages.Survival.Other.Self"),
                                        Placeholder.component("player", target.teamDisplayName())));
                            }
                        })
                )
                .register();
    }
}
