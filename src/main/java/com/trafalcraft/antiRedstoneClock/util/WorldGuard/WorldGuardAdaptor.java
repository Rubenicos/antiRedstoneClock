package com.trafalcraft.antiRedstoneClock.util.WorldGuard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.World;

interface WorldGuardAdaptor {

    RegionManager getRegionManager(WorldGuardPlugin worldGuard, World world);
}