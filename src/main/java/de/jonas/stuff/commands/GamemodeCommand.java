package de.jonas.stuff.commands;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.gaminglounge.configapi.Language;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class GamemodeCommand {
    public GamemodeCommand() {

        Stuff stuff = Stuff.INSTANCE;
        FileConfiguration conf = stuff.getConfig();
        String suggestion = conf.getString("GamemodeCommand.suggestionName.Player");
        List<String> aliases = conf.getStringList("GamemodeCommand.Aliases");
        List<String> aliasesC = conf.getStringList("GamemodeCommand.Messages.Creative.Aliases");
        List<String> aliasesS = conf.getStringList("GamemodeCommand.Messages.Spectator.Aliases");
        List<String> aliasesA = conf.getStringList("GamemodeCommand.Messages.Adventure.Aliases");
        List<String> aliasesSu = conf.getStringList("GamemodeCommand.Messages.Survival.Aliases");
        var mm = MiniMessage.miniMessage();

        CommandAPI.unregister("gamemode");
        // Create our command
        new CommandAPICommand("gamemode")
                .withAliases(aliases.toArray(new String[aliases.size()]))
                .withSubcommand(new CommandAPICommand("creative")
                        .withAliases(aliasesC.toArray(new String[aliasesC.size()]))
                        .withPermission(conf.getString("GamemodeCommand.Messages.Creative.Permission"))
                        .withOptionalArguments(new PlayerArgument(suggestion))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get(suggestion);

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.setGameMode(GameMode.CREATIVE);
                            target.setFlying(true);
                            target.setAllowFlight(true);

                            if (executor == target || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(Language.getValue(stuff, target, "gamemodecommand.creative.self", true)));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(Language.getValue(stuff, target, "gamemodecommand.creative.other.other", true),
                                        Placeholder.component("player", executorP.teamDisplayName())));
                                if (executor instanceof Player p) {
                                    executor.sendMessage(mm.deserialize(Language.getValue(stuff, p, "gamemodecommand.creative.other.self", true),
                                        Placeholder.component("player", target.teamDisplayName())));
                                } else {
                                    executor.sendMessage(mm.deserialize(Language.getValue(stuff, "en_US", "gamemodecommand.creative.other.self", true),
                                        Placeholder.component("player", target.teamDisplayName())));
                                }

                            }
                        })
                ).withSubcommand(new CommandAPICommand("spectator")
                        .withAliases(aliasesS.toArray(new String[aliasesS.size()]))
                        .withPermission(conf.getString("GamemodeCommand.Messages.Spectator.Permission"))
                        .withOptionalArguments(new PlayerArgument(suggestion))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get(suggestion);

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.setGameMode(GameMode.SPECTATOR);
                            target.setFlying(true);
                            target.setAllowFlight(true);


                            if (executor == target || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(Language.getValue(stuff, target, "gamemodecommand.spectator.self", true)));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(Language.getValue(stuff, target, "gamemodecommand.spectator.other.other", true),
                                        Placeholder.component("player", executorP.teamDisplayName())));
                                if (executor instanceof Player p) {
                                    executor.sendMessage(mm.deserialize(Language.getValue(stuff, p, "gamemodecommand.spectator.other.self", true),
                                        Placeholder.component("player", target.teamDisplayName())));
                                } else {
                                    executor.sendMessage(mm.deserialize(Language.getValue(stuff, "en_US", "gamemodecommand.spectator.other.self", true),
                                        Placeholder.component("player", target.teamDisplayName())));                                    
                                }
                            }
                        })
                ).withSubcommand(new CommandAPICommand("adventure")
                        .withAliases(aliasesA.toArray(new String[aliasesA.size()]))
                        .withPermission(conf.getString("GamemodeCommand.Messages.Adventure.Permission"))
                        .withOptionalArguments(new PlayerArgument(suggestion))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get(suggestion);

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.setGameMode(GameMode.ADVENTURE);
                            if (target.getPersistentDataContainer().has(FlyCommand.flyAllowidentifier) &&
                                target.getPersistentDataContainer().get(FlyCommand.flyAllowidentifier, PersistentDataType.BOOLEAN) == true) {
                                target.setAllowFlight(true);
                                target.setFlying(true);
                            }

                            if (executor == target || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(Language.getValue(stuff, target, "gamemodecommand.adventure.self", true)));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(Language.getValue(stuff, target, "gamemodecommand.adventure.other.other", true),
                                        Placeholder.component("player", executorP.teamDisplayName())));
                                if (executor instanceof Player p) {
                                    executor.sendMessage(mm.deserialize(Language.getValue(stuff, p, "gamemodecommand.adventure.other.self", true),
                                            Placeholder.component("player", target.teamDisplayName())));
                                } else {
                                    executor.sendMessage(mm.deserialize(Language.getValue(stuff, "en_US", "gamemodecommand.adventure.other.self", true),
                                            Placeholder.component("player", target.teamDisplayName())));                                    
                                }
                            }
                        })
                ).withSubcommand(new CommandAPICommand("survival")
                        .withAliases(aliasesSu.toArray(new String[aliasesSu.size()]))
                        .withPermission(conf.getString("GamemodeCommand.Messages.Survival.Permission"))
                        .withOptionalArguments(new PlayerArgument(suggestion))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get(suggestion);

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.setGameMode(GameMode.SURVIVAL);
                            if (target.getPersistentDataContainer().has(FlyCommand.flyAllowidentifier) &&
                                target.getPersistentDataContainer().get(FlyCommand.flyAllowidentifier, PersistentDataType.BOOLEAN) == true) {
                                target.setAllowFlight(true);
                                target.setFlying(true);
                            }

                            if (executor == target || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(Language.getValue(stuff, target, "gamemodecommand.survival.self", true)));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(Language.getValue(stuff, target, "gamemodecommand.survival.other.other", true),
                                        Placeholder.component("player", executorP.teamDisplayName())));
                                if (executor instanceof Player p) {
                                    executor.sendMessage(mm.deserialize(Language.getValue(stuff, p, "gamemodecommand.survival.other.self", true),
                                            Placeholder.component("player", target.teamDisplayName())));
                                } else {
                                    executor.sendMessage(mm.deserialize(Language.getValue(stuff, "en_US", "gamemodecommand.survival.other.self", true),
                                        Placeholder.component("player", target.teamDisplayName())));
                                }
                            }
                        })
                )
                .register();
    }
}
