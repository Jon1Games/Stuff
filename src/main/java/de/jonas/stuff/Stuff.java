package de.jonas.stuff;

import de.jonas.stuff.commands.*;
import de.jonas.stuff.listener.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Stuff extends JavaPlugin {

    public static Stuff INSTANCE;
    public static String PREFIX;
    public Logger logger;
    public Tpa tpa;
    public CancelTeleport cancelTeleport = new CancelTeleport();;

    public void onLoad() {
        INSTANCE = this;
        this.logger = this.getLogger();

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
        if (getConfig().getBoolean("EnableCalculatorCommand")) new CalculatorCommand();
        if (getConfig().getBoolean("MsgCommand.Enabled")) new MsgCommand();
        if (getConfig().getBoolean("FlyCommand.Enabled")) new FlyCommand();
        if (getConfig().getBoolean("SpeedCommand.Enabled")) new SpeedCommand();
        if (getConfig().getBoolean("GamemodeCommand.Enabled")) new GamemodeCommand();
        if (getConfig().getBoolean("PortableInventoryCommand.Enabled")) new PortableInventoryCommand();
        if (getConfig().getBoolean("TpaCommand.Enabled")) this.tpa = new Tpa();

    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        PREFIX = "[Stuff] ";

        this.listener();

        CommandAPI.onEnable();

        this.saveDefaultConfig();

        logger.log(Level.INFO, "Activatet Plugin");

        logger.log(Level.WARNING, getConfig().getString("CustomJoinQuitMessage.Messages.JoinMessage"));

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
        pm.registerEvents(cancelTeleport, this);
    }
}

