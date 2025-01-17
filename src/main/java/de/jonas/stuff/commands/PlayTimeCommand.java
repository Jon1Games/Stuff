package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.gaminglounge.configapi.Language;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.Component;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayTimeCommand {
    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();
    String suggestion = conf.getString("PlayTimeCommand.suggestionName.Player");
    List<String> aliases = conf.getStringList("PlayTimeCommand.Aliases");

    public PlayTimeCommand() {

        new CommandAPICommand("stuff:playtime")
                .withAliases(aliases.toArray(new String[aliases.size()]))
                .withOptionalArguments(new PlayerArgument(suggestion))
                .executesPlayer(((player, commandArguments) -> {
                    Player target = (Player) commandArguments.get(suggestion);
                    if (target == null) target = player;

                    target.sendMessage(calculateTime(target.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20,
                            target, player));

                }))
                .register();

    }

    public static Component calculateTime(long seconds, Player player, Player executor) {
        MiniMessage mm = MiniMessage.miniMessage();
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24L);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        String m;
        if (player == executor) {
            m = Language.getValue(Stuff.INSTANCE, executor, "playtime.self", true);
        } else {
            m = Language.getValue(Stuff.INSTANCE, executor, "playtime.other", true);
        }
        if (m == null) return mm.deserialize("<red>Error</red>");

        return mm.deserialize(m,
            Placeholder.component("days", Component.text(day)), Placeholder.component("hours", Component.text(hours)),
            Placeholder.component("min", Component.text(minute)), Placeholder.component("sec", Component.text(second)));

    }

}
