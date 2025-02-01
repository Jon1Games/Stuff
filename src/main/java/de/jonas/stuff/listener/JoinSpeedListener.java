package de.jonas.stuff.listener;

import de.jonas.stuff.Stuff;
import de.jonas.stuff.commands.SpeedCommand;
import me.gaminglounge.configapi.Language;
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

        if (player.getPersistentDataContainer().has(SpeedCommand.flySpeedIdentifier) &&
                player.getPersistentDataContainer().get(SpeedCommand.flySpeedIdentifier, PersistentDataType.FLOAT) > 1) {
            float speed = player.getPersistentDataContainer().get(SpeedCommand.flySpeedIdentifier, PersistentDataType.FLOAT);
            player.setFlySpeed(speed);
            int fullspeed = (int) (speed * 10);
            if (fullspeed == 2) return;
            player.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, player, "flyspeed.self.returnSpeed", true),
                Placeholder.component("speed", Component.text(fullspeed))));
        }

        if (player.getPersistentDataContainer().has(SpeedCommand.walkSpeedIdentifier) &&
                player.getPersistentDataContainer().get(SpeedCommand.walkSpeedIdentifier, PersistentDataType.FLOAT) > 1) {
            float speed = player.getPersistentDataContainer().get(SpeedCommand.walkSpeedIdentifier, PersistentDataType.FLOAT);
            player.setWalkSpeed(speed);
            int fullspeed = (int) (speed * 10);
            if (fullspeed == 2) return;
            player.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, player, "walkspeed.self.returnSpeed", true),
                Placeholder.component("speed", Component.text(fullspeed))));
        }
    }
}
