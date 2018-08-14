package com.trafalcraft.antiRedstoneClock.util.WorldGuard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitWorldGuardPlatform;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.World;

class WorldGuard_7 {
    static RegionManager getRegionManager(WorldGuardPlugin worldGuard, World world) {
        BukkitWorldGuardPlatform wgPlatform = (BukkitWorldGuardPlatform)
                WorldGuard.getInstance().getPlatform();
        RegionManager regionManager = wgPlatform.getRegionContainer().get(wgPlatform.getWorldByName(world.getName()));
        return regionManager;
    }
}