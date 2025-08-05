package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import me.gaminglounge.configapi.Language;
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
    public SpeedCommand() {
        Stuff stuff = Stuff.INSTANCE;
        FileConfiguration conf = stuff.getConfig();
        List<String> aliasesF = conf.getStringList("FlySpeedCommand.Aliases");
        List<String> aliasesW = conf.getStringList("WalkSpeedCommand.Aliases");
        var mm = MiniMessage.miniMessage();

        if (conf.getBoolean("FlySpeedCommand.Enabled")) {
            new CommandAPICommand("stuff:flyspeed")
                    .withPermission(conf.getString("FlySpeedCommand.Permission"))
                    .withAliases(aliasesF.toArray(new String[aliasesF.size()]))
                    .withArguments(new IntegerArgument("speed", -10, 10))
                    .withOptionalArguments(new PlayerArgument("player"))
                    .executes((executor, args) -> {
                        Player target = (Player) args.get("player");
                        float speed = (int) args.get("speed") / 10.0f;

                        if (executor instanceof Player && target == null) target = (Player) executor;

                        target.getPersistentDataContainer().set(flySpeedIdentifier, PersistentDataType.FLOAT, speed);
                        target.setFlySpeed(speed);

                        int fullspeed = (int) (speed * 10);

                        if (target == executor || !(executor instanceof Player)) {
                            target.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, target, "flyspeed.self.newSpeed", true),
                                    Placeholder.component("speed", Component.text(fullspeed))));
                        } else {
                            Player executorP = (Player) executor;
                            target.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, target, "flyspeed.other.SelfNewSpeed", true),
                                    Placeholder.component("player", executorP.teamDisplayName()), Placeholder.component("speed",
                                            Component.text(fullspeed))));
                            if (executor instanceof Player p) {
                                executor.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, p, "flyspeed.other.OtherNewSpeed", true),
                                Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed",
                                        Component.text(fullspeed))));
                            } else {
                                executor.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, "en_US", "flyspeed.other.OtherNewSpeed", true),
                                Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed",
                                        Component.text(fullspeed))));
                            }
                        }
                    })
                    .register();
            stuff.increaseCommandCount();
        }

            if (conf.getBoolean("WalkSpeedCommand.Enabled")) {
                new CommandAPICommand("stuff:walkSpeed")
                        .withPermission(conf.getString("WalkSpeedCommand.Permission"))
                        .withAliases(aliasesW.toArray(new String[aliasesW.size()]))
                        .withArguments(new IntegerArgument("speed", -10, 10))
                        .withOptionalArguments(new PlayerArgument("player"))
                        .executes((executor, args) -> {
                            Player target = (Player) args.get("player");
                            float speed = (int) args.get("speed") / 10.0f;

                            if (executor instanceof Player && target == null) target = (Player) executor;

                            target.getPersistentDataContainer().set(walkSpeedIdentifier, PersistentDataType.FLOAT, speed);

                            target.setWalkSpeed(speed);

                            int fullspeed = (int) (speed * 10);

                            if (target == executor || !(executor instanceof Player)) {
                                target.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, target, "walkspeed.self.newSpeed", true),
                                        Placeholder.component("speed", Component.text(fullspeed))));
                            } else {
                                Player executorP = (Player) executor;
                                target.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, target, "walkspeed.other.SelfNewSpeed", true),
                                        Placeholder.component("player", executorP.teamDisplayName()), Placeholder.component("speed",
                                                Component.text(fullspeed))));
                                if (executor instanceof Player p) {
                                    executor.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, p, "walkspeed.other.OtherNewSpeed", true),
                                    Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed",
                                            Component.text(fullspeed))));
                                } else {
                                    executor.sendMessage(mm.deserialize(Language.getValue(Stuff.INSTANCE, "en_US", "walkspeed.other.OtherNewSpeed", true),
                                    Placeholder.component("player", target.teamDisplayName()), Placeholder.component("speed",
                                            Component.text(fullspeed))));
                                }
                            }
                        })
                        .register();
                stuff.increaseCommandCount();
            }
    }
}
