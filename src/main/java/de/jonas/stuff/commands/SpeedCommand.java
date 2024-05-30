package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SpeedCommand {
    public static final NamespacedKey flySpeedIdentifier = new NamespacedKey("stuff", "fly_speed");
    public static final NamespacedKey walkSpeedIdentifier = new NamespacedKey("stuff", "walk_speed");
    @SuppressWarnings("null")
    public SpeedCommand() {
        Stuff stuff = Stuff.INSTANCE;
        FileConfiguration conf = stuff.getConfig();
        String suggestionSF = conf.getString("FlySpeedCommand.suggestionName.Speed");
        String suggestionSW = conf.getString("WalkSpeedCommand.suggestionName.Speed");
        String suggestionPF = conf.getString("FlySpeedCommand.suggestionName.Player");
        String suggestionPW = conf.getString("WalkSpeedCommand.suggestionName.Player");
        List<String> aliasesF = conf.getStringList("FlySpeedCommand.Aliases");
        List<String> aliasesW = conf.getStringList("WalkSpeedCommand.Aliases");
        var mm = MiniMessage.miniMessage();

        if (conf.getBoolean("FlySpeedCommand.Enabled")) {
            new CommandAPICommand("stuff:flyspeed")
                    .withPermission(conf.getString("FlySpeedCommand.Permission"))
                    .withAliases(aliasesF.toArray(new String[aliasesF.size()]))
                    .withArguments(new IntegerArgument(suggestionSF, -10, 10))
                    .withOptionalArguments(new PlayerArgument(suggestionPF))
                    .executes((executor, args) -> {
                        Player target = (Player) args.get(suggestionPF);
                        float speed = (int) args.get(suggestionSF) / 10.0f;

                        if (executor instanceof Player && target == null) target = (Player) executor;

                        target.getPersistentDataContainer().set(flySpeedIdentifier, PersistentDataType.FLOAT, speed);
                        target.setFlySpeed(speed);

                        int fullspeed = (int) (speed * 10);

                        if (target == executor || !(executor instanceof Player)) {
                            target.sendMessage(mm.deserialize(conf.getString("FlySpeedCommand.Messages.Self.NewSpeed"),
                                    Placeholder.component("speed", Component.text(fullspeed))));
                        } else {
                            Player executorP = (Player) executor;
                            target.sendMessage(mm.deserialize(conf.getString("FlySpeedCommand.Messages.Other.Other.NewSpeed"),
                                    Placeholder.component("player", executorP.teamDisplayName()), Placeholder.component("speed",
                                            Component.text(fullspeed))));
                            executor.sendMessage(mm.deserialize(conf.getString("FlySpeedCommand.Messages.Other.Self.NewSpeed"),
                                    Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed",
                                            Component.text(fullspeed))));
                        }
                    })
                    .register();
        }

            if (conf.getBoolean("WalkSpeedCommand.Enabled")) {
                new CommandAPICommand("stuff:walkSpeed")
                        .withPermission(conf.getString("WalkSpeedCommand.Permission"))
                        .withAliases(aliasesW.toArray(new String[aliasesW.size()]))
                        .withArguments(new IntegerArgument(suggestionSW, -10, 10))
                        .withOptionalArguments(new PlayerArgument(suggestionPW))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get(suggestionPW);
                            float speed = (int) args.get(suggestionSW) / 10.0f;

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.getPersistentDataContainer().set(walkSpeedIdentifier, PersistentDataType.FLOAT, speed);

                            target.setWalkSpeed(speed);

                            int fullspeed = (int) (speed * 10);

                            if (target == executor || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(conf.getString("SpeedCommand.Messages.Self.NewSpeed"),
                                        Placeholder.component("speed", Component.text(fullspeed))));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(conf.getString("SpeedCommand.Messages.Other.Other.NewSpeed"),
                                        Placeholder.component("player", executorP.teamDisplayName()), Placeholder.component("speed",
                                                Component.text(fullspeed))));
                                executor.sendMessage(mm.deserialize(conf.getString("SpeedCommand.Messages.Other.Self.NewSpeed"),
                                        Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed",
                                                Component.text(fullspeed))));
                            }
                        })
                        .register();
            }
    }
}
