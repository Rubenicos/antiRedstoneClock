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
        if (worldGuard.getDescription().getVersion().startsWith("7")) {
            regionManager = WorldGuard_7.getRegionManager(worldGuard, loc.getWorld());
        } else {
            regionManager = WorldGuard_6.getRegionManager(worldGuard, loc.getWorld());
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


}
