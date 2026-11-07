package com.bearnation.bearessentials.utils;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeUtil {

    public static void changeGamemode(CommandSender sender, Player target, GameMode mode) {
        boolean self = sender instanceof Player && ((Player) sender).getUniqueId().equals(target.getUniqueId());

        target.setGameMode(mode);

        String modeFormatted = ChatColor.WHITE + mode.name().substring(0, 1).toUpperCase() + mode.name().substring(1).toLowerCase();

        if (self) {
            target.sendMessage(ChatColor.YELLOW + "Your gamemode was set to " + modeFormatted);
        } else {
            target.sendMessage(ChatColor.YELLOW + "Your gamemode was set to " + modeFormatted +
                    ChatColor.YELLOW + " by " + ChatColor.WHITE + sender.getName());
            sender.sendMessage(ChatColor.YELLOW + "You set " + ChatColor.GREEN + target.getName() +
                    ChatColor.YELLOW + "'s gamemode to " + modeFormatted);
        }
    }
}
