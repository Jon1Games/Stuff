package de.jonas.stuff;

import de.jonas.stuff.commands.*;
import de.jonas.stuff.listener.JoinFlyListener;
import de.jonas.stuff.listener.JoinQuitMessageListener;
import de.jonas.stuff.listener.JoinSpeedListener;
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

    public void onLoad() {
        this.logger = this.getLogger();

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
        if (getConfig().getBoolean("CalculatorCommand")) new CalculatorCommand();
        if (getConfig().getBoolean("MsgCommand")) new MsgCommand();
        if (getConfig().getBoolean("FlyCommand")) new FlyCommand();
        if (getConfig().getBoolean("SpeedCommand")) new SpeedCommand();
        if (getConfig().getBoolean("GamemodeCommand")) new GamemodeCommand();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        PREFIX = "[Stuff] ";

        this.listener();

        CommandAPI.onEnable();

        this.saveDefaultConfig();

        logger.log(Level.INFO, "Activatet Plugin");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CommandAPI.onDisable();

        logger.log(Level.INFO,"Plugin deaktiviert.");
    }

    public void listener() {
        PluginManager pm = Bukkit.getPluginManager();

        if (getConfig().getBoolean("CustomJoinQuitMessage")) pm.registerEvents(new JoinQuitMessageListener(), this);
        if (getConfig().getBoolean("FlyCommand")) pm.registerEvents(new JoinFlyListener(), this);
        if (getConfig().getBoolean("SpeedCommand")) pm.registerEvents(new JoinSpeedListener(), this);
    }
}

