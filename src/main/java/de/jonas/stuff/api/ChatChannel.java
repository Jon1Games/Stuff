package de.jonas.stuff.api;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface ChatChannel {

    public boolean canSeeMessage(Player source, Audience audience);
    public Component render(Player source, Component playerDisplayName, Component formattedMessage, Audience viewer);
    public boolean canJoinChannel(Player player);

}
