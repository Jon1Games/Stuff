package de.jonas.stuff.listener;

import de.jonas.stuff.Stuff;
import de.jonas.stuff.commands.SpeedCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class JoinSpeedListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        var mm = MiniMessage.miniMessage();
        Stuff stuff = Stuff.INSTANCE;

        if (player.getPersistentDataContainer().has(SpeedCommand.speedIdentifier)) {
            float speed = player.getPersistentDataContainer().get(SpeedCommand.speedIdentifier, PersistentDataType.FLOAT);
            player.setWalkSpeed(speed);
            player.setFlySpeed(speed);
            int fullspeed = (int) (speed * 10);
            if (fullspeed == 2) return;
            player.sendMessage(mm.deserialize(stuff.getConfig().getString("SpeedCommand.Messages.Self.ReturnSpeed"), Placeholder.component("speed", Component.text(fullspeed))));
        }
    }
}
