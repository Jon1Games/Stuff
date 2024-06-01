package de.jonas.stuff;

import de.jonas.stuff.commands.*;
import de.jonas.stuff.listener.*;
import de.jonas.stuff.utility.PermToOp;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

public final class Stuff extends JavaPlugin {

    public static Stuff INSTANCE;
    public TeamDisplaynameSet teamDisplaynameSet;
    public int commands;
    public int listeners;

    public void onLoad() {

        getLogger().log(Level.INFO, "Starting Plugin");

        INSTANCE = this;

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
        if (getConfig().getBoolean("InfoCommands.Enabled")) {
            new InfoCommands();
            increaseCommandCount();
        }
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

        listeners = 0;
        this.listener();
        if (listeners != 0) {
            getLogger().log(Level.INFO, listeners + " listener registered.");
        } else {

        }

        this.saveDefaultConfig();

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
        if (getConfig().getBoolean("Format.Chat.Enabled")) {
            pm.registerEvents(new ChatListener(), this);
            increaseListenerCount();
        }
        if (getConfig().getBoolean("Format.PlayerNames.Enabled")) {
            pm.registerEvents(teamDisplaynameSet, this);
            increaseListenerCount();
        }
    }
    
    public void increaseCommandCount() {
        commands++;
    }
    
    public void increaseListenerCount() {
        listeners++;
    }
}

