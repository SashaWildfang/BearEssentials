package com.bearnation.bearessentials.listeners;

import com.bearnation.bearessentials.utils.BackManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final BackManager backManager;

    public PlayerDeathListener(BackManager backManager) {
        this.backManager = backManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        backManager.setLastLocation(event.getEntity().getUniqueId(), event.getEntity().getLocation());
    }
}
