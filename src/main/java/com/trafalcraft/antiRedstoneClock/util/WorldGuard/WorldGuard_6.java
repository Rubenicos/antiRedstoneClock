package com.trafalcraft.antiRedstoneClock.util.WorldGuard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.World;

import java.lang.reflect.Method;

class WorldGuard_6 {

    //TODO need to make it cleaner with maven
    static RegionManager getRegionManager(WorldGuardPlugin worldGuard, World world) {
        try {
            Method method = worldGuard.getClass().getMethod("getRegionContainer");
            Object regionContainer = method.invoke(worldGuard);
            method = regionContainer.getClass().getMethod("get", World.class);
            return (RegionManager) method.invoke(regionContainer, world);
        } catch (Exception e) {
            return null;
        }
    }

}
