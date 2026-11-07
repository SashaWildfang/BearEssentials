package com.bearnation.bearessentials.listeners;

import com.bearnation.bearessentials.BearEssentials;
import com.bearnation.bearessentials.commands.InvseeCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class InvseeListener implements Listener {
    private final BearEssentials plugin;

    public InvseeListener(BearEssentials plugin) {
        this.plugin = plugin;
        startAutoRefresh();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player viewer)) return;

        UUID viewerId = viewer.getUniqueId();
        Map<UUID, UUID> views = InvseeCommand.getActiveViews();
        if (!views.containsKey(viewerId)) return;

        UUID targetId = views.get(viewerId);
        Player target = Bukkit.getPlayer(targetId);
        if (target == null || !target.isOnline()) return;

        int slot = event.getRawSlot();
        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();

        // Handle armor restriction
        if (slot >= 2 && slot <= 5 && !isValidArmorSlot(cursor, slot)) {
            event.setCancelled(true);
            return;
        }

        // Let the event go through, then apply viewer GUI to target inventory
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Inventory inv = viewer.getOpenInventory().getTopInventory();

            // Update hand item (slot 0)
            target.getInventory().setItemInMainHand(safeItem(inv.getItem(0)));

            // Update armor (2–5)
            target.getInventory().setHelmet(safeItem(inv.getItem(2)));
            target.getInventory().setChestplate(safeItem(inv.getItem(3)));
            target.getInventory().setLeggings(safeItem(inv.getItem(4)));
            target.getInventory().setBoots(safeItem(inv.getItem(5)));

            // Update hotbar (18–26)
            for (int i = 0; i <= 8; i++) {
                target.getInventory().setItem(i, safeItem(inv.getItem(18 + i)));
            }

            // Update main inventory (27–53)
            for (int i = 9; i <= 35; i++) {
                target.getInventory().setItem(i, safeItem(inv.getItem(27 + (i - 9))));
            }

            target.updateInventory();
        }, 1L);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player viewer)) return;

        UUID viewerId = viewer.getUniqueId();
        Map<UUID, UUID> views = InvseeCommand.getActiveViews();
        if (!views.containsKey(viewerId)) return;

        // Delay and sync back to target
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player target = Bukkit.getPlayer(views.get(viewerId));
            if (target != null && target.isOnline()) {
                applyGuiToTarget(viewer, target);
            }
        }, 1L);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InvseeCommand.getActiveViews().remove(event.getPlayer().getUniqueId());
    }

    public static void refreshAll() {
        Map<UUID, UUID> views = InvseeCommand.getActiveViews();
        for (UUID viewerId : views.keySet()) {
            Player viewer = Bukkit.getPlayer(viewerId);
            UUID targetId = views.get(viewerId);
            if (viewer != null && viewer.isOnline()) {
                refresh(viewer, targetId);
            }
        }
    }

    private static void refresh(Player viewer, UUID targetId) {
        Player target = Bukkit.getPlayer(targetId);
        if (target == null || !target.isOnline()) return;

        Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("BearEssentials"), () -> {
            Inventory inv = viewer.getOpenInventory().getTopInventory();
            ItemStack[] targetContents = target.getInventory().getContents();

            // Hand
            inv.setItem(0, safeItem(target.getInventory().getItemInMainHand()));
            // Armor
            inv.setItem(2, safeItem(target.getInventory().getHelmet()));
            inv.setItem(3, safeItem(target.getInventory().getChestplate()));
            inv.setItem(4, safeItem(target.getInventory().getLeggings()));
            inv.setItem(5, safeItem(target.getInventory().getBoots()));
            // Hotbar
            for (int i = 0; i <= 8; i++) {
                inv.setItem(18 + i, safeItem(targetContents[i]));
            }
            // Main inventory
            for (int i = 9; i <= 35; i++) {
                inv.setItem(27 + (i - 9), safeItem(targetContents[i]));
            }
        }, 1L);
    }

    private static void applyGuiToTarget(Player viewer, Player target) {
        Inventory inv = viewer.getOpenInventory().getTopInventory();

        target.getInventory().setItemInMainHand(safeItem(inv.getItem(0)));
        target.getInventory().setHelmet(safeItem(inv.getItem(2)));
        target.getInventory().setChestplate(safeItem(inv.getItem(3)));
        target.getInventory().setLeggings(safeItem(inv.getItem(4)));
        target.getInventory().setBoots(safeItem(inv.getItem(5)));

        for (int i = 0; i <= 8; i++) {
            target.getInventory().setItem(i, safeItem(inv.getItem(18 + i)));
        }

        for (int i = 9; i <= 35; i++) {
            target.getInventory().setItem(i, safeItem(inv.getItem(27 + (i - 9))));
        }

        target.updateInventory();
    }

    private void startAutoRefresh() {
        Bukkit.getScheduler().runTaskTimer(plugin, InvseeListener::refreshAll, 20L, 20L); // 1 second
    }

    private static boolean isValidArmorSlot(ItemStack item, int slot) {
        if (item == null || item.getType() == Material.AIR) return true;
        return switch (slot) {
            case 2 -> item.getType().name().endsWith("_HELMET");
            case 3 -> item.getType().name().endsWith("_CHESTPLATE");
            case 4 -> item.getType().name().endsWith("_LEGGINGS");
            case 5 -> item.getType().name().endsWith("_BOOTS");
            default -> false;
        };
    }

    private static ItemStack safeItem(ItemStack item) {
        return (item != null && item.getType() != Material.AIR) ? item.clone() : new ItemStack(Material.AIR);
    }
}
