package com.trafalcraft.antiRedstoneClock.util.WorldGuard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;


public class WorldGuardHook {
    private WorldGuardHook() {}

    private static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }


    public static boolean checkAllowedRegion(Location loc) {
        WorldGuardPlugin worldGuard = getWorldGuard();
        if (worldGuard == null) {
            return false;
        }
        RegionManager regionManager;
        if (getVersionAsInteger() < 7) {
            regionManager = new WorldGuard6().getRegionManager(worldGuard, loc.getWorld());
        } else {
            regionManager = new WorldGuard7().getRegionManager(worldGuard, loc.getWorld());
        }

        if (regionManager != null) {
            ApplicableRegionSet regions = VectorAdaptor.getRegion(regionManager, loc);
            for (String ignoreRegion : Main.getIgnoredRegions()) {
                for (ProtectedRegion region : regions.getRegions()) {
                    if (region.getId().equals(ignoreRegion)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static int getVersionAsInteger() {
        try {
            return Integer.parseInt(getVersion());
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    public static String getVersion() {
        WorldGuardPlugin worldGuard = getWorldGuard();
        if (worldGuard == null || worldGuard.getDescription().getVersion().length() == 0) {
            return "undefined";
        } else {
            return worldGuard.getDescription().getVersion().substring(0,1);
        }
    }
}
