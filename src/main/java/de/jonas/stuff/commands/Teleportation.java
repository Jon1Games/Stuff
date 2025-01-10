package de.jonas.stuff.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import de.jonas.stuff.Stuff;
import de.jonas.stuff.utility.ScheudulerRunLaterForX;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class Teleportation implements Listener {

    MiniMessage mm = MiniMessage.miniMessage();
    Stuff stuff = Stuff.INSTANCE;

    Map<Player, Map<Player, Long>> a = new HashMap<>(); // TPA list
    Map<Player, Long> b = new HashMap<>(); // move while in tpa 
    Map<Player, Long> c = new HashMap<>(); // cooldown TPA
    Map<Player, Long> d = new HashMap<>(); // cooldown RTP

    public Teleportation() {
        // Create our command
        List<String> aliases_SPAWN = stuff.getConfig().getStringList("TeleportCommands.Spawn.Aliases");
        List<String> aliases_SETSPAWN = stuff.getConfig().getStringList("TeleportCommands.SetSpawn.Aliases");
        List<String> aliases_TPA = stuff.getConfig().getStringList("TeleportCommands.TPA.Aliases");
        List<String> aliases_TPAC = stuff.getConfig().getStringList("TeleportCommands.TPAACCEPT.Aliases");
        List<String> aliases_TPAD = stuff.getConfig().getStringList("TeleportCommands.TPADECLINE.Aliases");
        List<String> aliases_RTP = stuff.getConfig().getStringList("TeleportCommands.RTP.Aliases");
        String suggestion = stuff.getConfig().getString("TeleportCommands.suggestionName.Player");

        if (stuff.getConfig().getBoolean("TeleportCommands.Spawn.Enabled")) {
            new CommandAPICommand("stuff:spawn")
                .withPermission(stuff.getConfig().getString("TeleportCommands.Spawn.Permission"))
                .withAliases(aliases_SPAWN.toArray(new String[aliases_SPAWN.size()]))
                .executesPlayer(((player, commandArguments) -> {
                        player.teleport(stuff.getSpawn());
                    }))
                .register();
                stuff.increaseCommandCount();
        }

        if (stuff.getConfig().getBoolean("TeleportCommands.SetSpawn.Enabled")) {
            new CommandAPICommand("stuff:setspawn")
                .withPermission(stuff.getConfig().getString("TeleportCommands.SetSpawn.Permission"))
                .withAliases(aliases_SETSPAWN.toArray(new String[aliases_SETSPAWN.size()]))
                .executesPlayer(((player, commandArguments) -> {
                            stuff.getConfig().set("TeleportCommands.Spawn.World", player.getWorld().getName());
                            stuff.getConfig().set("TeleportCommands.Spawn.Pos_X", player.getX());
                            stuff.getConfig().set("TeleportCommands.Spawn.Pos_Y", player.getY());
                            stuff.getConfig().set("TeleportCommands.Spawn.Pos_Z", player.getZ());
                            stuff.getConfig().set("TeleportCommands.Spawn.Pos_YAW", player.getYaw());
                            stuff.getConfig().set("TeleportCommands.Spawn.Pos_PITCH", player.getPitch());
                            stuff.saveConfig();
                            player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.newSpawn")));
                    }))
                .register();
                stuff.increaseCommandCount();
        }

        if (stuff.getConfig().getBoolean("TeleportCommands.TPA.Enabled")) {
            new CommandAPICommand("stuff:tpa")
                .withPermission(stuff.getConfig().getString("TeleportCommands.TPA.Permission"))
                .withAliases(aliases_TPA.toArray(new String[aliases_TPA.size()]))
                .withOptionalArguments(new PlayerArgument(suggestion))
                .executesPlayer(((player, commandArguments) -> {
                    if(
                        !player.hasPermission(stuff.getConfig().getString("TeleportCommands.TPA.CooldownBypassPermission")) && 
                        d.containsKey(player)) 
                        {
                        Long gp = c.get(player);
                        Long sc = System.currentTimeMillis(); 
                        if(gp <= sc) {
                            d.remove(player);
                        } else {
                            player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.cooldownTPA"),
                                Placeholder.component("time_left", Component.text((sc - gp) / 1_000))
                            ));
                            return;
                        }
                    }
                    Player target = (Player) commandArguments.get(suggestion);
                    if (target == null) {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.noDestination")));
                    } else if (target == player) {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.tpaToSelf")));
                    } else {
                        tpa(player, target, System.currentTimeMillis() + stuff.getConfig().getInt("TeleportCommands.TPA.RequestDuration") * 1_000);
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.tpaSender"),
                            Placeholder.component("player", target.displayName())
                        ));
                        target.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.tpaRCP"),
                            Placeholder.styling("accept", ClickEvent.runCommand("/stuff:tpaaccept " + player.getName())),
                            Placeholder.styling("decline", ClickEvent.runCommand("/stuff:tpadecline " + player.getName())),
                            Placeholder.component("player", player.displayName())
                            ));
                    }}))
                .register();
                stuff.increaseCommandCount();
        }

        if (stuff.getConfig().getBoolean("TeleportCommands.TPAACCEPT.Enabled")) {
        new CommandAPICommand("stuff:tpaaccept")
            .withPermission(stuff.getConfig().getString("TeleportCommands.TPAACCEPT.Permission"))
            .withAliases(aliases_TPAC.toArray(new String[aliases_TPAC.size()]))
            .withOptionalArguments(new PlayerArgument(suggestion))
            .executesPlayer(((player, commandArguments) -> {
                Player target = (Player) commandArguments.get(suggestion);
                if (target == null) {
                    player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.noAcceptor")));
                } else if (target == player) {
                    player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.acceptSelf")));
                } else {
                    if (!tpac(player, target)) {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.noTPA")));
                    }
                }}))
            .register();
            stuff.increaseCommandCount();
        }

        if (stuff.getConfig().getBoolean("TeleportCommands.TPADECLINE.Enabled")) {
            new CommandAPICommand("stuff:tpadecline")
                .withPermission(stuff.getConfig().getString("TeleportCommands.TPADECLINE.Permission"))
                .withAliases(aliases_TPAD.toArray(new String[aliases_TPAD.size()]))
                .withOptionalArguments(new PlayerArgument(suggestion))
                .executesPlayer(((player, commandArguments) -> {
                    Player target = (Player) commandArguments.get(suggestion);
                    if (target == null) {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.noAcceptor")));
                    } else if (target == player) {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.acceptSelf")));
                    } else {
                        if (!tpad(player, target)) {
                            player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.noTPA")));
                        }
                    }}))
                .register();
                stuff.increaseCommandCount();
            }

        if (stuff.getConfig().getBoolean("TeleportCommands.RTP.Enabled")) {
            new CommandAPICommand("stuff:rtp")
                .withPermission(stuff.getConfig().getString("TeleportCommands.RTP.Permission"))
                .withAliases(aliases_RTP.toArray(new String[aliases_RTP.size()]))
                .executesPlayer(((player, commandArguments) -> {
                    if(
                        !player.hasPermission(stuff.getConfig().getString("TeleportCommands.RTP.CooldownBypassPermission")) && 
                        d.containsKey(player)) 
                        {
                        Long gp = d.get(player);
                        Long sc = System.currentTimeMillis(); 
                        if(gp <= sc) {
                            d.remove(player);
                        } else {
                            player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.cooldownRTP"),
                                Placeholder.component("time_left", Component.text((gp - sc) / 1_000))
                            ));
                            return;
                        }
                    }

                    World pw = player.getWorld();
                    
                    if (stuff.getConfig().get("TeleportCommands.RTP.Worlds." + pw.getName()) == null) {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.noWorld"),
                            Placeholder.component("world", mm.deserialize(pw.getName()))
                        ));
                        return;
                    }
                    if (!stuff.getConfig().getBoolean("TeleportCommands.RTP.Worlds." + pw.getName() + ".Enabled")) {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.worldDisabled"),
                            Placeholder.component("world", mm.deserialize(pw.getName()))
                        ));
                        return;
                    }
                    if (!player.hasPermission(stuff.getConfig().getString("TeleportCommands.RTP.Worlds." + pw.getName() + ".Permission"))) {
                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.noPerms"),
                            Placeholder.component("world", mm.deserialize(pw.getName()))
                        ));
                        return;
                    }

                    Location center = pw.getWorldBorder().getCenter();
                    double maxRadius = Math.min(pw.getWorldBorder().getSize(), stuff.getConfig().getInt(
                        "TeleportCommands.RTP.Worlds." + pw.getName() + ".MaxDistance"
                        ));
                    double minRadius = Math.min(pw.getWorldBorder().getSize(), stuff.getConfig().getInt(
                        "TeleportCommands.RTP.Worlds." + pw.getName() + ".MinDistance"
                        ));
                    
                    int minY =
                        stuff.getConfig().isInt("TeleportCommands.RTP.Worlds." + pw.getName() + ".MinY") ? 
                        stuff.getConfig().getInt("TeleportCommands.RTP.Worlds." + pw.getName() + ".MinY") :
                        Integer.MIN_VALUE;
                    int maxY =
                        stuff.getConfig().isInt("TeleportCommands.RTP.Worlds." + pw.getName() + ".MaxY") ? 
                        stuff.getConfig().getInt("TeleportCommands.RTP.Worlds." + pw.getName() + ".MaxY") :
                        Integer.MAX_VALUE;
                    
                    player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.searchingForPos")));
                    Bukkit.getServer().getScheduler().runTaskAsynchronously(stuff, () -> {
                        do { 
                            double phi = Math.random() * 2 * Math.PI;
                            double r = (maxRadius - minRadius) * Math.random() + minRadius;
                            double x = Math.cos(phi) * r + center.blockX();
                            double z = Math.sin(phi) * r + center.blockZ();
                            Location target = safeLocation(new Location(pw, x, player.getLocation().getY(), z), minY, maxY);
                            if (target != null) {
                                player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.saveLocationFound")));
                                Location loc = player.getLocation();
                                ScheudulerRunLaterForX.runTaskForX(stuff, cycles -> {
                                    if (!loc.equals(player.getLocation())) {
                                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.moved")));
                                        return false;
                                    }
                                    if (cycles > 0) {
                                        player.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.countdown"),
                                            Placeholder.component("time_left", Component.text(cycles))
                                        ));
                                    } else {
                                        player.teleportAsync(target);
                                        d.put(player, System.currentTimeMillis() + (stuff.getConfig().getLong("TeleportCommands.RTP.Cooldown") * 1_000));
                                    } 
                                    return true;
                                }, 20, stuff.getConfig().getInt("TeleportCommands.TPA.NotMoveFor"));
                                break;
                            }
                        } while (true);
                    });
                }))
                .register();
            stuff.increaseCommandCount();
        }
    }

    public void onEnable() {
        //setup tpa cleaner for memory saving
        Bukkit.getScheduler().scheduleSyncRepeatingTask(stuff, () -> {
            long now = System.currentTimeMillis();
            a.entrySet().forEach(ent -> ent.getValue().entrySet().removeIf(ent2 -> ent2.getValue() >= now));
            a.entrySet().removeIf(ent -> ent.getValue().isEmpty());
        }, 0, 6000);
    }

    void tpa(Player sender, Player reciver, Long timeout) {
        if(!a.containsKey(reciver)) a.put(reciver, new HashMap<>());
        a.get(reciver).put(sender, timeout);
        c.put(sender, System.currentTimeMillis() + (stuff.getConfig().getLong("TeleportCommands.TPA.Cooldown") * 1_000));
    }
    
    boolean tpac(Player acceptor, Player sender) {
        if(!a.containsKey(acceptor)) return false;
        if(!a.get(acceptor).containsKey(sender)) return false;
        if(a.get(acceptor).get(sender) < System.currentTimeMillis()) return false;
        acceptor.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.tpaAccepted"),
            Placeholder.component("player", sender.name())
        ));
        Location loc = sender.getLocation();
        ScheudulerRunLaterForX.runTaskForX(stuff, cycles -> {
            if (!loc.equals(sender.getLocation())) {
                sender.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.moved")));
                return false;
            }
            if (cycles > 0) {
                sender.sendMessage(mm.deserialize(stuff.getConfig().getString("TeleportCommands.Messages.countdown"),
                    Placeholder.component("time_left", Component.text(cycles))
                ));
            } else {
                sender.teleport(acceptor.getLocation());
            }         
            return true;
        }, 20, stuff.getConfig().getInt("TeleportCommands.TPA.NotMoveFor"));
        a.get(acceptor).remove(sender);
        return true;
    }  
    
    boolean tpad(Player deliner, Player sender) {
        if(!a.containsKey(deliner)) return false;
        if(!a.get(deliner).containsKey(sender)) return false;
        a.get(deliner).remove(sender);
        return true;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        if(a.containsKey(e.getPlayer())) a.remove(e.getPlayer());
        for(Entry<Player, Map<Player, Long>> ent : a.entrySet())
            if(ent.getValue().containsKey(e.getPlayer())) ent.getValue().remove(e.getPlayer());
    }

    public static double alignChunk(double v) {
        return v - (v % 16) + (v < 0 ? -16 : 0);
    }

    public static boolean isUnsafeMaterial(Material mat) {
        return
            mat == null ||
            mat == Material.WATER ||
            mat == Material.LAVA ||
            mat == Material.FIRE ||
            mat == Material.KELP ||
            mat == Material.KELP_PLANT ||
            mat == Material.SEAGRASS ||
            mat == Material.TALL_SEAGRASS ||
            mat == Material.BUBBLE_COLUMN ||
            mat == Material.CACTUS ||
            mat == Material.WITHER_ROSE ||
            mat == Material.SWEET_BERRY_BUSH ||
            mat == Material.POWDER_SNOW ||
            mat == Material.MAGMA_BLOCK ||
            false;
    }

    public static boolean isSafeGround(Material mat) {
        return
            !isUnsafeMaterial(mat) &&
            mat.isCollidable() &&
            true;
    }

    public static boolean isSafeAir(Material mat) {
        return
            !isUnsafeMaterial(mat) &&
            !mat.isCollidable() &&
            true;
    }

    public static Vector checkCunkCulumn(int gx, int gz, int minY, int maxY, ChunkSnapshot chunk) {
        boolean[] cache = new boolean[3];
        int cx = gx & 0xF;
        int cz = gz & 0xF;

        cache[1] = isSafeAir(chunk.getBlockType(cx, maxY - 1, cz));
        cache[0] = isSafeAir(chunk.getBlockType(cx, maxY - 2, cz));
        for(int y = maxY - 3; y > minY; y--) {
            cache[2] = cache[1];
            cache[1] = cache[0];

            var b = chunk.getBlockType(cx, y, cz);

            if(
                cache[2] &&
                cache[1] &&
                isSafeGround(b) &&
                true) return new Vector(gx, y, gz);

            cache[0] = isSafeAir(b);
        }
        return null;
    }

    public static Location safeLocation(Location oSource) {
        return safeLocation(oSource, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static Location safeLocation(Location oSource, int oMinY, int oMaxY) {
        var source = oSource.clone();
        var world = source.getWorld();
        // center in chunk
        source.setX(alignChunk(source.getX()) + 8);
        source.setZ(alignChunk(source.getZ()) + 8);
        // general information
        int minY = Math.max(oMinY, world.getMinHeight());
        int maxY = Math.min(oMaxY, world.getMaxHeight());
        int x = source.getBlockX();
        int z = source.getBlockZ();
        // get chunk
        var chunk = world.getChunkAtAsync(source, true).thenApply(c -> c.getChunkSnapshot(false, false, false)).join();

        Vector tmp = checkCunkCulumn(x, z, minY, maxY, chunk);
        for(int bx = 2; bx > -3 && tmp == null; bx--) {
            for(int bz = 2; bz > -3 && tmp == null; bz--) {
                if(bx == 0 && bz == 0) continue;
                tmp = checkCunkCulumn(x + bx, z + bz, minY, maxY, chunk);
            }
        }
        if(tmp != null) return tmp.toLocation(world).add(0.5, 1, 0.5);
        return null;
    }

}

