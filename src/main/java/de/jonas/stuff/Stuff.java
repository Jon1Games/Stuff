package de.jonas.stuff;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.jonas.stuff.chatchannels.AbstractChannel;
import de.jonas.stuff.chatchannels.Default;
import de.jonas.stuff.commands.BroadcastCommand;
import de.jonas.stuff.commands.CalculatorCommand;
import de.jonas.stuff.commands.CommandCommand;
import de.jonas.stuff.commands.DebugCommands;
import de.jonas.stuff.commands.FlyCommand;
import de.jonas.stuff.commands.GamemodeCommand;
import de.jonas.stuff.commands.InfoCommands;
import de.jonas.stuff.commands.MsgCommand;
import de.jonas.stuff.commands.PingCommand;
import de.jonas.stuff.commands.PlayTimeCommand;
import de.jonas.stuff.commands.PortableInventoryCommand;
import de.jonas.stuff.commands.ReloadCommand;
import de.jonas.stuff.commands.SpeedCommand;
import de.jonas.stuff.commands.SudoCommand;
import de.jonas.stuff.commands.SwitchChannel;
import de.jonas.stuff.commands.Teleportation;
import de.jonas.stuff.interfaced.ChatChannel;
import de.jonas.stuff.listener.BlockBreakEvent;
import de.jonas.stuff.listener.BlockPlace;
import de.jonas.stuff.listener.BossBarTimer;
import de.jonas.stuff.listener.ChatListener;
import de.jonas.stuff.listener.DoAfter;
import de.jonas.stuff.listener.DoBefore;
import de.jonas.stuff.listener.FirstJoin;
import de.jonas.stuff.listener.InvClickEvent;
import de.jonas.stuff.listener.JoinFlyListener;
import de.jonas.stuff.listener.JoinQuitMessageListener;
import de.jonas.stuff.listener.JoinSpeedListener;
import de.jonas.stuff.listener.JointTpToSpawn;
import de.jonas.stuff.listener.TeamDisplaynameSet;
import de.jonas.stuff.utility.PermToOp;
import de.jonas.stuff.utility.TimedMessages;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

public final class Stuff extends JavaPlugin {

    public static Stuff INSTANCE;
    public Teleportation teleportation;
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
    public MsgCommand msgCommand;
    public TimerHandler timerHandler;

    public void onLoad() {

        if (getConfig().getBoolean("OnlyUseAPI")) {
            getLogger().log(Level.INFO, "Starting Plugin in APi only mode");
        } else {
            getLogger().log(Level.INFO, "Starting Plugin");
        }

        INSTANCE = this;

        this.saveDefaultConfig();

        chatChannelManager = new ChatChannelManager();
        Default defaultChannel = new Default();
        chatChannelManager.setDefaultChannel(defaultChannel);

        itemBuilderManager = new ItemBuilderManager();

        captureManager = new ChatCaptureManager();

        events = new Events();
        
        if(!getConfig().getBoolean("OnlyUseAPI")) {

            if (!CommandAPI.isLoaded()) CommandAPI.onLoad(new CommandAPIBukkitConfig(this));

            if (getConfig().getBoolean("Format.PlayerNames.Enabled")) {
                getLogger().log(Level.INFO, "Enabling playername formatting");
                teamDisplaynameSet = new TeamDisplaynameSet();
                teamDisplaynameSet.onLoad();
            } else {
                getLogger().log(Level.INFO, "Playername formatting is disabled");
            }

            new ReloadCommand();
            commands = 1;
            if (getConfig().getBoolean("DebugCommands.Enabled")) {
                DebugCommands debugCommands = new DebugCommands();
                debugCommands.events();
                increaseCommandCount();
            }
            if (getConfig().getBoolean("EnableCalculatorCommand.Enabled")) {
                new CalculatorCommand();
                increaseCommandCount();
            }
            if (getConfig().getBoolean("MsgCommand.Enabled")) {
                msgCommand = new MsgCommand();
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
            if (getConfig().getBoolean("TeleportCommands.Enabled")) {
                teleportation = new Teleportation();
            }
            if (getConfig().getBoolean("SwitchChannel.Enabled") && getConfig().getBoolean("Format.Chat.Enabled")) {
                new SwitchChannel();
                increaseCommandCount();
            }
            if (getConfig().getBoolean("Sudo.Enabled")) {
                new SudoCommand();
                increaseCommandCount();
            }

            getLogger().log(Level.INFO, commands + " commands registered");

        }
        
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.listener();

        chatChannelManager.onEnable();

        if(!getConfig().getBoolean("OnlyUseAPI")) {

            CommandAPI.onEnable();

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
            }

            getLogger().log(Level.INFO, channels+ " channels registered");

            if (getConfig().getBoolean("Format.PlayerNames.Enabled")) teamDisplaynameSet.onEnable();

            if (getConfig().getBoolean("GiveOpPermission.Enabled")) {
                PermToOp permToOp = new PermToOp();
                permToOp.onEnable();
            }

            if (getConfig().getBoolean("TimedMessages.Enabled")) new TimedMessages();

            if (getConfig().getBoolean("OnlyUseAPI")) {
                getLogger().log(Level.INFO, "Startup in API only mode Complete");
            } else {
                getLogger().log(Level.INFO, "Startup Complete");
            }
    
            if (getConfig().getBoolean("Timings.Enabled")) {
                timerHandler = new TimerHandler();
            }

            if (getConfig().getBoolean("TeleportCommands.Enabled")) {
                teleportation.onEnable();
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().log(Level.INFO,"Disabling Plugin");

        CommandAPI.onDisable();
        getLogger().log(Level.INFO, "CommandAPI disabled");

        getLogger().log(Level.INFO,"Disabling complete");
    }

    public void listener() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new InvClickEvent(), this);
        pm.registerEvents(new BlockPlace(), this);
        pm.registerEvents(new FirstJoin(), this);
        pm.registerEvents(new BlockBreakEvent(), this);
        getLogger().log(Level.INFO, "3 API listener registered");

        if(!getConfig().getBoolean("OnlyUseAPI")) {
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
            if (getConfig().getBoolean("TeleportCommands.Enabled")) {
                if (getConfig().getBoolean("TeleportCommands.TPA.Enabled")) {
                    pm.registerEvents(teleportation, this);
                    increaseListenerCount();
                }
                if (
                    getConfig().getBoolean("TeleportCommands.Spawn.Enabled") &&
                    getConfig().getBoolean("TeleportCommands.Spawn.JoinTpToSpawn")
                    ) {
                    pm.registerEvents(new JointTpToSpawn(), this);
                    increaseListenerCount();
                }
            }
            if (getConfig().getBoolean("MsgCommand.Enabled")) {
                pm.registerEvents(msgCommand, this);
                increaseListenerCount();
            }
            if (getConfig().getBoolean("Timings.Enabled")) {
                pm.registerEvents(new BossBarTimer(), this);
                increaseListenerCount();
            }
            if (getConfig().getBoolean("DoBefore.Enabled")) {
                pm.registerEvents(new DoBefore(), this);
                increaseListenerCount();
            }
            if (getConfig().getBoolean("DoAfter.Enabled")) {
                pm.registerEvents(new DoAfter(), this);
                increaseListenerCount();
            }
            getLogger().log(Level.INFO, listeners + " listener registered");
        }
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

        public Location getSpawn() {
        Double x = getConfig().getDouble("TeleportCommands.Spawn.Pos_X");
        Double y = getConfig().getDouble("TeleportCommands.Spawn.Pos_Y");
        Double z = getConfig().getDouble("TeleportCommands.Spawn.Pos_Z");
        
        if (
            x == 0 || 
            y == 0 || 
            z == 0 || 
            x.isNaN() || 
            y.isNaN() || 
            z.isNaN() || 
            x.isInfinite() || 
            y.isInfinite() || 
            z.isInfinite()
        ) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }

        return new Location(
            Bukkit.getWorld(getConfig().getString("TeleportCommands.Spawn.World")),
            x, y, z,
            (float) getConfig().getDouble("TeleportCommands.Spawn.Pos_YAW"),
            (float) getConfig().getDouble("TeleportCommands.Spawn.Pos_PITCH")
        );
    }

}

