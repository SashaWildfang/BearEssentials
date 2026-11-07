package com.bearnation.bearessentials.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class TrashListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("Trash Bin")) {
            event.getInventory().clear(); // Delete items
        }
    }
}
