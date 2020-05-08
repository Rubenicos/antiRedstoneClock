package com.trafalcraft.antiRedstoneClock.util.worldGuard;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.bukkit.protection.DelayedRegionOverlapAssociation;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

class WorldGuard_6 implements IWorldGuard {

    private static final StateFlag ANTIREDSTONECLOCK_FLAG = new StateFlag("anti-redstone-clock", true);
    private static final WorldGuardPlugin worldGuard = getWorldGuard();

    @Override
    public boolean isAllowedRegion(Location loc) {
        boolean result = false;
        if (worldGuard != null) {
            RegionQuery query = worldGuard.getRegionContainer().createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(loc);
            RegionAssociable associable = new DelayedRegionOverlapAssociation(query, loc);
            if (!set.testState(associable, ANTIREDSTONECLOCK_FLAG)) {
                result = true;
            } else {
                RegionManager regionManager = worldGuard.getRegionManager(loc.getWorld());
                result = checkRegionFromConfigFile(loc, regionManager);
            }
        }
        return result;
    }

    private boolean checkRegionFromConfigFile(Location loc, RegionManager regionManager) {
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
        if (worldGuard == null || worldGuard.getDescription().getVersion().length() == 0) {
            return "undefined";
        } else {
            return worldGuard.getDescription().getVersion().substring(0, 1);
        }
    }

    @Override
    public boolean registerFlag() {
        boolean flagLoaded = false;
        if (worldGuard != null) {
            FlagRegistry registry = worldGuard.getFlagRegistry();
            try {
                // register our flag with the registry
                registry.register(ANTIREDSTONECLOCK_FLAG);
                flagLoaded = true;
            } catch (FlagConflictException e) {
                Bukkit.getLogger().severe("A plugin already use the flag antiredstoneclock. WorldGuard flag support will not work");
            }
        }
        return flagLoaded;
    }

    private ApplicableRegionSet getRegion(RegionManager regionManager, Location loc) {
        Vector vector = new Vector(loc.getX(), loc.getY(), loc.getZ());
        return regionManager.getApplicableRegions(vector);
    }

    private static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }
}
