package de.jonas.stuff.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import de.jonas.stuff.ItemBuilderManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.PlaceEvent;

public class BlockPlace implements Listener{

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock() == null) return;
        PersistentDataContainer container = e.getItemInHand().getItemMeta().getPersistentDataContainer();
        if (!container.has(ItemBuilderManager.blockPlaceEvent)) return;

        String pdv = container.get(ItemBuilderManager.blockPlaceEvent, PersistentDataType.STRING);
        PlaceEvent ev = Stuff.INSTANCE.itemBuilderManager.getPlaceEvent(pdv);
        if (ev != null) {
            ev.onPlace(e);
        }
    }
    
}
