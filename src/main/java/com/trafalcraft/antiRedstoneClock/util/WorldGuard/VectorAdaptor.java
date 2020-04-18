package com.trafalcraft.antiRedstoneClock.util.WorldGuard;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.trafalcraft.antiRedstoneClock.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

class VectorAdaptor {
    private VectorAdaptor() {}

    static ApplicableRegionSet getRegion(RegionManager regionManager, Location loc) {
        ApplicableRegionSet region = null;
        try {
            region = regionManager.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
        } catch (NoClassDefFoundError e) {
            //Compatibility for older worldguard version
            try {
                Class<?> vector = Class.forName("com.sk89q.worldedit.Vector");
                Object vectorInst = vector.getConstructor(double.class, double.class, double.class)
                        .newInstance(loc.getX(), loc.getY(), loc.getZ());
                Method applicationRegions = regionManager.getClass()
                        .getMethod("getApplicableRegions", vector);
                region = (ApplicableRegionSet) applicationRegions.invoke(regionManager, vectorInst);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                    | IllegalAccessException | InvocationTargetException e1) {
                Bukkit.getLogger().severe("error with worldEdit vector, please report this issue");
                Main.getInstance().getLogger().log(Level.SEVERE, "[antiRedstoneClock]", e1);
            }
        }
        return region;
    }
}
