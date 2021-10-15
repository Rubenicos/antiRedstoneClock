package com.trafalcraft.anti_redstone_clock.util.worldGuard;

import org.bukkit.Location;

public interface IWorldGuard {
    boolean isAllowedRegion(Location loc);

    String getVersion();

    boolean registerFlag ();
}
