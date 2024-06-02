package de.jonas.stuff.utility;

import de.jonas.stuff.ItemBuilderManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.api.ClickEvent;
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

public class ItemBuilder {

    Stuff stuff = Stuff.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();

    private Material material;
    private Component name;
    private boolean glint;
    private int clickID;
    private List<Component> lore;

    public ItemBuilder() {
        lore = new ArrayList<>();
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
        int pdv = stuff.itemBuilderManager.addEvent(listener);
        clickID = pdv;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        if (glint) {
            meta.addEnchant(Enchantment.DURABILITY, 10, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.lore(lore);
        meta.getPersistentDataContainer().set(ItemBuilderManager.inventoryClickEvent, PersistentDataType.INTEGER, clickID);
        item.setItemMeta(meta);
        Stuff.INSTANCE.increaseitemBuildsCount();
        return item;
    } 

}
