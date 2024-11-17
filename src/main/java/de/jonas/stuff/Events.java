package de.jonas.stuff;

import org.bukkit.event.inventory.InventoryClickEvent;

import de.jonas.stuff.interfaced.ClickEvent;
import de.jonas.stuff.utility.PagenationInventory;

public class Events {
    
    private static final ClickEvent closeInv = Events::closeInvI;
    private static final ClickEvent cancelEvent = Events::cancelEventI;
    private static final ClickEvent prevPage = pageOffset(-1);
    private static final ClickEvent nextPage = pageOffset(1);

    public Events() {
        Stuff.INSTANCE.itemBuilderManager.addClickEvent(closeInv, "stuff:closeinv");
        Stuff.INSTANCE.itemBuilderManager.addClickEvent(cancelEvent, "stuff:cancelevent");
        Stuff.INSTANCE.itemBuilderManager.addClickEvent(prevPage, "stuff:prev_page");
        Stuff.INSTANCE.itemBuilderManager.addClickEvent(nextPage, "stuff:next_page");
    }

    private static void closeInvI(InventoryClickEvent e) {
		e.setCancelled(true);
        e.getWhoClicked().closeInventory();
	}

    private static void cancelEventI(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    private static ClickEvent pageOffset(int offset) {
        return e -> {
            if (e.getInventory().getHolder() instanceof PagenationInventory pgi) {
                e.setCancelled(true);
                int targetPage = pgi.currentpage + offset;
                if (targetPage < 0 || targetPage > (pgi.items.size() + PagenationInventory.numItemsOnPage - 1) / PagenationInventory.numItemsOnPage) {
                    // error
                    return;
                }
                pgi.fillPage(targetPage);
            }
        };
    }

}
