package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class InfoCommands {

    Stuff stuff = Stuff.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();

    public InfoCommands() {

        if (stuff.getConfig().getBoolean("InfoCommands.Links.Enabled")) {
            new CommandAPICommand("stuff:links")
                    .withAliases("links", "Stuff:links")
                    .executesPlayer((player, args) -> {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("InfoCommands.Links.Message")));
                    })
                    .register();
        }

        if (stuff.getConfig().getBoolean("InfoCommands.Discord.Enabled")) {
            new CommandAPICommand("stuff:discord")
                    .withAliases("discord", "Stuff:discord")
                    .executesPlayer((player, args) -> {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("InfoCommands.Discord.Message")));
                    })
                    .register();
        }

        if (stuff.getConfig().getBoolean("InfoCommands.TikTok.Enabled")) {
            new CommandAPICommand("stuff:tiktok")
                    .withAliases("tiktok", "Stuff:tiktok")
                    .executesPlayer((player, args) -> {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("InfoCommands.TikTok.Message")));
                    })
                    .register();
        }

        if (stuff.getConfig().getBoolean("InfoCommands.Instagram.Enabled")) {
            new CommandAPICommand("stuff:instagram")
                    .withAliases("instagram", "Stuff:instagram")
                    .executesPlayer((player, args) -> {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("InfoCommands.Instagram.Message")));
                    })
                    .register();
        }

        if (stuff.getConfig().getBoolean("InfoCommands.Twitter.Enabled")) {
            new CommandAPICommand("stuff:twitter")
                    .withAliases("twitter", "Stuff:twitter")
                    .executesPlayer((player, args) -> {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("InfoCommands.Twitter.Message")));
                    })
                    .register();
        }

        if (stuff.getConfig().getBoolean("InfoCommands.X.Enabled")) {
            new CommandAPICommand("stuff:x")
                    .withAliases("x", "Stuff:x")
                    .executesPlayer((player, args) -> {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("InfoCommands.X.Message")));
                    })
                    .register();
        }

        if (stuff.getConfig().getBoolean("InfoCommands.MinecraftMap.Enabled")) {
            new CommandAPICommand("stuff:map")
                    .withAliases("map", "Stuff:map")
                    .executesPlayer((player, args) -> {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("InfoCommands.MinecraftMap.Message")));
                    })
                    .register();
        }
    }
}
