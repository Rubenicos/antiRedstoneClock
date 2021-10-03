package com.trafalcraft.anti_redstone_clock.util.worldguard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitWorldGuardPlatform;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.DelayedRegionOverlapAssociation;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.trafalcraft.anti_redstone_clock.Main;
import com.trafalcraft.anti_redstone_clock.util.worldGuard.IWorldGuard;

class WorldGuard7 implements IWorldGuard {

    private static StateFlag ANTIREDSTONECLOCK_FLAG;
    private static final WorldGuardPlugin worldGuard = getWorldGuard();

    @Override
    public boolean isAllowedRegion(Location loc) {
        boolean result = false;
        if (worldGuard != null) {
            RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));
            RegionAssociable associable = new DelayedRegionOverlapAssociation(query, BukkitAdapter.adapt(loc));
            if (!set.testState(associable, ANTIREDSTONECLOCK_FLAG)) {
                return true;
            } else {
                RegionManager regionManager = getRegionManager(loc.getWorld());
                result = checkRegionFromConfigFile(loc, regionManager);
            }
        }
        return result;
    }

    private boolean checkRegionFromConfigFile(Location loc, RegionManager regionManager) {
        if (regionManager != null) {
            ApplicableRegionSet regions = regionManager
                    .getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
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
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("anti-redstone-clock", true);
            registry.register(flag);
            ANTIREDSTONECLOCK_FLAG = flag; // only set our field if there was no error
            flagLoaded = true;
        } catch (FlagConflictException e) {
            // some other plugin registered a flag by the same name already.
            // you can use the existing flag, but this may cause conflicts - be sure to
            // check type
            Flag<?> existing = registry.get("anti-redstone-clock");
            if (existing instanceof StateFlag) {
                ANTIREDSTONECLOCK_FLAG = (StateFlag) existing;
            } else {
                Bukkit.getLogger().severe(
                        "A plugin already use the flag anti-redstone-clock. WorldGuard flag support will not work");
            }
        }
        return flagLoaded;
    }

    private RegionManager getRegionManager(World world) {
        BukkitWorldGuardPlatform wgPlatform = (BukkitWorldGuardPlatform) WorldGuard.getInstance().getPlatform();
        com.sk89q.worldedit.world.World worldEditWorld = wgPlatform.getMatcher().getWorldByName(world.getName());
        return wgPlatform.getRegionContainer().get(worldEditWorld);
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
