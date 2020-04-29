package com.trafalcraft.antiRedstoneClock.util.worldGuard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitWorldGuardPlatform;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

class WorldGuard_7 implements IWorldGuard {

    @Override
    public boolean isAllowedRegion(Location loc) {
        WorldGuardPlugin worldGuard = getWorldGuard();
        if (worldGuard == null) {
            return false;
        }
        RegionManager regionManager = getRegionManager(worldGuard, loc.getWorld());

        if (regionManager != null) {
            ApplicableRegionSet regions = regionManager.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
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

    private RegionManager getRegionManager(WorldGuardPlugin worldGuard, World world) {
        BukkitWorldGuardPlatform wgPlatform = (BukkitWorldGuardPlatform)
                WorldGuard.getInstance().getPlatform();
        com.sk89q.worldedit.world.World worldEditWorld = wgPlatform.getMatcher().getWorldByName(world.getName());
        return wgPlatform.getRegionContainer().get(worldEditWorld);
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

}