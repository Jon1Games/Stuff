package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;

public class ReloadCommand {
    Stuff stuff = Stuff.INSTANCE;

    public ReloadCommand() {
        new CommandAPICommand("Stuff:Reload")
                .withAliases("Stuff:reload", "stuff:Reload", "stuff:reload")
                    .withSubcommand(new CommandAPICommand("TeamDisplayName")
                            .executes(((commandSender, args) -> {
                                Bukkit.getOnlinePlayers().forEach(pl -> {
                                    stuff.teamDisplaynameSet.teams.remove(pl);
                                    stuff.teamDisplaynameSet.removeTeam(stuff.teamDisplaynameSet.scoreboard, pl);
                                    Bukkit.getOnlinePlayers().forEach(pl2 -> stuff.teamDisplaynameSet.
                                            removeTeam(pl2.getScoreboard(), pl));

                                    stuff.teamDisplaynameSet.onJoinPlayer(pl);
                                });
                            }))
                    )
                    .withSubcommand(new CommandAPICommand("config")
                            .executes(((commandSender, commandArguments) -> {
                                stuff.reloadConfig();
                            }))
                )
                .register();
    }



}
