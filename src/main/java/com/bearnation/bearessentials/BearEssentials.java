package com.bearnation.bearessentials;

import com.bearnation.bearessentials.commands.*;
import com.bearnation.bearessentials.listeners.PlayerDeathListener;
import com.bearnation.bearessentials.listeners.SignColorListener;
import com.bearnation.bearessentials.listeners.TrashListener;
import com.bearnation.bearessentials.utils.BackManager;
import com.bearnation.bearessentials.utils.CooldownManager;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import com.bearnation.bearessentials.listeners.InvseeListener;

import java.util.Objects;

public class BearEssentials extends JavaPlugin {

    private static BearEssentials instance;
    private CooldownManager cooldownManager;
    private BackManager backManager;

    @Override
    public void onEnable() {
        instance = this;
        cooldownManager = new CooldownManager(getDataFolder());
        backManager = new BackManager();
        getServer().getPluginManager().registerEvents(new InvseeListener(this), this);

        getServer().getScheduler().runTaskTimer(this, InvseeListener::refreshAll, 10L, 10L);


        // Gamemode commands with tab completion
        registerWithTab(new GmcCommand(), "gmc");
        registerWithTab(new GmsCommand(), "gms");
        registerWithTab(new GmaCommand(), "gma");
        registerWithTab(new GmspCommand(), "gmsp");

        // General gamemode command
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new GamemodeCommand());

        // Inventory viewer
        Objects.requireNonNull(getCommand("invsee")).setExecutor(new InvseeCommand()); // or false if you want to disable edits globally

        // Trash bin
        Objects.requireNonNull(getCommand("trash")).setExecutor(new TrashCommand());
        getServer().getPluginManager().registerEvents(new TrashListener(), this);

        // Feed & Heal (with cooldowns)
        FeedCommand feed = new FeedCommand(cooldownManager);
        HealCommand heal = new HealCommand(cooldownManager);
        ExtinguishCommand extinguish = new ExtinguishCommand(cooldownManager);

        Objects.requireNonNull(getCommand("feed")).setExecutor(feed);
        Objects.requireNonNull(getCommand("feed")).setTabCompleter(feed);

        Objects.requireNonNull(getCommand("heal")).setExecutor(heal);
        Objects.requireNonNull(getCommand("heal")).setTabCompleter(heal);

        Objects.requireNonNull(getCommand("extinguish")).setExecutor(extinguish);
        Objects.requireNonNull(getCommand("extinguish")).setTabCompleter(extinguish);
        Objects.requireNonNull(getCommand("item")).setExecutor(new ItemCommand());

        // Hat
        Objects.requireNonNull(getCommand("hat")).setExecutor(new HatCommand());

        // Back command
        Objects.requireNonNull(getCommand("back")).setExecutor(new BackCommand(backManager));
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(backManager), this);
        getServer().getPluginManager().registerEvents(new SignColorListener(), this);

        getLogger().info("BearEssentials enabled.");
    }

    private void registerWithTab(TabExecutor command, String name) {
        Objects.requireNonNull(getCommand(name)).setExecutor(command);
        Objects.requireNonNull(getCommand(name)).setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        getLogger().info("BearEssentials disabled.");
    }

    public static BearEssentials getInstance() {
        return instance;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public BackManager getBackManager() {
        return backManager;
    }
}
