package com.trafalcraft.antiRedstoneClock.util.WorldGuard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitWorldGuardPlatform;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.World;

class WorldGuard7 implements WorldGuardAdaptor{

    @Override
    public RegionManager getRegionManager(WorldGuardPlugin worldGuard, World world) {
        BukkitWorldGuardPlatform wgPlatform = (BukkitWorldGuardPlatform)
                WorldGuard.getInstance().getPlatform();
        com.sk89q.worldedit.world.World worldEditWorld = wgPlatform.getMatcher().getWorldByName(world.getName());
        return wgPlatform.getRegionContainer().get(worldEditWorld);
    }
}