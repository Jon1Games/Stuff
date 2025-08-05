package de.jonas.stuff.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.gaminglounge.configapi.Language;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class MsgCommand implements Listener{

    Map<Player, Player> a = new HashMap<>();

    public MsgCommand() {
        Stuff stuff = Stuff.INSTANCE;
        FileConfiguration conf = stuff.getConfig();
        String suggestion = "player";
        List<String> aliases_MSG = conf.getStringList("MsgCommand.Aliases");
        List<String> aliases_ANSWER = conf.getStringList("MsgCommand.AnswerCommand.Aliases");
        var mm = MiniMessage.miniMessage();

        for (String a : conf.getStringList("MsgCommand.unregister")) {
            CommandAPI.unregister(a);
        }

        new CommandAPICommand("stuff:msg")
            .withPermission(stuff.getConfig().getString("MsgCommand.Permission"))
                .withAliases(aliases_MSG.toArray(new String[aliases_MSG.size()]))
                .withArguments(new PlayerArgument(suggestion))
                .withArguments(new GreedyStringArgument("message"))
                .executesPlayer((player, args) -> {
                    Player target = (Player) args.get(suggestion);

                    if (player == target) {
                        player.sendMessage(mm.deserialize(Language.getValue(stuff, player, "messageSelf", true)));
                        return;
                    }

                    if (target == null) {
                        return;
                    }
                    if (!target.isOnline()) {
                        player.sendMessage(mm.deserialize(Language.getValue(stuff, player, "playerOffline"),
                            Placeholder.component("player", mm.deserialize(String.valueOf(target)))
                        ));
                        return;
                    }

                    a.put(player, target);
                    a.put(target, player);

                    Component prefixto = mm.deserialize(Objects.requireNonNull(Language.getValue(stuff, target, "msg.to")),
                            Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component
                                    ("toplayer", target.teamDisplayName()),
                            Placeholder.component("message", Component.text((String) args.get("message"))));
                    Component prefixfrom = mm.deserialize(Objects.requireNonNull(Language.getValue(stuff, player, "msg.from")),
                            Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component
                                    ("toplayer", target.teamDisplayName()),
                            Placeholder.component("message", Component.text((String) args.get("message"))));

                    target.sendMessage(prefixto);
                    player.sendMessage(prefixfrom);
                })
                .register();

        new CommandAPICommand("stuff:msganswer")
            .withPermission(stuff.getConfig().getString("MsgCommand.AnswerCommand.Permission"))
            .withAliases(aliases_ANSWER.toArray(new String[aliases_ANSWER.size()]))
            .withArguments(new GreedyStringArgument("message"))
            .executesPlayer((player, args) -> {

                Player target = a.get(player);
                if (target == null) {
                    player.sendMessage(mm.deserialize(stuff.getConfig().getString("MsgCommand.Messages.NoAnswerPlayer")));
                    return;
                }
                if (!target.isOnline()) {
                    player.sendMessage(mm.deserialize(stuff.getConfig().getString("MsgCommand.Messages.PlayerOffline"),
                        Placeholder.component("player", mm.deserialize(String.valueOf(target)))
                    ));
                    return;
                }

                a.put(target, player);

                    Component prefixto = mm.deserialize(Objects.requireNonNull(Language.getValue(stuff, target, "msg.to")),
                        Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component
                                ("toplayer", target.teamDisplayName()),
                        Placeholder.component("message", Component.text((String) args.get("message"))));
                    Component prefixfrom = mm.deserialize(Objects.requireNonNull(Language.getValue(stuff, player, "msg.from")),
                        Placeholder.component("fromplayer", player.teamDisplayName()), Placeholder.component
                                ("toplayer", target.teamDisplayName()),
                        Placeholder.component("message", Component.text((String) args.get("message"))));

                target.sendMessage(prefixto);
                player.sendMessage(prefixfrom);
            })
        .register();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        if(a.containsKey(e.getPlayer())) a.remove(e.getPlayer());
    }

}
