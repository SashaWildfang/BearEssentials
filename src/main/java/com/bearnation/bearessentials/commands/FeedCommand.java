package com.bearnation.bearessentials.commands;

import com.bearnation.bearessentials.utils.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FeedCommand implements TabExecutor {

    private final CooldownManager cooldownManager;
    private static final String SELF_PERM = "bearessentials.cmd.feed";
    private static final String OTHER_PERM = "bearessentials.cmd.feed.other";

    public FeedCommand(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            if (!player.hasPermission(SELF_PERM)) {
                player.sendMessage("§cYou don’t have permission to use this.");
                return true;
            }
            if (cooldownManager.isOnCooldown("feed", player.getUniqueId())) {
                long seconds = cooldownManager.getRemaining("feed", player.getUniqueId()) / 1000;
                player.sendMessage("§cYou must wait " + seconds + "s before using /feed again.");
                return true;
            }

            player.setFoodLevel(20);
            player.setSaturation(20f);
            player.sendMessage("§aYou have been fed.");
            cooldownManager.setCooldown("feed", player.getUniqueId());
            return true;
        }

        if (!player.hasPermission(OTHER_PERM)) {
            player.sendMessage("§cYou don’t have permission to feed others.");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return true;
        }

        target.setFoodLevel(20);
        target.setSaturation(20f);
        player.sendMessage("§aYou fed §e" + target.getName() + "§a.");
        target.sendMessage("§aYou have been fed by §e" + player.getName() + "§a.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission(OTHER_PERM)) {
            List<String> matches = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                matches.add(p.getName());
            }
            return matches;
        }
        return new ArrayList<>();
    }
}
