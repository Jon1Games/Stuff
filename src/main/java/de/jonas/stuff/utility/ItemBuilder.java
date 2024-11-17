package de.jonas.stuff.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import com.destroystokyo.paper.profile.PlayerProfile;

import de.jonas.stuff.ItemBuilderManager;
import de.jonas.stuff.Stuff;
import de.jonas.stuff.interfaced.ClickEvent;
import de.jonas.stuff.interfaced.LeftClickEvent;
import de.jonas.stuff.interfaced.PlaceEvent;
import de.jonas.stuff.interfaced.RightClickEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ItemBuilder {

    Stuff stuff = Stuff.INSTANCE;
    MiniMessage mm = MiniMessage.miniMessage();

    private Material material;
    private Component name;
    private boolean glint,hasClickedEvent,hasPlaceEvent,hasLeftClickedEvent,hasRightClickedEvent;
    private String clickID,leftClickID,rightClickID,placeID;
    private List<Component> lore;
    private UUID skullPlayerUUID;

    public ItemBuilder() {
        lore = new ArrayList<>();
        hasClickedEvent = false;
    }

    public ItemBuilder setMaterial(Material itemMaterial) {
        material = itemMaterial;
        return this;
    }

    public ItemBuilder setSkull(UUID playerUUID) {
        skullPlayerUUID = playerUUID;
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

    public ItemBuilder whenClicked(String pdv) {
        if (hasClickedEvent) {
            throw new IllegalStateException("This item already has an click listener");
        }
        clickID = pdv;
        hasClickedEvent = true;
        return this;
    }

    public ItemBuilder whenClicked(ClickEvent lisstener, String pdv) {
        if (hasClickedEvent) {
            throw new IllegalStateException("This item already has an click listener");
        }
        stuff.itemBuilderManager.addClickEvent(lisstener, pdv);
        clickID = pdv;
        hasClickedEvent = true;
        return this;
    }

    public ItemBuilder whenLeftClicked(String pdv) {
        if (hasLeftClickedEvent) {
            throw new IllegalStateException("This item already has an click listener");
        }
        leftClickID = pdv;
        hasLeftClickedEvent = true;
        return this;
    }

    public ItemBuilder whenLeftClicked(LeftClickEvent lisstener, String pdv) {
        if (hasLeftClickedEvent) {
            throw new IllegalStateException("This item already has an click listener");
        }
        stuff.itemBuilderManager.addleftClickEvent(lisstener, pdv);
        leftClickID = pdv;
        hasLeftClickedEvent = true;
        return this;
    }

    public ItemBuilder whenRightClicked(String pdv) {
        if (hasRightClickedEvent) {
            throw new IllegalStateException("This item already has an click listener");
        }
        rightClickID = pdv;
        hasRightClickedEvent = true;
        return this;
    }

    public ItemBuilder whenRightClicked(RightClickEvent lisstener, String pdv) {
        if (hasRightClickedEvent) {
            throw new IllegalStateException("This item already has an click listener");
        }
        stuff.itemBuilderManager.addRightClickEvent(lisstener, pdv);
        rightClickID = pdv;
        hasRightClickedEvent = true;
        return this;
    }

    public ItemBuilder whenPlaced(String pdv) {
        if (hasPlaceEvent) {
            throw new IllegalStateException("This item already has an place listener");
        }
        placeID = pdv;
        hasPlaceEvent = true;
        return this;
    }

    public ItemBuilder whenPlaced(PlaceEvent listener, String pdv) {
        if (hasPlaceEvent) {
            throw new IllegalStateException("This item already has an place listener");
        }
        placeID = pdv;
        stuff.itemBuilderManager.addPlaceEvent(listener, pdv);
        hasPlaceEvent = true;
        return this;
    }

    public ItemStack build() {
        if (skullPlayerUUID != null) {
            material = Material.PLAYER_HEAD;
        } else if (material == null) {
            throw new IllegalArgumentException("Material may not be null");
        }
        ItemStack item = new ItemStack(material);
            
        if (skullPlayerUUID != null) {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            OfflinePlayer p = Bukkit.getOfflinePlayer(skullPlayerUUID);
            PlayerProfile prof = p.getPlayerProfile();
            if (!prof.isComplete()) {
                prof.complete();
            }
            skullMeta.setPlayerProfile(prof);
            item.setItemMeta(skullMeta);
        }
        ItemMeta meta = item.getItemMeta();
        if (name != null) meta.displayName(name);
        if (glint) {
            meta.addEnchant(Enchantment.UNBREAKING, 10, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (lore != null) meta.lore(lore);
        if (hasClickedEvent) meta.getPersistentDataContainer().set(ItemBuilderManager.inventoryClickEvent, PersistentDataType.STRING, clickID);
        if (hasLeftClickedEvent) meta.getPersistentDataContainer().set(ItemBuilderManager.inventoryLeftClickEvent, PersistentDataType.STRING, leftClickID);
        if (hasRightClickedEvent) meta.getPersistentDataContainer().set(ItemBuilderManager.inventoryRightClickEvent, PersistentDataType.STRING, rightClickID);
        if (hasPlaceEvent) meta.getPersistentDataContainer().set(ItemBuilderManager.blockPlaceEvent, PersistentDataType.STRING, placeID);
        item.setItemMeta(meta);
        return item;
    } 

}
