package de.jonas.stuff.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.jonas.stuff.Stuff;

public class JointTpToSpawn implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(Stuff.INSTANCE.getSpawn());
    }
    
}
