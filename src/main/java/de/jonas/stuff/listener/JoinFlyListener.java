package de.jonas.stuff.listener;

import de.jonas.stuff.Stuff;
import de.jonas.stuff.commands.FlyCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.net.http.WebSocket;

public class JoinFlyListener implements WebSocket.Listener, Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        var mm = MiniMessage.miniMessage();
        Stuff stuff = new Stuff();

        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (player.getGameMode() == GameMode.SPECTATOR) return;

        if (player.getPersistentDataContainer().has(FlyCommand.flyAllowidentifier)) {
            player.setAllowFlight(player.getPersistentDataContainer().get(FlyCommand.flyAllowidentifier, PersistentDataType.BOOLEAN));
            e.joinMessage(mm.deserialize(stuff.getConfig().getString("ReturnInEnterFlyMode")));
        } else player.getPersistentDataContainer().set(FlyCommand.flyAllowidentifier, PersistentDataType.BOOLEAN, player.getAllowFlight());
    }
}
