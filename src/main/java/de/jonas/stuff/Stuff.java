package de.jonas.stuff;

import de.jonas.stuff.chatchannels.AbstractChannel;
import de.jonas.stuff.chatchannels.Default;
import de.jonas.stuff.commands.*;
import de.jonas.stuff.interfaced.ChatChannel;
import de.jonas.stuff.listener.*;
import de.jonas.stuff.utility.PermToOp;
import de.jonas.stuff.utility.TimedMessages;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public final class Stuff extends JavaPlugin {

    public static Stuff INSTANCE;
    public TeamDisplaynameSet teamDisplaynameSet;
    public int commands;
    public int listeners;
    public ChatChannelManager chatChannelManager;
    public int channels;
    public ItemBuilderManager itemBuilderManager;
    public int itemBuilds;
    public ChatChannel inputChatChannel;
    public ChatCaptureManager captureManager;
    public Events events;

    public void onLoad() {

        getLogger().log(Level.INFO, "Starting Plugin");

        INSTANCE = this;

        this.saveDefaultConfig();

        chatChannelManager = new ChatChannelManager();
        Default defaultChannel = new Default();
        chatChannelManager.setDefaultChannel(defaultChannel);

        itemBuilds = 0;
        itemBuilderManager = new ItemBuilderManager();
        getLogger().log(Level.INFO, itemBuilds + " Item builded");

        captureManager = new ChatCaptureManager();

        events = new Events();

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));

        if (getConfig().getBoolean("Format.PlayerNames.Enabled")) {
            getLogger().log(Level.INFO, "Enabling playername formatting");
            teamDisplaynameSet = new TeamDisplaynameSet();
            teamDisplaynameSet.onLoad();
        } else {
            getLogger().log(Level.INFO, "Playername formatting is disabled");
        }

        new ReloadCommand();
        commands = 1;
        if (getConfig().getBoolean("EnableCalculatorCommand.Enabled")) {
            new CalculatorCommand();
            increaseCommandCount();
        }
        if (getConfig().getBoolean("MsgCommand.Enabled")) {
            new MsgCommand();
            increaseCommandCount();
        }
        if (getConfig().getBoolean("FlyCommand.Enabled")) {
            new FlyCommand();
            increaseCommandCount();
        }
        new SpeedCommand();
        if (getConfig().getBoolean("GamemodeCommand.Enabled")) {
            new GamemodeCommand();
            increaseCommandCount();
        }
        if (getConfig().getBoolean("PortableInventoryCommand.Enabled")) {
            new PortableInventoryCommand();
            increaseCommandCount();
        }
        if (getConfig().getBoolean("InfoCommands.Enabled")) new InfoCommands();
        if (getConfig().getBoolean("PlayTimeCommand.Enabled")) {
            new PlayTimeCommand();
            increaseCommandCount();
        }
        if (getConfig().getBoolean("PingCommand.Enabled")) {
            new PingCommand();
            increaseCommandCount();
        }
        if (getConfig().getBoolean("CommandCommand.Enabled")) {
            new CommandCommand();
            increaseCommandCount();
        }
        if (getConfig().getBoolean("BroadcastCommand.Enabled")) {
            new BroadcastCommand();
            increaseCommandCount();
        }
        if (getConfig().getBoolean("SwitchChannel.Enabled") && getConfig().getBoolean("Format.Chat.Enabled")) {
            new SwitchChannel();
            increaseCommandCount();
        }

        getLogger().log(Level.INFO, commands + " commands registered");
        
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        CommandAPI.onEnable();

        if (getConfig().getBoolean("Format.PlayerNames.Enabled")) teamDisplaynameSet.onEnable();

        if (getConfig().getBoolean("GiveOpPermission.Enabled")) {
            PermToOp permToOp = new PermToOp();
            permToOp.onEnable();
        }

        chatChannelManager.onEnable();

        ConfigurationSection sec = getConfig().getConfigurationSection("Channels");
        for (String a : sec.getKeys(false)) {
            ConfigurationSection cs = sec.getConfigurationSection(a);

            if (!cs.getBoolean("Enabled")) continue;

            AbstractChannel abstractChannel = new AbstractChannel(
                    cs.getString("Permission.See"),
                    cs.getString("Permission.Join"),
                    cs.getString("Format"),
                    cs.getBoolean("CanSeeOwnMessages")
            );

            chatChannelManager.registerChannel(a.toLowerCase(), abstractChannel);

            increaseChannelCount();

            new TimedMessages();

        }

        getLogger().log(Level.INFO, channels+ " channels registered");

        this.listener();
        getLogger().log(Level.INFO, listeners + " listener registered");

        getLogger().log(Level.INFO, "Startup Complete");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().log(Level.INFO," Starting disabling");

        CommandAPI.onDisable();
        getLogger().log(Level.INFO, "CommandAPI disabled");

        getLogger().log(Level.INFO,"Disabling complete");
    }

    public void listener() {
        PluginManager pm = Bukkit.getPluginManager();

        if (getConfig().getBoolean("CustomJoinQuitMessage.Enabled")) {
            pm.registerEvents(new JoinQuitMessageListener(), this);
            increaseListenerCount();
        }
        if (getConfig().getBoolean("FlyCommand.Enabled")) {
            pm.registerEvents(new JoinFlyListener(), this);
            increaseListenerCount();
        }
        if (getConfig().getBoolean("SpeedCommand.Enabled")) {
            pm.registerEvents(new JoinSpeedListener(), this);
            increaseListenerCount();
        }
        pm.registerEvents(new ChatListener(), this);
        increaseListenerCount();
        if (getConfig().getBoolean("Format.PlayerNames.Enabled")) {
            pm.registerEvents(teamDisplaynameSet, this);
            increaseListenerCount();
        }
        pm.registerEvents(new InvClickEvent(), this);
        increaseListenerCount();
        pm.registerEvents(new BlockPlace(), this);
        increaseListenerCount();
        pm.registerEvents(new FirstJoin(), this);
        increaseListenerCount();
    }
    
    public void increaseCommandCount() {
        commands++;
    }
    
    public void increaseListenerCount() {
        listeners++;
    }

    public void increaseChannelCount() {
        channels++;
    }

    public void increaseitemBuildsCount() {
        itemBuilds++;
    }
}

