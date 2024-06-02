package de.jonas.stuff;

import org.bukkit.entity.Player;
import de.jonas.stuff.interfaced.PlayerChatEvent;
import java.util.Map;
import java.util.HashMap;

public class ChatCaptureManager {
    
    private Map<Player, PlayerChatEvent> map; 

    public ChatCaptureManager() {
        map = new HashMap<>();
    }

    public void capturePlayer(Player p, PlayerChatEvent e) {
        map.put(p, e);
    }

    public void unsetPlayerCapture(Player p) {
        map.remove(p);
    }

    public PlayerChatEvent getPlayer(Player p) {
        return map.get(p);
    }

}
