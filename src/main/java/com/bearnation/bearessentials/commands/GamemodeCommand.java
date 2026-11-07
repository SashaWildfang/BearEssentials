package com.bearnation.bearessentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class GamemodeCommand implements CommandExecutor, TabCompleter {

    private static final Map<String, GameMode> GAMEMODES = new HashMap<>();

    static {
        GAMEMODES.put("survival", GameMode.SURVIVAL);
        GAMEMODES.put("s", GameMode.SURVIVAL);
        GAMEMODES.put("0", GameMode.SURVIVAL);

        GAMEMODES.put("creative", GameMode.CREATIVE);
        GAMEMODES.put("c", GameMode.CREATIVE);
        GAMEMODES.put("1", GameMode.CREATIVE);

        GAMEMODES.put("adventure", GameMode.ADVENTURE);
        GAMEMODES.put("a", GameMode.ADVENTURE);
        GAMEMODES.put("2", GameMode.ADVENTURE);

        GAMEMODES.put("spectator", GameMode.SPECTATOR);
        GAMEMODES.put("sp", GameMode.SPECTATOR);
        GAMEMODES.put("3", GameMode.SPECTATOR);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /gamemode <mode> [player]");
            return true;
        }

        GameMode mode = GAMEMODES.get(args[0].toLowerCase());
        if (mode == null) {
            sender.sendMessage("§cUnknown gamemode: " + args[0]);
            return true;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cOnly players can change their own gamemode.");
                return true;
            }

            player.setGameMode(mode);
            player.sendMessage("§eYour gamemode was set to §f" + capitalize(mode.name()));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return true;
        }

        target.setGameMode(mode);
        target.sendMessage("§eYour gamemode was set to §f" + capitalize(mode.name()) + "§e by §f" + sender.getName());
        sender.sendMessage("§eYou set §a" + target.getName() + "§e's gamemode to §f" + capitalize(mode.name()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return GAMEMODES.keySet().stream()
                    .sorted()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .sorted()
                    .toList();
        }

        return Collections.emptyList();
    }

    private String capitalize(String text) {
        return text.charAt(0) + text.substring(1).toLowerCase();
    }
}
