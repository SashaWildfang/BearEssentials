package com.bearnation.bearessentials.utils;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackManager {

    private final Map<UUID, Location> lastLocations = new HashMap<>();

    public void setLastLocation(UUID uuid, Location location) {
        lastLocations.put(uuid, location);
    }

    public Location getLastLocation(UUID uuid) {
        return lastLocations.get(uuid);
    }

    public void clear(UUID uuid) {
        lastLocations.remove(uuid);
    }
}
