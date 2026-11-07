package com.bearnation.bearessentials.commands;

import com.bearnation.bearessentials.utils.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExtinguishCommand implements TabExecutor {

    private final CooldownManager cooldownManager;
    private static final String SELF_PERM = "bearessentials.cmd.extinguish";
    private static final String OTHER_PERM = "bearessentials.cmd.extinguish.other";

    public ExtinguishCommand(CooldownManager cooldownManager) {
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

            if (cooldownManager.isOnCooldown("extinguish", player.getUniqueId())) {
                long seconds = cooldownManager.getRemaining("extinguish", player.getUniqueId()) / 1000;
                player.sendMessage("§cYou must wait " + seconds + "s before using /extinguish again.");
                return true;
            }

            player.setFireTicks(0);
            player.sendMessage("§aYou have been extinguished.");
            cooldownManager.setCooldown("extinguish", player.getUniqueId());
            return true;
        }

        if (!player.hasPermission(OTHER_PERM)) {
            player.sendMessage("§cYou don’t have permission to extinguish others.");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return true;
        }

        target.setFireTicks(0);
        player.sendMessage("§aYou extinguished §e" + target.getName() + "§a.");
        target.sendMessage("§aYou have been extinguished by §e" + player.getName() + "§a.");
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
