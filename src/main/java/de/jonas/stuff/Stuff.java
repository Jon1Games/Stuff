package de.jonas.stuff;

import de.jonas.stuff.commands.*;
import de.jonas.stuff.listener.*;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Stuff extends JavaPlugin {

    public static Stuff INSTANCE;
    public static String PREFIX;
    public Logger logger;
    public CancelTeleport cancelTeleport;
    public TeamDisplaynameSet teamDisplaynameSet;

    public void onLoad() {
        INSTANCE = this;
        this.logger = this.getLogger();

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));

        cancelTeleport = new CancelTeleport();
        teamDisplaynameSet = new TeamDisplaynameSet();
        teamDisplaynameSet.onLoad();

        if (getConfig().getBoolean("EnableCalculatorCommand")) new CalculatorCommand();
        if (getConfig().getBoolean("MsgCommand.Enabled")) new MsgCommand();
        if (getConfig().getBoolean("FlyCommand.Enabled")) new FlyCommand();
        if (getConfig().getBoolean("SpeedCommand.Enabled")) new SpeedCommand();
        if (getConfig().getBoolean("GamemodeCommand.Enabled")) new GamemodeCommand();
        if (getConfig().getBoolean("PortableInventoryCommand.Enabled")) new PortableInventoryCommand();
        new ReloadCommand();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        PREFIX = "[Stuff] ";

        CommandAPI.onEnable();

        teamDisplaynameSet.onEnable();

        this.listener();

        this.saveDefaultConfig();

        logger.log(Level.INFO, "Activated Plugin");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CommandAPI.onDisable();

        logger.log(Level.INFO,"Plugin deaktiviert.");
    }

    public void listener() {
        PluginManager pm = Bukkit.getPluginManager();

        if (getConfig().getBoolean("CustomJoinQuitMessage.Enabled")) pm.registerEvents(new JoinQuitMessageListener(), this);
        if (getConfig().getBoolean("FlyCommand.Enabled")) pm.registerEvents(new JoinFlyListener(), this);
        if (getConfig().getBoolean("SpeedCommand.Enabled")) pm.registerEvents(new JoinSpeedListener(), this);
        if (getConfig().getBoolean("Format.Chat.Enabled")) pm.registerEvents(new ChatListener(), this);
        if (getConfig().getBoolean("Format.PlayerNames.Enabled")) pm.registerEvents(teamDisplaynameSet, this);
        pm.registerEvents(cancelTeleport, this);
    }
}

