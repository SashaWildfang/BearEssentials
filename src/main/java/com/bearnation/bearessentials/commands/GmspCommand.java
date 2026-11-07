package com.bearnation.bearessentials.commands;

import com.bearnation.bearessentials.utils.GamemodeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GmspCommand implements TabExecutor {

    private static final String SELF_PERM = "bearessentials.cmd.gmsp";
    private static final String OTHER_PERM = "bearessentials.cmd.gmsp.other";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player player) {
            if (!sender.hasPermission(SELF_PERM)) {
                sender.sendMessage("§cYou don’t have permission.");
                return true;
            }

            GamemodeUtil.changeGamemode(sender, player, GameMode.SPECTATOR);
            return true;
        }

        if (args.length == 1) {
            if (!sender.hasPermission(OTHER_PERM)) {
                sender.sendMessage("§cYou don’t have permission to change others' gamemode.");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage("§cThat player is not online.");
                return true;
            }

            GamemodeUtil.changeGamemode(sender, target, GameMode.SPECTATOR);
            return true;
        }

        sender.sendMessage("§cUsage: /gmsp [player]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission(OTHER_PERM)) {
            List<String> names = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                names.add(player.getName());
            }
            return names;
        }
        return Collections.emptyList();
    }
}
