package de.jonas.stuff.commands;

import de.jonas.stuff.Stuff;
import dev.jorel.commandapi.CommandAPICommand;

public class PortableInventoryCommand {

    public PortableInventoryCommand() {
        Stuff stuff = Stuff.INSTANCE;

        new CommandAPICommand("workbench")
                .withAliases("wb", "craft", "werkbank")
                .withPermission(stuff.getConfig().getString("PortableInventoryCommand.WorkbenchPermission"))
                .executesPlayer((player, arsg) -> {
                    player.openWorkbench(player.getLocation(), true);
                })
                .register();

        new CommandAPICommand("anvil")
                .withAliases("av", "Ambos")
                .withPermission(stuff.getConfig().getString("PortableInventoryCommand.AnvilPermission"))
                .executesPlayer((player, args) -> {
                    player.openAnvil(player.getLocation(), true);
                })
                .register();

        new CommandAPICommand("grindstone")
                .withAliases("gs", "Schleifstein")
                .withPermission(stuff.getConfig().getString("PortableInventoryCommand.GrindstonePermission"))
                .executesPlayer((player, args) -> {
                    player.openGrindstone(player.getLocation(), true);
                })
                .register();

        new CommandAPICommand("smithingtable")
                .withAliases("st", "Schmiedetisch")
                .withPermission(stuff.getConfig().getString("PortableInventoryCommand.SmithingtablePermission"))
                .executesPlayer((player, args) -> {
                    player.openSmithingTable(player.getLocation(), true);
                })
                .register();
    }
}
