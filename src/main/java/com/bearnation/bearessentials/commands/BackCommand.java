package com.bearnation.bearessentials.commands;

import com.bearnation.bearessentials.utils.BackManager;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {

    private final BackManager backManager;

    public BackCommand(BackManager backManager) {
        this.backManager = backManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        Location lastLoc = backManager.getLastLocation(player.getUniqueId());

        if (lastLoc == null) {
            player.sendMessage("§cYou have no location to return to.");
            return true;
        }

        player.teleportAsync(lastLoc).thenRun(() ->
                player.sendMessage("§aTeleported back to your last location.")
        );
        return true;
    }
}
