package de.jonas.stuff.commands;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ReloadCommand {

    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();
    MiniMessage mm = MiniMessage.miniMessage();

    public ReloadCommand() {
        new CommandAPICommand("Stuff:reload")
                .withAliases("stuff:reload")
                .withPermission(conf.getString("ReloadCommand.permission"))
                    .withSubcommand(new CommandAPICommand("TeamDisplayName")
                            .executes(((commandSender, args) -> {
                                Bukkit.getOnlinePlayers().forEach(pl -> {
                                    stuff.teamDisplaynameSet.teams.remove(pl);
                                    stuff.teamDisplaynameSet.removeTeam(stuff.teamDisplaynameSet.scoreboard, pl);
                                    Bukkit.getOnlinePlayers().forEach(pl2 -> stuff.teamDisplaynameSet.
                                            removeTeam(pl2.getScoreboard(), pl));

                                    stuff.teamDisplaynameSet.onJoinPlayer(pl);
                                    commandSender.sendMessage(mm.deserialize("[Stuff] Player names successfully reloaded"));
                                });
                            }))
                    )
                    .withSubcommand(new CommandAPICommand("config")
                        .executes(((commandSender, commandArguments) -> {
                            stuff.reloadConfig();
                            commandSender.sendMessage(mm.deserialize("[Stuff] Config successfully reloaded"));
                        })))
                    .withSubcommand(new CommandAPICommand("timers")
                        .executes(((commandSender, commandArguments) -> {
                            stuff.reloadConfig();
                            stuff.timerHandler.loadConfig();
                            commandSender.sendMessage(mm.deserialize("[Stuff] Timers successfully reloaded"));
                        })))
                .register();
    }



}
