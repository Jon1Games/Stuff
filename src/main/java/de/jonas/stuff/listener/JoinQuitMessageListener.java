package de.jonas.stuff.listener;

import de.jonas.stuff.Stuff;
import de.jonas.stuff.commands.Tpa;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class JoinQuitMessageListener implements Listener {

    MiniMessage mm = MiniMessage.miniMessage();
    Stuff stuff = Stuff.INSTANCE;
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.joinMessage(mm.deserialize(stuff.getConfig().getString("CustomJoinQuitMessage.Messages.JoinMessage"), Placeholder.component("player", e.getPlayer().teamDisplayName())));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.quitMessage(mm.deserialize(stuff.getConfig().getString("CustomJoinQuitMessage.Messages.LeaveMessage"), Placeholder.component("player", e.getPlayer().teamDisplayName())));

        var a = Stuff.INSTANCE.tpa.a;

        if(a.containsKey(e.getPlayer())) a.remove(e.getPlayer());
        // save ne concurrent modification exception
        for(Map.Entry<Player, Map<Player, Long>> ent : a.entrySet())
            if(ent.getValue().containsKey(e.getPlayer())) ent.getValue().remove(e.getPlayer());
    }
}
