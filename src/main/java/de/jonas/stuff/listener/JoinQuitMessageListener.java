package de.jonas.stuff.listener;

import de.jonas.stuff.Stuff;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitMessageListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        var mm = MiniMessage.miniMessage();
        Stuff stuff = new Stuff();

        e.joinMessage(mm.deserialize(stuff.getConfig().getString("JoinMessage"), Placeholder.component("player", player.teamDisplayName())));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        var mm = MiniMessage.miniMessage();
        Stuff stuff = new Stuff();

        e.quitMessage(mm.deserialize(stuff.getConfig().getString("LeaveMessage"), Placeholder.component("player", player.teamDisplayName())));
    }
}
