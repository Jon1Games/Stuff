package de.jonas.stuff.api;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import de.jonas.stuff.utility.ItemBuilder;

public class GuiPlaceholder {

    Inventory inv;

    public GuiPlaceholder(Inventory inv, int[] filledSlots) {
        this.inv = inv;
        for (int a : filledSlots) {
            inv.setItem(a, 
                new ItemBuilder()
                    .setName("")
                    .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .whenClicked("stuff:cancelevent")
                    .build()
            );
        }
    }

    public Inventory getInventory() {
        return inv;
    }
    
}
