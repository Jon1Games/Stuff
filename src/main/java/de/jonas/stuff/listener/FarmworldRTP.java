package de.jonas.stuff.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class FarmworldRTP implements Listener {

    @EventHandler
    public void onJoin(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().getName().equals("farmwelt")
                || event.getPlayer().getWorld().getName().equals("farmwelt_nether")
                || event.getPlayer().getWorld().getName().equals("farmwelt_the_end")) {
            Bukkit.dispatchCommand(event.getPlayer(), "rtp");
        }
    }
}
