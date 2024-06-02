package de.jonas.stuff.utility;

import org.bukkit.entity.Player;

import de.jonas.stuff.ChatCaptureManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.PlayerChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class UseNextChatInput {
    
    MiniMessage mm = MiniMessage.miniMessage();
    ChatCaptureManager manager = Stuff.INSTANCE.captureManager;

    private Player p;
    private Component m;
    private PlayerChatEvent e;

    public UseNextChatInput(Player p) {
        this.p = p;
    }

    public UseNextChatInput sendMessage(String message) {
        m = mm.deserialize(message).decoration(TextDecoration.ITALIC, false);
        return this;
    }

    public UseNextChatInput sendMessage(Component message) {
        m = message;
        return this;
    }

    public UseNextChatInput setChatEvent(PlayerChatEvent e) {
        this.e = e;
        return this;
    }

    public void capture() {
        p.sendMessage(m);
        manager.capturePlayer(p, e);
    }

}
