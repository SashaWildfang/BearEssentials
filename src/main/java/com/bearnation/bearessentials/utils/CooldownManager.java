package com.bearnation.bearessentials.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CooldownManager {

    private static CooldownManager instance;

    private final File file;
    private final YamlConfiguration config;
    private final long cooldownMillis = 5 * 60 * 1000; // 5 minutes

    public CooldownManager(File dataFolder) {
        file = new File(dataFolder, "command_cooldowns.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {}
        }
        config = YamlConfiguration.loadConfiguration(file);
        instance = this;
    }

    public static CooldownManager getInstance() {
        return instance;
    }

    public boolean isOnCooldown(String command, UUID uuid) {
        long now = System.currentTimeMillis();
        long expiry = config.getLong(command + "." + uuid, 0);
        return now < expiry;
    }

    public long getRemaining(String command, UUID uuid) {
        long now = System.currentTimeMillis();
        long expiry = config.getLong(command + "." + uuid, 0);
        return Math.max(0, expiry - now);
    }

    public void setCooldown(String command, UUID uuid) {
        long expiry = System.currentTimeMillis() + cooldownMillis;
        config.set(command + "." + uuid, expiry);
        save();
    }

    public boolean canUse(Player player, String command) {
        UUID uuid = player.getUniqueId();

        if (isOnCooldown(command, uuid)) {
            long remaining = getRemaining(command, uuid);
            player.sendMessage("§cYou must wait §e" + (remaining / 1000) + "s §cbefore using this command again.");
            return false;
        }

        setCooldown(command, uuid);
        return true;
    }


    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
