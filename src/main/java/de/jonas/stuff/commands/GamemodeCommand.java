package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeCommand {
    public GamemodeCommand() {
        CommandAPI.unregister("gamemode");
        // Create our command
        new CommandAPICommand("gamemode")
                .withAliases("gm")
                .withSubcommand(new CommandAPICommand("creative")
                        .withAliases("1")
                        .withPermission("stuff.gamemode.creative")
                        .withOptionalArguments(new EntitySelectorArgument.OneEntity("Spieler"))
                        .executesPlayer((player, args) -> {
                            Player target = (Player) args.get("Spieler");
                            Stuff stuff = new Stuff();
                            var mm = MiniMessage.miniMessage();

                            if (target == null) target = player;

                            target.setGameMode(GameMode.CREATIVE);

                            if (player == target) {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeCreative")));
                            } else {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("OtherOtherGamemodeCreative"),
                                        Placeholder.component("player", player.teamDisplayName())));
                                player.sendMessage(mm.deserialize(stuff.getConfig().getString("SelfOtherGamemodeCreative"),
                                        Placeholder.component("player", target.teamDisplayName())));
                            }
                        })
                ).withSubcommand(new CommandAPICommand("spectator")
                        .withAliases("3")
                        .withPermission("stuff.gamemode.spectator")
                        .withOptionalArguments(new EntitySelectorArgument.OneEntity("Spieler"))
                        .executesPlayer((player, args) -> {
                            Player target = (Player) args.get("Spieler");
                            Stuff stuff = new Stuff();
                            var mm = MiniMessage.miniMessage();

                            if (target == null) target = player;

                            target.setGameMode(GameMode.SPECTATOR);

                            if (player == target) {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeSpectator")));
                            } else {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("OtherOtherGamemodeSpectator"),
                                        Placeholder.component("player", player.teamDisplayName())));
                                player.sendMessage(mm.deserialize(stuff.getConfig().getString("SelfOtherGamemodeSpectator"),
                                        Placeholder.component("player", target.teamDisplayName())));
                            }
                        })
                ).withSubcommand(new CommandAPICommand("adventure")
                        .withAliases("2")
                        .withPermission("stuff.gamemode.adventure")
                        .withOptionalArguments(new EntitySelectorArgument.OneEntity("Spieler"))
                        .executesPlayer((player, args) -> {
                            Player target = (Player) args.get("Spieler");
                            Stuff stuff = new Stuff();
                            var mm = MiniMessage.miniMessage();

                            if (target == null) target = player;

                            target.setGameMode(GameMode.ADVENTURE);

                            if (player == target) {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeAdventure")));
                            } else {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("OtherOtherGamemodeAdventure"),
                                        Placeholder.component("player", player.teamDisplayName())));
                                player.sendMessage(mm.deserialize(stuff.getConfig().getString("SelfOtherGamemodeAdventure"),
                                        Placeholder.component("player", target.teamDisplayName())));
                            }
                        })
                ).withSubcommand(new CommandAPICommand("survival")
                        .withAliases("0")
                        .withPermission("stuff.gamemode.survival")
                        .withOptionalArguments(new EntitySelectorArgument.OneEntity("Spieler"))
                        .executesPlayer((player, args) -> {
                            Player target = (Player) args.get("Spieler");
                            Stuff stuff = new Stuff();
                            var mm = MiniMessage.miniMessage();

                            if (target == null) target = player;

                            target.setGameMode(GameMode.SURVIVAL);

                            if (player == target) {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("GamemodeSurvival")));
                            } else {
                                target.sendMessage(mm.deserialize(stuff.getConfig().getString("OtherOtherGamemodeSurvival"),
                                        Placeholder.component("player", player.teamDisplayName())));
                                player.sendMessage(mm.deserialize(stuff.getConfig().getString("SelfOtherGamemodeSurvival"),
                                        Placeholder.component("player", target.teamDisplayName())));
                            }
                        })
                )
                .register();
    }
}
