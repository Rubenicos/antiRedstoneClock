package com.trafalcraft.antiRedstoneClock.util;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;


public class WorldGuardLink {
    private static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }


    public static boolean checkAllowedRegion(Location loc) {
        if (getWorldGuard() == null) {
            return false;
        }
        if (getWorldGuard().getRegionManager(loc.getWorld()) != null) {
            RegionManager worldGuard = getWorldGuard().getRegionManager(loc.getWorld());
            ApplicableRegionSet regions = worldGuard.getApplicableRegions(loc);
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
}
