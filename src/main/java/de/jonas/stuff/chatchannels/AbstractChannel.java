package de.jonas.stuff.chatchannels;

import de.jonas.stuff.api.ChatChannel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

public class AbstractChannel implements ChatChannel {

    MiniMessage mm = MiniMessage.miniMessage();

    public String seePermission;
    public String joinPermission;
    public String format;
    public boolean canSeeOwnMessages;

    public AbstractChannel(String seePermission, String joinPermission, String format, boolean canSeeOwnMessages) {
        this.seePermission = seePermission;
        this.joinPermission = joinPermission;
        this.format = format;
        this.canSeeOwnMessages = canSeeOwnMessages;
    }

    @Override
    public boolean canSeeMessage(Player source, Audience audience) {
        if (canSeeOwnMessages && source == audience) return true;
        if (audience instanceof Permissible i) return i.hasPermission(seePermission);
        return false;
    }

    @Override
    public Component render(Player source, Component playerDisplayName, Component formattedMessage, Audience viewer) {
        return mm.deserialize(format,
                Placeholder.component("player", playerDisplayName),
                Placeholder.component("message", formattedMessage));
    }

    @Override
    public boolean canJoinChannel(Player player) {
        return player.hasPermission(joinPermission);
    }
}
