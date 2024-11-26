package de.jonas.stuff.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Marker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import de.jonas.stuff.ItemBuilderManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.BreakEvent;

public class BlockBreakEvent implements Listener{

    public static final NamespacedKey prevent_break = new NamespacedKey("stuff", "not_break_block_entity");

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock() == null) return;
        PersistentDataContainer container = e.getItemInHand().getItemMeta().getPersistentDataContainer();
        if (!container.has(ItemBuilderManager.blockBreakEvent)) return;

        String pdv = container.get(ItemBuilderManager.blockBreakEvent, PersistentDataType.STRING);
        Entity et = e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(), EntityType.MARKER);
        et.getPersistentDataContainer().set(prevent_break, PersistentDataType.STRING, pdv);
    }

    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent e) {
        if (e.getBlock() == null) return;
        
        for (Entity et : e.getBlock().getLocation().getNearbyEntities(0.1, 0.1, 0.1)) {
            if (et instanceof Marker) {
                String pdv = et.getPersistentDataContainer().get(prevent_break, PersistentDataType.STRING);
                BreakEvent ev = Stuff.INSTANCE.itemBuilderManager.getBreakEvent(pdv);
                if (ev != null) {
                    ev.onBreak(e);
                }
            }
        }
    }
    
}
