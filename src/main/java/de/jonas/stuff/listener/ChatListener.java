package de.jonas.stuff.listener;

import de.jonas.stuff.ChatCaptureManager;
import de.jonas.stuff.ChatChannelManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.ChatChannel;
import de.jonas.stuff.interfaced.PlayerChatEvent;
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
    ChatChannelManager chatChannelManager = stuff.chatChannelManager;
    ChatCaptureManager captureManager = stuff.captureManager;

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent e) {
        Player p = e.getPlayer();
        PlayerChatEvent playerChatEvent = captureManager.getPlayer(p);
        if (playerChatEvent != null) {
            captureManager.unsetPlayerCapture(p);
            e.setCancelled(true);
            playerChatEvent.onMessage(p, PlainTextComponentSerializer.plainText().serialize(e.originalMessage()));
            return;
        } 
        ChatChannel chatChannel = chatChannelManager.getChannel(p);
        e.viewers().removeIf(viewer -> !chatChannel.canSeeMessage(p, viewer));
        e.renderer(this::renderMessage);
    }

    private Component renderMessage(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        String messageT = PlainTextComponentSerializer.plainText().serialize(message);
        Component messageC;
        if (stuff.getConfig().getString("Format.Chat.ColorType").equalsIgnoreCase("vanilla")) {
            messageC = LegacyComponentSerializer.legacyAmpersand().deserialize(messageT);
        } else if (stuff.getConfig().getString("Format.Chat.ColorType").equalsIgnoreCase("minimessage")) {
            messageC = mm.deserialize(messageT,
                    Placeholder.component("i", source.getInventory().getItemInMainHand().displayName()));
        } else {
            messageC = message;
        }

        return chatChannelManager.getChannel(source).render(source, source.teamDisplayName(), messageC, viewer);
    }
}
