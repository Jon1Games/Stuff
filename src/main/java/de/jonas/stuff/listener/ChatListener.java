package de.jonas.stuff.listener;

import de.jonas.stuff.Stuff;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    Stuff stuff = Stuff.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent e) {
        e.renderer(this::renderMessage);
    }

    private Component renderMessage(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        String messageT = PlainTextComponentSerializer.plainText().serialize(message);
        Component displayText = LegacyComponentSerializer.legacyAmpersand().deserialize(messageT);
        return mm.deserialize(stuff.getConfig().getString("Format.Chat.Format"),
                Placeholder.component("player", source.teamDisplayName()),
                Placeholder.component("message", displayText));
    }
}
