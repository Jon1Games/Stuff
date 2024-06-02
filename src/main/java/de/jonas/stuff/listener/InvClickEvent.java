package de.jonas.stuff.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import de.jonas.stuff.ItemBuilderManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.ClickEvent;

public class InvClickEvent implements Listener{
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;
        PersistentDataContainer container = e.getCurrentItem().getItemMeta().getPersistentDataContainer();
        if (!container.has(ItemBuilderManager.inventoryClickEvent)) return;

        int pdv = container.get(ItemBuilderManager.inventoryClickEvent, PersistentDataType.INTEGER);
        ClickEvent ev = Stuff.INSTANCE.itemBuilderManager.getClickEvent(pdv);
        if (ev != null) {
            ev.onClick(e);
        }

    }

}
