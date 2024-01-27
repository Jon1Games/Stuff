package de.jonas.stuff.listener;

import de.jonas.stuff.Stuff;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class CancelTeleport implements Listener {

    public Map<Player, Boolean> cancelTeleportMap = new HashMap<>();
    MiniMessage mm = MiniMessage.miniMessage();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!e.hasChangedPosition()) return;
        if (cancelTeleportMap.containsKey(e.getPlayer())) {
            if (!cancelTeleportMap.get(e.getPlayer())) e.getPlayer().sendMessage(mm.deserialize(
                    Stuff.INSTANCE.getConfig().getString("TpaCommand.Messages.Error.Move")
            ));
            cancelTeleportMap.put(e.getPlayer(), true);
        }

    }
}
