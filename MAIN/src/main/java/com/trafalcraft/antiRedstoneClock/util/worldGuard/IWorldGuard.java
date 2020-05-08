package com.trafalcraft.antiRedstoneClock.util.worldGuard;

import org.bukkit.Location;

public interface IWorldGuard {
    boolean isAllowedRegion(Location loc);

    String getVersion();

    boolean registerFlag ();
}
