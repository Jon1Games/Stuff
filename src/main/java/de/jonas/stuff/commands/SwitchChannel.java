package de.jonas.stuff.commands;


import de.jonas.stuff.ChatChannelManager;
import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SwitchChannel {

    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration conf = stuff.getConfig();
    ChatChannelManager chatChannelManager = stuff.chatChannelManager;
    MiniMessage mm = MiniMessage.miniMessage();

    public SwitchChannel() {

        new CommandAPICommand("stuff:channel")
                .withPermission(conf.getString("SwitchChannel.Permission"))
                .withAliases(conf.getStringList("SwitchChannel.Aliases").toArray(num -> new String[num]))
                .withOptionalArguments(new StringArgument(conf.getString("SwitchChannel.suggestionName.Channel"))
                        .replaceSuggestions(ArgumentSuggestions.strings(info -> {
                            if (info.sender() instanceof Player p) {
                                return chatChannelManager.getChannelNamesForPlayer(p).toArray(num -> new String[num]);
                            } else return new String[]{};
                        })))
                .executesPlayer(((sender, args) -> {
                    String channel = (String) args.get(
                            conf.getString("SwitchChannel.suggestionName.Channel"));
                    if (channel == null) {
                        chatChannelManager.unsetPlayerChannel(sender);
                        sender.sendMessage(mm.deserialize(conf.getString("SwitchChannel.Messages.JoinedChannel"),
                                Placeholder.component("channel", Component.text(
                                        conf.getString("SwitchChannel.DefaultChannelName")
                                ))));
                        return;
                    }
                    channel = channel.toLowerCase();

                    if (!chatChannelManager.getChannelNames().contains(channel)) {
                        sender.sendMessage(mm.deserialize(
                                conf.getString("SwitchChannel.Messages.NotExistend"),
                                Placeholder.component("channel", Component.text(channel))));
                        return;
                    }

                    if (chatChannelManager.setPlayerChannel(sender, channel)) {
                        sender.sendMessage(mm.deserialize(
                                conf.getString("SwitchChannel.Messages.JoinedChannel"),
                                Placeholder.component("channel", Component.text(channel))));
                    } else {
                        sender.sendMessage(mm.deserialize(
                                conf.getString("SwitchChannel.Messages.NoPermissionsChannel"),
                                Placeholder.component("channel", Component.text(channel))));
                    }

                }))
                .register();

    }

}
