package com.trafalcraft.antiRedstoneClock.util.worldGuard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import com.sk89q.worldedit.Vector;

import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

class WorldGuard_6 implements IWorldGuard {
    
    public boolean isAllowedRegion(Location loc) {
        WorldGuardPlugin worldGuard = getWorldGuard();
        if (worldGuard == null) {
            return false;
        }
        RegionManager regionManager = worldGuard.getRegionManager(loc.getWorld());

        if (regionManager != null) {
            ApplicableRegionSet regions = getRegion(regionManager, loc);
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

    @Override
    public String getVersion() {
        WorldGuardPlugin worldGuard = getWorldGuard();
        if (worldGuard == null || worldGuard.getDescription().getVersion().length() == 0) {
            return "undefined";
        } else {
            return worldGuard.getDescription().getVersion().substring(0, 1);
        }
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    private ApplicableRegionSet getRegion(RegionManager regionManager, Location loc) {
        Vector vector = new Vector(loc.getX(), loc.getY(), loc.getZ());
        return regionManager.getApplicableRegions(vector);
    }
}
