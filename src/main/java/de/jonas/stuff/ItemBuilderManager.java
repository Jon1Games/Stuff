package de.jonas.stuff;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.NamespacedKey;

import de.jonas.stuff.interfaced.ClickEvent;
import de.jonas.stuff.interfaced.LeftClickEvent;
import de.jonas.stuff.interfaced.PlaceEvent;
import de.jonas.stuff.interfaced.RightClickEvent;
;

public class ItemBuilderManager {

    public static final NamespacedKey inventoryClickEvent = new NamespacedKey("stuff", "inventory_click_event");
    public static final NamespacedKey inventoryLeftClickEvent = new NamespacedKey("stuff", "inventory_left_click_event");
    public static final NamespacedKey inventoryRightClickEvent = new NamespacedKey("stuff", "inventory_right_click_event");
    public static final NamespacedKey blockPlaceEvent = new NamespacedKey("stuff", "block_place_event");
    private Map<String, ClickEvent> handlerMap;
    private Map<String, PlaceEvent> handlerMap2;
    private Map<String, LeftClickEvent> handlerMap3;
    private Map<String, RightClickEvent> handlerMap4;

    public ItemBuilderManager() {
        handlerMap = new HashMap<>();
        handlerMap2 = new HashMap<>();
        handlerMap3 = new HashMap<>();
        handlerMap4 = new HashMap<>();
    }

    public void addClickEvent(ClickEvent event, String pdv) {
        handlerMap.put(pdv, event);
    }

    public void addleftClickEvent(LeftClickEvent event, String pdv) {
        handlerMap3.put(pdv, event);
    }

    public void addRightClickEvent(RightClickEvent event, String pdv) {
        handlerMap4.put(pdv, event);
    }

    public ClickEvent getClickEvent(String pdv) {
        return handlerMap.get(pdv);
    }

    public LeftClickEvent getleftClickEvent(String pdv) {
        return handlerMap3.get(pdv);
    }

    public RightClickEvent getRightClickEvent(String pdv) {
        return handlerMap4.get(pdv);
    }

    public void addPlaceEvent(PlaceEvent event, String pdv) {
        handlerMap2.put(pdv, event);
    }

    public PlaceEvent getPlaceEvent(String pdv) {
        return handlerMap2.get(pdv);
    }

}
