package de.jonas.stuff.utility;

import de.jonas.stuff.ItemBuilderManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.ClickEvent;
import de.jonas.stuff.interfaced.PlaceEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;

public class ItemBuilder {

    Stuff stuff = Stuff.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();

    private Material material;
    private Component name;
    private boolean glint, hasClickedEvent, hasPlaceEvent;
    private int clickID, placeID;
    private List<Component> lore;

    public ItemBuilder() {
        lore = new ArrayList<>();
        hasClickedEvent = false;
    }

    public ItemBuilder setMaterial(Material itemMaterial) {
        material = itemMaterial;
        return this;
    }

    public ItemBuilder setName(String itemName) {
        name = mm.deserialize(itemName).decoration(TextDecoration.ITALIC, false);
        return this;
    }

    public ItemBuilder setName(Component itemName) {
        name = itemName;
        return this;
    }

    public ItemBuilder setGlint(boolean itemGlint) {
        glint = itemGlint;
        return this;
    }

    public ItemBuilder setLore(List<Component> itemLore) {
        lore = itemLore;
        return this;
    }

    public ItemBuilder addLoreLine(String loreLine) {
        lore.add(mm.deserialize(loreLine).decoration(TextDecoration.ITALIC, false));
        return this;
    }

    public ItemBuilder addLoreLine(Component loreLine) {
        lore.add(loreLine);
        return this;
    }

    public ItemBuilder whenClicked(ClickEvent listener) {
        if (hasClickedEvent) {
            throw new IllegalStateException("This item already has an click listener");
        }
        clickID = stuff.itemBuilderManager.addClickEvent(listener);
        hasClickedEvent = true;
        return this;
    }

    public ItemBuilder whenPlaced(PlaceEvent listener) {
        if (hasPlaceEvent) {
            throw new IllegalStateException("This item already has an place listener");
        }
        placeID = stuff.itemBuilderManager.addPlaceEvent(listener);
        hasPlaceEvent = true;
        return this;
    }

    public ItemStack build() {
        if (material == null) {
            throw new IllegalArgumentException("Material may not be null");
        }
        ItemStack item = new ItemStack(material);
            
        ItemMeta meta = item.getItemMeta();
        if (name != null) meta.displayName(name);
        if (glint) {
            meta.addEnchant(Enchantment.DURABILITY, 10, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (lore != null) meta.lore(lore);
        if (hasClickedEvent) meta.getPersistentDataContainer().set(ItemBuilderManager.inventoryClickEvent, PersistentDataType.INTEGER, clickID);
        if (hasPlaceEvent) meta.getPersistentDataContainer().set(ItemBuilderManager.blockPlaceEvent, PersistentDataType.INTEGER, placeID);
        item.setItemMeta(meta);
        Stuff.INSTANCE.increaseitemBuildsCount();
        return item;
    } 

}
