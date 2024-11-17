package de.jonas.stuff.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import de.jonas.stuff.ItemBuilderManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.ClickEvent;
import de.jonas.stuff.interfaced.LeftClickEvent;
import de.jonas.stuff.interfaced.RightClickEvent;

public class InvClickEvent implements Listener{
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null) return;
        PersistentDataContainer container = e.getCurrentItem().getItemMeta().getPersistentDataContainer();

        if (container.has(ItemBuilderManager.inventoryClickEvent)) {
            String pdv = container.get(ItemBuilderManager.inventoryClickEvent, PersistentDataType.STRING);
            ClickEvent ev = Stuff.INSTANCE.itemBuilderManager.getClickEvent(pdv);
            if (ev != null) {
                ev.onClick(e);
            }
        }

        if (e.isRightClick()) {
            if (!container.has(ItemBuilderManager.inventoryRightClickEvent)) return;
            String pdv2 = container.get(ItemBuilderManager.inventoryRightClickEvent, PersistentDataType.STRING);
            RightClickEvent ev2 = Stuff.INSTANCE.itemBuilderManager.getRightClickEvent(pdv2);
            if (ev2 != null) {
                ev2.onClick(e);
            } 
        }

        if (e.isLeftClick()) {
            if (!container.has(ItemBuilderManager.inventoryLeftClickEvent)) return;
            String pdv3 = container.get(ItemBuilderManager.inventoryLeftClickEvent, PersistentDataType.STRING);
            LeftClickEvent ev3 = Stuff.INSTANCE.itemBuilderManager.getleftClickEvent(pdv3);
            if (ev3 != null) {
                ev3.onClick(e);
            } 
        }

    }

}
