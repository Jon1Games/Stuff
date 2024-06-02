package de.jonas.stuff;

import org.bukkit.NamespacedKey;

import de.jonas.stuff.interfaced.ClickEvent;
import de.jonas.stuff.interfaced.PlaceEvent;

import java.util.Map;
import java.util.HashMap;;

public class ItemBuilderManager {

    public static final NamespacedKey inventoryClickEvent = new NamespacedKey("stuff", "inventory_click_event");
    public static final NamespacedKey blockPlaceEvent = new NamespacedKey("stuff", "block_place_event");
    private Map<Integer, ClickEvent> handlerMap;
    private Map<Integer, PlaceEvent> handlerMap2;
    private int count;

    public ItemBuilderManager() {
        handlerMap = new HashMap<>();
        handlerMap2 = new HashMap<>();
        count = 0;
    }

    public int addClickEvent(ClickEvent event) {
        for (Map.Entry<Integer, ClickEvent> e : handlerMap.entrySet()) {
            if (e.getValue() == event) {
                return e.getKey();
            }
        }
        int pdv = count++;
        handlerMap.put(pdv, event);
        return pdv;
    }

    public ClickEvent getClickEvent(int pdv) {
        return handlerMap.get(pdv);
    }

    public int addPlaceEvent(PlaceEvent event) {
        for (Map.Entry<Integer, PlaceEvent> e : handlerMap2.entrySet()) {
            if (e.getValue() == event) {
                return e.getKey();
            }
        }
        int pdv = count++;
        handlerMap2.put(pdv, event);
        return pdv;
    }

    public PlaceEvent getPlaceEvent(int pdv) {
        return handlerMap2.get(pdv);
    }

}
