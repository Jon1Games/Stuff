package de.jonas.stuff.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.jonas.stuff.Stuff;

public class BossBarTimer implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (Stuff.INSTANCE.timerHandler.barAktive()) e.getPlayer().showBossBar(Stuff.INSTANCE.timerHandler.getBar());
    }

    @EventHandler
    public void onJoin(PlayerQuitEvent e) {
        e.getPlayer().hideBossBar(Stuff.INSTANCE.timerHandler.getBar());
    }
    
}
