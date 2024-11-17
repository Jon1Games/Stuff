package de.jonas.stuff.utility;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import de.jonas.stuff.api.GuiPlaceholder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

public class PagenationInventory implements InventoryHolder{

    private Inventory inv;
    public List<ItemStack> items;
    public int currentpage;
    public static final int numItemsOnPage =  7 * 4;
    private Component nextName;
    private Component backName;
    private Component closeName;

    public PagenationInventory(List<ItemStack> items) {
        this.items = items;
    }

    public PagenationInventory setBackName(Component name) {
        backName = name;
        return this;
    }

    public PagenationInventory setNextName(Component name) {
        nextName = name;
        return this;
    }

    public PagenationInventory setCloseName(Component name) {
        closeName = name;
        return this;
    }


    @Override
    public @NotNull Inventory getInventory() {
        if (nextName == null) {
            nextName = Component.text("Weiter").decoration(TextDecoration.ITALIC, false);
        }
        if (backName == null) {
            backName = Component.text("Zurück").decoration(TextDecoration.ITALIC, false);
        }
        if (nextName == null) {
            closeName = Component.text("Schließen").decoration(TextDecoration.ITALIC, false);
        }

        this.inv = Bukkit.createInventory(this, 9 * 6, Component.text(" "));

        int[] placeholder = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,46,47,48,50,51,52};
        inv = new GuiPlaceholder(inv, placeholder).getInventory(); 

        inv.setItem(45, 
        new ItemBuilder()
            .setMaterial(Material.SPECTRAL_ARROW)
            .setName(backName)
            .whenClicked("stuff:prev_page")
            .build()
        );

        inv.setItem(49, 
            new ItemBuilder()
                .setMaterial(Material.BARRIER)
                .setName(closeName)
                .whenClicked("stuff:closeinv")
                .build()
        );

        inv.setItem(53,
            new ItemBuilder()
                .setMaterial(Material.ARROW)
                .setName(nextName)
                .whenClicked("stuff:next_page")
                .build()
        );

        fillPage(0);

        return this.inv;
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
                    inv.setItem((y + 1) * 9 + x + 1, items.get(y*7 + x +pageNum*numItemsOnPage));
                }
            }
        } catch(IndexOutOfBoundsException | NullPointerException e) {}

        for (int a = inv.firstEmpty(); a<44; a++) {
            if (a == -1) break;
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
