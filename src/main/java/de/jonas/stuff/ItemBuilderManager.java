package de.jonas.stuff;

import org.bukkit.NamespacedKey;

import de.jonas.stuff.interfaced.ClickEvent;

import java.util.Map;
import java.util.HashMap;;

public class ItemBuilderManager {

    public static final NamespacedKey inventoryClickEvent = new NamespacedKey("stuff", "inventory_click_event");
    private Map<Integer, ClickEvent> handlerMap;
    private int count;

    public ItemBuilderManager() {
        handlerMap = new HashMap<>();
        count = 0;
    }

    public int addEvent(ClickEvent event) {
        for (Map.Entry<Integer, ClickEvent> e : handlerMap.entrySet()) {
            if (e.getValue() == event) {
                return e.getKey();
            }
        }
        int pdv = count++;
        handlerMap.put(pdv, event);
        return pdv;
    }

    public ClickEvent get(int pdv) {
        return handlerMap.get(pdv);
    }

}
