package de.jonas.stuff;

import org.bukkit.NamespacedKey;

import de.jonas.stuff.interfaced.ClickEvent;
import de.jonas.stuff.interfaced.PlaceEvent;

import java.util.Map;
import java.util.HashMap;;

public class ItemBuilderManager {

    public static final NamespacedKey inventoryClickEvent = new NamespacedKey("stuff", "inventory_click_event");
    public static final NamespacedKey blockPlaceEvent = new NamespacedKey("stuff", "block_place_event");
    private Map<String, ClickEvent> handlerMap;
    private Map<String, PlaceEvent> handlerMap2;

    public ItemBuilderManager() {
        handlerMap = new HashMap<>();
        handlerMap2 = new HashMap<>();
    }

    public void addClickEvent(ClickEvent event, String pdv) {
        handlerMap.put(pdv, event);
    }

    public ClickEvent getClickEvent(String pdv) {
        return handlerMap.get(pdv);
    }

    public void addPlaceEvent(PlaceEvent event, String pdv) {
        handlerMap2.put(pdv, event);
    }

    public PlaceEvent getPlaceEvent(String pdv) {
        return handlerMap2.get(pdv);
    }

}
