package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class CalculatorCommand {

    Stuff stuff = new Stuff();

    public CalculatorCommand() {
        // Create our command
        List<String> aliases = stuff.getConfig().getStringList("Aliases");
        String suggestion = stuff.getConfig().getString("EnableCalculatorCommand.suggestionName.Number");
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