package de.jonas.stuff.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CalculatorCommand {

    public CalculatorCommand() {
        // Create our command
        new CommandAPICommand("Rechner")
                .withAliases("calc", "rechne")
                .withSubcommand(new CommandAPICommand("+")
                        .withArguments(new DoubleArgument("Zahl1"))
                        .withArguments(new DoubleArgument("Zahl2"))
                        .executesPlayer((player, args) -> {
                            var mm = MiniMessage.miniMessage();
                            double arg1 = (double) args.get("Zahl1");
                            double arg2 = (double) args.get("Zahl2");
                            double result = arg1 + arg2;

                            Component msg = mm.deserialize(arg1 + " + " + arg2 + " = <green>" + result);
                            player.sendMessage(msg);
                        })
                ).withSubcommand(new CommandAPICommand("-")
                        .withArguments(new DoubleArgument("Zahl1"))
                        .withArguments(new DoubleArgument("Zahl2"))
                        .executesPlayer((player, args) -> {
                            var mm = MiniMessage.miniMessage();
                            double arg1 = (double) args.get("Zahl1");
                            double arg2 = (double) args.get("Zahl2");
                            double result = arg1 - arg2;

                            Component msg = mm.deserialize(arg1 + " - " + arg2 + " = <green>" + result);
                            player.sendMessage(msg);
                        })
                ).withSubcommand(new CommandAPICommand("*")
                        .withArguments(new DoubleArgument("Zahl1"))
                        .withArguments(new DoubleArgument("Zahl2"))
                        .executesPlayer((player, args) -> {
                            var mm = MiniMessage.miniMessage();
                            double arg1 = (double) args.get("Zahl1");
                            double arg2 = (double) args.get("Zahl2");
                            double result = arg1 * arg2;

                            Component msg = mm.deserialize(arg1 + " x " + arg2 + " = <green>" + result);
                            player.sendMessage(msg);
                        })
                ).withSubcommand(new CommandAPICommand("/")
                        .withArguments(new DoubleArgument("Zahl1"))
                        .withArguments(new DoubleArgument("Zahl2"))
                        .executesPlayer((player, args) -> {
                            var mm = MiniMessage.miniMessage();
                            double arg1 = (double) args.get("Zahl1");
                            double arg2 = (double) args.get("Zahl2");
                            double result = arg1 / arg2;

                            Component msg = mm.deserialize(arg1 + " / " + arg2 + " = <green>" + result);
                            player.sendMessage(msg);
                        })
                ).register();
    }
}