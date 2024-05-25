package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlayTimeCommand {

    public PlayTimeCommand() {

        new CommandAPICommand("stuff:playtime")
                .withOptionalArguments(new PlayerArgument("Spieler"))
                .executesPlayer(((player, commandArguments) -> {
                    Player target = (Player) commandArguments.get("Spieler");
                    if (target == null) target = player;

                    target.sendMessage(calculateTime(target.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20,
                            target, player));

                }))
                .register();

    }

    public static Component calculateTime(long seconds, Player player, Player executor) {
        MiniMessage mm = MiniMessage.miniMessage();
        Stuff stuff = Stuff.INSTANCE;
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24L);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        String m;
        if (player == executor) {
            m = stuff.getConfig().getString("PlayTimeCommand.Messages.self");
        } else {
            m = stuff.getConfig().getString("PlayTimeCommand.Messages.other");
        }
        if (m == null) return mm.deserialize("<red>Error");

        return mm.deserialize(m,
            Placeholder.component("days", Component.text(day)), Placeholder.component("hours", Component.text(hours)),
            Placeholder.component("min", Component.text(minute)), Placeholder.component("sec", Component.text(second)));

    }

}
