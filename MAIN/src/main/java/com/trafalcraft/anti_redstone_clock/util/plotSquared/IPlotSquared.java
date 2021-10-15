package com.trafalcraft.anti_redstone_clock.util.plotSquared;

import org.bukkit.Location;

public interface IPlotSquared {

    void init();

    boolean isAllowedPlot(Location location) ;

    String getVersion();
}
