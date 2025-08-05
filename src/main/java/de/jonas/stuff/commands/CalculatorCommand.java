package de.jonas.stuff.commands;

import java.util.List;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CalculatorCommand {

    Stuff stuff = Stuff.INSTANCE;

    public CalculatorCommand() {
        // Create our command
        List<String> aliases = stuff.getConfig().getStringList("EnableCalculatorCommand.Aliases");
        String suggestion = "number";
        new CommandAPICommand("stuff:calculator")
                .withAliases(aliases.toArray(new String[aliases.size()]))
                .withSubcommand(new CommandAPICommand("+")
                        .withArguments(new DoubleArgument((suggestion + " 1")))
                        .withArguments(new DoubleArgument((suggestion + " 2")))
                        .executesPlayer((player, args) -> {
                            var mm = MiniMessage.miniMessage();
                            double arg1 = (double) args.get((suggestion + " 1"));
                            double arg2 = (double) args.get((suggestion + " 2"));

                            double result = arg1 + arg2;

                            Component msg = mm.deserialize(arg1 + " + " + arg2 + " = <green>" + result);
                            player.sendMessage(msg);
                        })
                ).withSubcommand(new CommandAPICommand("-")
                        .withArguments(new DoubleArgument((suggestion + " 1")))
                        .withArguments(new DoubleArgument((suggestion + " 2")))
                        .executesPlayer((player, args) -> {
                            var mm = MiniMessage.miniMessage();
                            double arg1 = (double) args.get((suggestion + " 1"));
                            double arg2 = (double) args.get((suggestion + " 2"));
                            double result = arg1 - arg2;

                            Component msg = mm.deserialize(arg1 + " - " + arg2 + " = <green>" + result);
                            player.sendMessage(msg);
                        })
                ).withSubcommand(new CommandAPICommand("*")
                        .withArguments(new DoubleArgument((suggestion + " 1")))
                        .withArguments(new DoubleArgument((suggestion + " 2")))
                        .executesPlayer((player, args) -> {
                            var mm = MiniMessage.miniMessage();
                            double arg1 = (double) args.get((suggestion + " 1"));
                            double arg2 = (double) args.get((suggestion + " 2"));
                            double result = arg1 * arg2;

                            Component msg = mm.deserialize(arg1 + " x " + arg2 + " = <green>" + result);
                            player.sendMessage(msg);
                        })
                ).withSubcommand(new CommandAPICommand("/")
                        .withArguments(new DoubleArgument((suggestion + " 1")))
                        .withArguments(new DoubleArgument((suggestion + " 2")))
                        .executesPlayer((player, args) -> {
                            var mm = MiniMessage.miniMessage();
                            double arg1 = (double) args.get((suggestion + " 1"));
                            double arg2 = (double) args.get((suggestion + " 2"));
                            double result = arg1 / arg2;

                            Component msg = mm.deserialize(arg1 + " / " + arg2 + " = <green>" + result);
                            player.sendMessage(msg);
                        })
                ).register();
    }
}