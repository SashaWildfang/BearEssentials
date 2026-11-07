package com.bearnation.bearessentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TrashCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Inventory trash = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Trash Bin");
        player.openInventory(trash);
        player.sendMessage(ChatColor.GRAY + "You opened the trash bin. Items will disappear when closed.");
        return true;
    }
}
