package de.jonas.stuff.chatchannels;

import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.ChatChannel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Default implements ChatChannel {

    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();
    MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean canSeeMessage(Player source, Audience audience) {
        return true;
    }

    @Override
    public Component render(Player source, Component playerDisplayName, Component formattedMessage, Audience viewer) {
        return mm.deserialize(conf.getString("Format.Chat.Format"),
                Placeholder.component("player", playerDisplayName),
                Placeholder.component("message", formattedMessage));
    }

    @Override
    public boolean canJoinChannel(Player player) {
        return true;
    }
}
