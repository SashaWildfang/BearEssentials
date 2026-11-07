package com.bearnation.bearessentials.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignColorListener implements Listener {

    private static final String PERMISSION = "bearessentials.signcolor";

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        // Only allow players with permission to use color codes
        if (!player.hasPermission(PERMISSION)) return;

        for (int i = 0; i < event.lines().size(); i++) {
            String line = event.getLine(i);
            if (line == null) continue;

            event.setLine(i, ChatColor.translateAlternateColorCodes('&', line));
        }
    }
}
