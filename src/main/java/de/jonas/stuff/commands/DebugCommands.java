package de.jonas.stuff.commands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;

import de.jonas.stuff.Stuff;
import de.jonas.stuff.commands.debugCommands.MaterialInfo;
import de.jonas.stuff.interfaced.BlockBreakWithItemEvent;
import de.jonas.stuff.interfaced.BreakEvent;
import de.jonas.stuff.interfaced.PlaceEvent;
import de.jonas.stuff.utility.ItemBuilder;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BlockStateArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.wrappers.Location2D;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class DebugCommands {

    MiniMessage mm = MiniMessage.miniMessage();
    Stuff stuff = Stuff.INSTANCE;
    
    private static final PlaceEvent testPlace = DebugCommands::testPlaceI;
    private static final BreakEvent testBreak = DebugCommands::testBreakI;
    private static final BlockBreakWithItemEvent testBreakWItem = DebugCommands::testBreakWItemI;

    public void events() {
        Stuff.INSTANCE.itemBuilderManager.addPlaceEvent(testPlace, "stuff-test:placed");
        Stuff.INSTANCE.itemBuilderManager.addBreakEvent(testBreak, "stuff-test:broken");
        Stuff.INSTANCE.itemBuilderManager.addItemBreakBlockEvent(testBreakWItem, "stuff-test:broken_w_item");
    }

    public DebugCommands() {

        for (String a : stuff.getConfig().getStringList("DebugCommands.unregister")) {
            CommandAPI.unregister(a);
        }

        List<String> aliases = stuff.getConfig().getStringList("DebugCommands.Aliases");

        new CommandAPICommand("stuff:debug")
            .withPermission(stuff.getConfig().getString("PATH"))
            .withAliases(aliases.toArray(new String[aliases.size()]))
            .withSubcommand(new CommandAPICommand("blockMaterial")
                    .withArguments(new BlockStateArgument("block"))
                    .executes((sender, arguments) -> {
                        BlockData blockdata = (BlockData) arguments.get("block");
                        if(blockdata == null) {
                            sender.sendMessage(mm.deserialize("<red>You didn't give a block state.</red>"));
                            return;
                        }
                        var mat = blockdata.getMaterial();
                        sender.sendMessage(materialInfo(mat));
                    })
                )
            .withSubcommand(new CommandAPICommand("itemMaterial")
                    .withArguments(new ItemStackArgument("item"))
                    .executes((sender, arguments) -> {
                        ItemStack item = (ItemStack) arguments.get("item");
                        if(item == null) {
                            sender.sendMessage(mm.deserialize("<red>You didn't give an itemstack.</red>"));
                            return;
                        }
                        var mat = item.getType();
                        sender.sendMessage(materialInfo(mat));
                    })
                )
            .withSubcommand(new CommandAPICommand("safeLocation")
                    .withArguments(new Location2DArgument("position", LocationType.BLOCK_POSITION))
                    .withOptionalArguments(new IntegerArgument("minY"))
                    .withOptionalArguments(new IntegerArgument("maxY"))
                    .executes((sender, arguments) -> {
                        Location2D position = (Location2D) arguments.get("position");
                        if(position == null) {
                            sender.sendMessage(mm.deserialize("<red>You didn't give a position.</red>"));
                            return;
                        }
                        Integer tmp = (Integer) arguments.get("minY");
                        int minY = tmp != null ? tmp : Integer.MIN_VALUE;
                        tmp = (Integer) arguments.get("maxY");
                        int maxY = tmp != null ? tmp : Integer.MAX_VALUE;

                        sender.sendMessage(mm.deserialize("<aqua>Searching...</aqua>"));
                        Bukkit.getServer().getScheduler().runTaskAsynchronously(stuff, () -> {
                            Location safeLocation = Teleportation.safeLocation(position, minY, maxY);

                            if(safeLocation == null) {
                                sender.sendMessage(mm.deserialize("<red>No safe location found.</red>"));
                            } else {
                                String poss = safeLocation.getBlockX() + " " + safeLocation.getBlockY() + " " + safeLocation.getBlockZ();
                                sender.sendMessage(mm.deserialize("<aqua>Safe location found at </aqua><yellow>" + poss
                                        + "</yellow> <green><click:run_command:/tp " + poss
                                        + ">Teleport</click></green>"));
                            }
                        });
                    })
                )
            .withSubcommand(new CommandAPICommand("listener")
                    .withArguments(new GreedyStringArgument("class"))
                    .executes((executor, args) -> {
                        String clazzName = (String) args.get("class");
                        try {
                            Class<?> c = Class.forName(clazzName);
                            if(!Event.class.isAssignableFrom(c)) {
                                executor.sendMessage(mm.deserialize("<red>The Class is not an Event!</red>"));
                                return;
                            }
                            @SuppressWarnings("unchecked")
                            HandlerList handlerList = getEventListeners((Class<? extends Event>) c);
                            executor.sendMessage(mm.deserialize("<aqua>Listeners found for \"</aqua><yellow>" + clazzName + "</yellow><aqua>\":</aqua>"));
                            for(RegisteredListener rl : handlerList.getRegisteredListeners()) {
                                executor.sendMessage(mm.deserialize("<aqua>-</aqua>"
                                        + "<yellow>" + rl.getListener().getClass().getName() + "</yellow> "
                                        + "<aqua>@</aqua> <yellow>" + rl.getPlugin().getName() + "</yellow> "
                                        + "<aqua>with EventPriority:</aqua> <yellow>" + rl.getPriority().name() + "</yellow>"));
                            }
                        } catch(ClassNotFoundException e) {
                            executor.sendMessage(mm.deserialize("<red>Class not found:</red> <yellow>" + e.getMessage() + "</yellow>"));
                        } catch(NoSuchMethodException e) {
                            executor.sendMessage(mm.deserialize("<red>Method not found:</red> <yellow>" + e.getMessage() + "</yellow>"));
                        } catch(Exception e) {
                            executor.sendMessage(mm.deserialize("<red>An Exception was Thrown:</red> <yellow>" + e.getMessage() + "</yellow>"));
                        }
                    })
                )
            .withSubcommand(new CommandAPICommand("ItemBuilder")
                    .withSubcommand(new CommandAPICommand("PlaceBreakBlock")
                        .executesPlayer((player, args) -> {
                            player.getInventory().addItem(new ItemBuilder()
                                .setName("PlaceBreack Test Block")
                                .setMaterial(Material.GLASS)
                                .whenPlaced("stuff-test:placed")
                                .whenBroken("stuff-test:broken")
                                .whenBrokenWithItem("stuff-test:broken_w_item")
                                .build()
                            );
                        })
                    )
            )
        .register();
    }
    
    public static String mmIfBlock(Material mat, Supplier<String> sup) {
        return mat.isBlock() ? sup.get() : "<red>This is not a block</red>";
    }
    public static String mmIfItem(Material mat, Supplier<String> sup) {
        return mat.isItem() ? sup.get() : "<red>This is not an item</red>";
    }
    public static String mmBoolean(boolean value) {
        return value ? "<green>true</green>" : "<red>false</red>";
    }
    public static String mmString(String value) {
        return "<green>" + value + "</green>";
    }
    public static String mmFloat(float value) {
        return "<green>" + value + "f</green>";
    }
    public static String mmInt(int value) {
        return "<green>" + value + "</green>";
    }
    public static String mmShort(short value) {
        return "<green>" + value + "s</green>";
    }

    private static final List<Function<Material, MaterialInfo>> materialInfos = new ArrayList<>();
    static {
        materialInfos.add(mat -> new MaterialInfo("getBlastResistance()", mmIfBlock(mat, () -> mmFloat(mat.getBlastResistance()))));
        materialInfos.add(mat -> new MaterialInfo("getEquipmentSlot()", mmIfItem(mat, () -> mmString(mat.getEquipmentSlot().toString()))));
        materialInfos.add(mat -> new MaterialInfo("getHardness()", mmIfBlock(mat, () -> mmFloat(mat.getHardness()))));
        materialInfos.add(mat -> new MaterialInfo("getKey()", mmString(mat.getKey().toString())));
        materialInfos.add(mat -> new MaterialInfo("getMaxDurability()", mmShort(mat.getMaxDurability())));
        materialInfos.add(mat -> new MaterialInfo("getMaxStackSize()", mmInt(mat.getMaxStackSize())));
        materialInfos.add(mat -> new MaterialInfo("getSlipperiness()", mmIfBlock(mat, () -> mmFloat(mat.getSlipperiness()))));
        materialInfos.add(mat -> new MaterialInfo("hasGravity()", mmBoolean(mat.hasGravity())));
        materialInfos.add(mat -> new MaterialInfo("isAir()", mmBoolean(mat.isAir())));
        materialInfos.add(mat -> new MaterialInfo("isBlock()", mmBoolean(mat.isBlock())));
        materialInfos.add(mat -> new MaterialInfo("isBurnable()", mmBoolean(mat.isBurnable())));
        materialInfos.add(mat -> new MaterialInfo("isCollidable()", mmIfBlock(mat, () -> mmBoolean(mat.isCollidable()))));
        materialInfos.add(mat -> new MaterialInfo("isCompostable()", mmBoolean(mat.isCompostable())));
        materialInfos.add(mat -> new MaterialInfo("isEdible()", mmBoolean(mat.isEdible())));
        materialInfos.add(mat -> new MaterialInfo("isEmpty()", mmBoolean(mat.isEmpty())));
        materialInfos.add(mat -> new MaterialInfo("isFlammable()", mmBoolean(mat.isFlammable())));
        materialInfos.add(mat -> new MaterialInfo("isFuel()", mmBoolean(mat.isFuel())));
        materialInfos.add(mat -> new MaterialInfo("isItem()", mmBoolean(mat.isItem())));
        materialInfos.add(mat -> new MaterialInfo("isLegacy()", mmBoolean(mat.isLegacy())));
        materialInfos.add(mat -> new MaterialInfo("isOccluding()", mmBoolean(mat.isOccluding())));
        materialInfos.add(mat -> new MaterialInfo("isRecord()", mmBoolean(mat.isRecord())));
        materialInfos.add(mat -> new MaterialInfo("isSolid()", mmBoolean(mat.isSolid())));
        materialInfos.add(mat -> new MaterialInfo("translationKey()", mmString(mat.translationKey())));
        materialInfos.add(mat -> new MaterialInfo("stuff.isUnsafeMaterial", mmIfBlock(mat, () -> mmBoolean(Teleportation.isUnsafeMaterial(mat)))));
        materialInfos.add(mat -> new MaterialInfo("stuff.isSafeGround", mmIfBlock(mat, () -> mmBoolean(Teleportation.isSafeGround(mat)))));
        materialInfos.add(mat -> new MaterialInfo("stuff.isSafeAir", mmIfBlock(mat, () -> mmBoolean(Teleportation.isSafeAir(mat)))));
    }

    public Component materialInfo(Material mat) {
        StringBuilder sb = new StringBuilder();
        sb.append("<aqua>Info for Material:</aqua> <yellow>");
        sb.append(mat.toString());
        sb.append("</yellow><br>");
        for(var f : materialInfos) {
            var g = f.apply(mat);
            sb.append("<aqua>-</aqua> <yellow>");
            sb.append(g.function());
            sb.append("</yellow><aqua>:</aqua> ");
            sb.append(g.value());
            sb.append("<br>");
        }

        return mm.deserialize(sb.toString());
    }

    private HandlerList getEventListeners(Class<? extends Event> type) throws Exception {
        Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
        method.setAccessible(true);
        return (HandlerList) method.invoke(null);
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) throws NoSuchMethodException {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new NoSuchMethodException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }

    private static void testPlaceI(BlockPlaceEvent e) {
        e.getPlayer().sendMessage("Place Event");
	}

    private static void testBreakI(BlockBreakEvent e) {
        e.getPlayer().sendMessage("Break Event");
	}

    private static void testBreakWItemI(BlockBreakEvent e) {
        e.getPlayer().sendMessage("Block Break With Item Event");
	}

}
