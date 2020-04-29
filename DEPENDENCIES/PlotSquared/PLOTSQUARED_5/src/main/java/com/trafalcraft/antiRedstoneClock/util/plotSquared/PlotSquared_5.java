package com.trafalcraft.antiRedstoneClock.util.plotSquared;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.flag.GlobalFlagContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class PlotSquared_5 implements IPlotSquared {

    public void init() {
        AntiRedstoneClockFlag antiRedstoneClockFlag = new AntiRedstoneClockFlag(true);
        GlobalFlagContainer.getInstance().addFlag(antiRedstoneClockFlag);
    }

    public boolean isAllowedPlot(Location location) {
        com.plotsquared.core.location.Location plotSquaredLocation = new com.plotsquared.core.location.Location(location.getWorld().getName(), location.getBlockX(), 1, location.getBlockZ());
        PlotArea applicablePlotArea = PlotSquared.get().getPlotAreaManager().getPlotArea(plotSquaredLocation);
        return applicablePlotArea != null && !applicablePlotArea.getPlot(plotSquaredLocation).getFlagContainer().getFlag(AntiRedstoneClockFlag.class).getValue();
    }

    public String getVersion() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlotSquared");
        return plugin.getDescription().getVersion().split("\\.")[0];
    }
}