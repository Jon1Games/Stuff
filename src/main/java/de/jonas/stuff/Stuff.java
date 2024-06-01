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

    public void onLoad() {
        INSTANCE = this;

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));

        if (getConfig().getBoolean("Format.PlayerNames.Enabled")) {
            teamDisplaynameSet = new TeamDisplaynameSet();
            teamDisplaynameSet.onLoad();
        }

        if (getConfig().getBoolean("EnableCalculatorCommand.Enabled")) new CalculatorCommand();
        if (getConfig().getBoolean("MsgCommand.Enabled")) new MsgCommand();
        if (getConfig().getBoolean("FlyCommand.Enabled")) new FlyCommand();
        new SpeedCommand();
        if (getConfig().getBoolean("GamemodeCommand.Enabled")) new GamemodeCommand();
        if (getConfig().getBoolean("PortableInventoryCommand.Enabled")) new PortableInventoryCommand();
        if (getConfig().getBoolean("InfoCommands.Enabled")) new InfoCommands();
        new ReloadCommand();
        if (getConfig().getBoolean("PlayTimeCommand.Enabled")) new PlayTimeCommand();
        if (getConfig().getBoolean("PingCommand.Enabled")) new PingCommand();
        if (getConfig().getBoolean("CommandCommand.Enabled")) new CommandCommand();
        if (getConfig().getBoolean("BroadcastCommand.Enabled")) new BroadcastCommand();
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

        this.listener();

        this.saveDefaultConfig();

        getLogger().log(Level.INFO, "Activated Plugin");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        CommandAPI.onDisable();

        getLogger().log(Level.INFO,"Plugin deaktiviert.");
    }

    public void listener() {
        PluginManager pm = Bukkit.getPluginManager();

        if (getConfig().getBoolean("CustomJoinQuitMessage.Enabled")) pm.registerEvents(new JoinQuitMessageListener(), this);
        if (getConfig().getBoolean("FlyCommand.Enabled")) pm.registerEvents(new JoinFlyListener(), this);
        if (getConfig().getBoolean("SpeedCommand.Enabled")) pm.registerEvents(new JoinSpeedListener(), this);
        if (getConfig().getBoolean("Format.Chat.Enabled")) pm.registerEvents(new ChatListener(), this);
        if (getConfig().getBoolean("Format.PlayerNames.Enabled")) pm.registerEvents(teamDisplaynameSet, this);
    }
}

