package de.jonas.stuff.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.title.Title;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import de.jonas.stuff.Stuff;


public class FirstJoin implements Listener {

    public static NamespacedKey firstJoin = new NamespacedKey("stuff", "is_first_join");

    MiniMessage mm = MiniMessage.miniMessage();
    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent e) {
        if (e.getPlayer().getPersistentDataContainer().has(firstJoin)) return;
        e.getPlayer().getPersistentDataContainer().set(firstJoin, PersistentDataType.BOOLEAN, true);

        if (
            conf.getBoolean("TeleportCommands.Enabled") || 
            conf.getBoolean("TeleportCommands.Spawn.Enabled")
            ) {
            e.getPlayer().teleport(Stuff.INSTANCE.getSpawn());
        }

        if (conf.getBoolean("FirstJoinTitle.Enabled")) {

            final Component mainTitle = mm.deserialize(conf.getString("FirstJoinTitle.Title"),
            Placeholder.component("player", e.getPlayer().teamDisplayName()));
            final Component subtitle = mm.deserialize(conf.getString("FirstJoinTitle.SubTitle"),
            Placeholder.component("player", e.getPlayer().teamDisplayName()));

            final Title title = Title.title(mainTitle, subtitle);
            
            e.getPlayer().showTitle(title);

        }

        if (conf.getBoolean("FirstJoniMessage.Enabled")) {
            e.getPlayer().sendMessage(mm.deserialize(conf.getString("FirstJoniMessage.Message")));
        }

    }

}
