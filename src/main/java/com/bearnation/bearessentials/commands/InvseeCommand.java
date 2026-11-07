package com.bearnation.bearessentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InvseeCommand implements CommandExecutor, Listener {

    private static final Map<UUID, UUID> activeViews = new HashMap<>();

    public static Map<UUID, UUID> getActiveViews() {
        return activeViews;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player viewer)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (!viewer.hasPermission("bearessentials.cmd.invsee")) {
            viewer.sendMessage(ChatColor.RED + "You don't have permission.");
            return true;
        }

        if (args.length != 1) {
            viewer.sendMessage(ChatColor.YELLOW + "Usage: /invsee <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            viewer.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        if (!viewer.hasPermission("bearessentials.cmd.invsee.other") && !viewer.equals(target)) {
            viewer.sendMessage(ChatColor.RED + "You can only view your own inventory.");
            return true;
        }

        Inventory invsee = Bukkit.createInventory(viewer, 54, target.getName() + "'s Inventory");
        ItemStack[] contents = target.getInventory().getContents();

        // Slot 0: held item
        invsee.setItem(0, contents[0]);

        // Slot 1: gray pane
        invsee.setItem(1, grayPane());

        // Slots 2–5: armor
        invsee.setItem(2, target.getInventory().getHelmet());
        invsee.setItem(3, target.getInventory().getChestplate());
        invsee.setItem(4, target.getInventory().getLeggings());
        invsee.setItem(5, target.getInventory().getBoots());

        // Slots 6–8: gray filler
        for (int i = 6; i <= 8; i++) {
            invsee.setItem(i, grayPane());
        }

        // Row 2: gray filler
        for (int i = 9; i <= 17; i++) {
            invsee.setItem(i, grayPane());
        }

        // Row 3 (18–26): hotbar (0–8)
        for (int i = 0; i <= 8; i++) {
            invsee.setItem(18 + i, contents[i]);
        }

        // Rows 4–6 (27–53): main inventory (9–35)
        for (int i = 9; i <= 35; i++) {
            invsee.setItem(27 + (i - 9), contents[i]);
        }

        viewer.openInventory(invsee);
        activeViews.put(viewer.getUniqueId(), target.getUniqueId());
        return true;
    }

    private ItemStack grayPane() {
        ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = pane.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            pane.setItemMeta(meta);
        }
        return pane;
    }
}
