package de.jonas.stuff.utility;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

public class PagenationInventory implements InventoryHolder{

    private Inventory inv;
    public List<ItemStack> items;
    public int currentpage;
    public static final int numItemsOnPage =  7 * 4;
    String nextName, backName, closeName;

    public PagenationInventory(List<ItemStack> items) {
        this.items = items;

        this.inv = Bukkit.createInventory(this, 9 * 6, Component.text("Seite: " + (currentpage + 1)));

        int[] placeholder = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,46,47,48,50,51,52};
        for (int a : placeholder) {
            inv.setItem(a,
                new ItemBuilder()
                    .setName("")
                    .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .whenClicked("stuff:cancelevent")
                    .build()
            );
        }

        fillPage(0);

    }

    @Override
    public @NotNull Inventory getInventory() {
        if (nextName == null) {
            nextName = "Weiter";
        }
        if (backName == null) {
            backName = "Zurück";
        }
        if (nextName == null) {
            closeName = "Schließen";
        }

        inv.setItem(45, 
        new ItemBuilder()
            .setMaterial(Material.SPECTRAL_ARROW)
            .setName("Zurück")
            .whenClicked("stuff:prev_page")
            .build()
        );

        inv.setItem(49, 
            new ItemBuilder()
                .setMaterial(Material.BARRIER)
                .setName("Schließen")
                .whenClicked("stuff:closeinv")
                .build()
        );

        inv.setItem(53,
            new ItemBuilder()
                .setMaterial(Material.ARROW)
                .setName("Weiter")
                .whenClicked("stuff:next_page")
                .build()
        );

        return this.inv;
    }

    public void setBackName(String name) {
        backName = name;
        return;
    }

    public void setNextName(String name) {
        nextName = name;
        return;
    }

    public void setCloseName(String name) {
        closeName = name;
        return;
    }

    public void fillPage(int pageNum) {
        currentpage = pageNum;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 7; x++) {
                inv.clear((y + 1) * 9 + x + 1);
            }
        }
        try {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 7; x++) {
                    inv.setItem((y + 1) * 9 + x + 1, items.get(y*5 + x +pageNum*numItemsOnPage));
                }
            }
        } catch(IndexOutOfBoundsException | NullPointerException e) {}

        for (int a = inv.firstEmpty(); a<44; a++) {
            if (a == 18 || a == 27 || a == 36 ||
            a == 17 || a == 26 || a == 35) continue;
            inv.setItem(a,
                new ItemBuilder()
                    .setMaterial(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
                    .setName("")
                    .whenClicked("stuff:cancelevent")
                    .build()
            );
        }

    }
}
