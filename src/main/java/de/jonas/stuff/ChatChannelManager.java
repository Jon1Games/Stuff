package de.jonas.stuff;

import org.bukkit.entity.Player;

import de.jonas.stuff.interfaced.ChatChannel;

import java.util.*;

public class ChatChannelManager {

    private Map<Player, ChatChannel> mapP;
    private Map<String, ChatChannel> mapC;
    private ChatChannel defaultChannel;

    public void onEnable() {
        mapP = new HashMap<>();
        mapC = new HashMap<>();
    }

    public void registerChannel(String channelName, ChatChannel channel) {
        mapC.put(channelName, channel);
    }

    public ChatChannel getChannel(Player player) {
        if (mapP.containsKey(player)) {
            return mapP.get(player);
        } else return defaultChannel;
    }

    public void unsetPlayerChannel(Player player) {
        mapP.remove(player);
    }

    public boolean setPlayerChannel(Player player, String channel) {
        ChatChannel chatChannel = mapC.get(channel);
        if (chatChannel == null) return false;
        if (!chatChannel.canJoinChannel(player)) return false;
        mapP.put(player, chatChannel);
        return true;
    }

    public void setDefaultChannel(ChatChannel defaultChannel) {
        this.defaultChannel = defaultChannel;
    }

    public ArrayList<String> getChannelNames() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(mapC.keySet());
        return list;
    }

    public ArrayList<String> getChannelNamesForPlayer(Player player) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(mapC.keySet());
        list.removeIf(channel -> !mapC.get(channel).canJoinChannel(player));
        return list;
    }

}
