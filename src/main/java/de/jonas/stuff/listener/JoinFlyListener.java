package de.jonas.stuff.listener;

import de.jonas.stuff.Stuff;
import de.jonas.stuff.commands.FlyCommand;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class JoinFlyListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        var mm = MiniMessage.miniMessage();
        Stuff stuff = Stuff.INSTANCE;

        if (player.getGameMode() == GameMode.CREATIVE) {
            player.setFlying(true);
            return;
        }
        if (player.getGameMode() == GameMode.SPECTATOR) {
            player.setFlying(true);
            return;
        }

        if (player.getPersistentDataContainer().has(FlyCommand.flyAllowidentifier)) {
            player.setAllowFlight(player.getPersistentDataContainer().get(FlyCommand.flyAllowidentifier, PersistentDataType.BOOLEAN));
            player.setFlying(true);
            e.getPlayer().sendMessage(mm.deserialize(stuff.getConfig().getString("FlyCommand.Messages.Self.ReturnInEnterFlyMode")));
        } else player.getPersistentDataContainer().set(FlyCommand.flyAllowidentifier, PersistentDataType.BOOLEAN, player.getAllowFlight());
    }
}
