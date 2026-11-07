package com.bearnation.bearessentials.commands;

import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        // If player already has a hat, take it off and put it in their hand
        if (helmet != null && helmet.getType() != Material.AIR) {
            // If their hand is not empty, cancel
            if (handItem != null && handItem.getType() != Material.AIR) {
                player.sendMessage("§cYour hand must be empty to remove your hat.");
                return true;
            }

            player.getInventory().setItemInMainHand(helmet);
            player.getInventory().setHelmet(null);
            player.sendMessage("§aYou took off your hat.");
            return true;
        }

        // Otherwise, try to put on item in hand
        if (handItem == null || handItem.getType() == Material.AIR) {
            player.sendMessage("§cYou must be holding an item to use as a hat.");
            return true;
        }

        player.getInventory().setHelmet(handItem);
        player.getInventory().setItemInMainHand(null);
        player.sendMessage("§aYou are now wearing that item as a hat.");
        return true;
    }
}
