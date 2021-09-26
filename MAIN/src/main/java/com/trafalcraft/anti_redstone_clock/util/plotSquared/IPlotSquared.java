package com.trafalcraft.antiRedstoneClock.util.plotSquared;

import org.bukkit.Location;

public interface IPlotSquared {

    void init();

    boolean isAllowedPlot(Location location) ;

    String getVersion();
}
