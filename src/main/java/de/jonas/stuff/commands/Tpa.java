package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Tpa {

    MiniMessage mm = MiniMessage.miniMessage();
    Stuff stuff = Stuff.INSTANCE;

    public Map<Player, Map<Player, Long>> a = new HashMap<>();
    Map<Player, Boolean> cancelTeleport = stuff.cancelTeleport.cancelTeleportMap;

    public Tpa() {

        new CommandAPICommand("tpa")
                .withArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
                .executesPlayer((player, args) -> {
                    Player reciver = (Player) args.get("Spieler");
                    tpa(player, reciver, System.currentTimeMillis() + 60 + 1_000);
                })
                .register();

        new CommandAPICommand("tpaccept")
                .withArguments(new EntitySelectorArgument.OnePlayer("Spieler"))
                .executesPlayer((player, args) -> {
                    Player sender = (Player) args.get("Spieler");
                    tpaccept(player, sender, System.currentTimeMillis() + 60 * 1_000);
                })
                .register();
    }

    void tpa(Player sender, Player reciver, Long timeout) {
        if (sender == reciver) {
            sender.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpa.Self")));
            return;
        }
        if (!a.containsKey(reciver)) a.put(reciver, new HashMap<>());
        a.get(reciver).put(sender,timeout);
        sender.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpa.Sender"), Placeholder.component("player", reciver.teamDisplayName())));
        reciver.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpa.Reciver"), Placeholder.component("player", sender.teamDisplayName())));
    }

    void tpaccept(Player acceptor, Player sender, Long timeout) {
        if (!a.containsKey(acceptor)) {
            acceptor.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpaccept.NoRequest"), Placeholder.component("player", sender.teamDisplayName())));
            return;
        }
        if (!a.get(acceptor).containsKey(sender)) {
            acceptor.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpaccept.NoRequest"), Placeholder.component("player", sender.teamDisplayName())));
            return;
        }
        if (timeout < System.currentTimeMillis()) {
            acceptor.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpaccept.Timeout")));
            return;
        }

        acceptor.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpaccept.Reciver"), Placeholder.component("player", sender.teamDisplayName())));

        if (stuff.getConfig().getBoolean("TpaCommand.Delay.Enabled")) {
            cancelTeleport.put(sender, false);
            int delay = stuff.getConfig().getInt("TpaCommand.Delay.Amount");
            for (int i = delay; i >= 1; i--) {
                int finalI = i;
                Bukkit.getScheduler().runTaskLater(Stuff.INSTANCE, () -> {
                    if (cancelTeleport.containsKey(sender) && cancelTeleport.get(sender)) { return; }
                    sender.sendActionBar(Component.text(finalI));
                    sender.playSound(sender, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 200, 50);
                }, 20 * (delay - finalI));
            }
            Bukkit.getScheduler().runTaskLater(Stuff.INSTANCE, () -> {
                if (cancelTeleport.containsKey(sender) && cancelTeleport.get(sender)) { cancelTeleport.remove(sender); return; }
                cancelTeleport.remove(sender);
                sender.teleport(acceptor.getLocation());
                sender.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpaccept.Sender"), Placeholder.component("player", acceptor.teamDisplayName())));
                a.get(acceptor).remove(sender);
            }, 20 * (delay));
        } else {
            sender.teleport(acceptor.getLocation());
            sender.sendMessage(mm.deserialize(stuff.getConfig().getString("TpaCommand.Messages.Tpaccept.Sender"), Placeholder.component("player", acceptor.teamDisplayName())));
            a.get(acceptor).remove(sender);
        }
    }
}