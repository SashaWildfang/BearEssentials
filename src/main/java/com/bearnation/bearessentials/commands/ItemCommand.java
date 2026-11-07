package com.bearnation.bearessentials.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemCommand implements CommandExecutor {

    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "You're not holding anything.");
            return true;
        }

        ItemMeta meta = item.getItemMeta();

        String displayName = meta.hasDisplayName()
                ? ChatColor.stripColor(meta.getDisplayName())
                : item.getType().toString().replace("_", " ").toLowerCase();

        // Build hover text with name and lore
        TextComponent.Builder hover = Component.text()
                .append(Component.text("Name:", NamedTextColor.YELLOW))
                .append(Component.text("\n"))
                .append(meta.hasDisplayName() 
                ? meta.displayName()
                : Component.text(item.getType().toString().replace("_", " ").toLowerCase(), NamedTextColor.WHITE));

        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            hover.append(Component.text("\n\nLore:", NamedTextColor.YELLOW)); // Yellow color for "Lore:"
            for (String line : lore) {
                hover.append(Component.text("\n")).append(LEGACY.deserialize(line));
            }
        }

        Component message = Component.text("🔍 ", NamedTextColor.GRAY)
        .append(Component.text(player.getName(), NamedTextColor.AQUA))
        .append(Component.text(" is holding ", NamedTextColor.YELLOW))
        .append(Component.text("["))
        .append(item.getAmount() > 1 
                ? Component.text(item.getAmount() + " x [") 
                : Component.text(""))
        .append(meta.hasDisplayName() 
                ? meta.displayName()
                : Component.text(item.getType().toString().replace("_", " ").toLowerCase()))
        .append(item.getAmount() > 1 
                ? Component.text("]]")
                : Component.text("]"))
        .hoverEvent(HoverEvent.showText(hover.build()));

        // Send to all players
        for (Player target : player.getServer().getOnlinePlayers()) {
            target.sendMessage(message);
        }

        return true;
    }
}