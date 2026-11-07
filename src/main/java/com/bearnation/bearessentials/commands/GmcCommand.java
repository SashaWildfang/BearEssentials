package com.bearnation.bearessentials.commands;

import com.bearnation.bearessentials.utils.GamemodeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GmcCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Self gamemode change
        if (args.length == 0 && sender instanceof Player player) {
            if (!sender.hasPermission("bearessentials.cmd.gmc")) {
                sender.sendMessage("§cYou don’t have permission.");
                return true;
            }

            GamemodeUtil.changeGamemode(sender, player, GameMode.CREATIVE);
            return true;
        }

        // Change for another player
        if (args.length == 1) {
            if (!sender.hasPermission("bearessentials.cmd.gmc.other")) {
                sender.sendMessage("§cYou don’t have permission to change others' gamemode.");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage("§cThat player is not online.");
                return true;
            }

            GamemodeUtil.changeGamemode(sender, target, GameMode.CREATIVE);
            return true;
        }

        sender.sendMessage("§cUsage: /gmc [player]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("bearessentials.cmd.gmc.other")) {
            List<String> names = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                names.add(player.getName());
            }
            return names;
        }
        return Collections.emptyList();
    }
}
