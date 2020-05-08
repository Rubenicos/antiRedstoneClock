package com.trafalcraft.antiRedstoneClock.util.plotSquared;

import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.flag.GlobalFlagContainer;
import com.trafalcraft.antiRedstoneClock.util.plotSquared.flag5.AntiRedstoneClockFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

class PlotSquared_5 implements IPlotSquared {

    @Override
    public void init() {
        AntiRedstoneClockFlag antiRedstoneClockFlag = new AntiRedstoneClockFlag(true);
        GlobalFlagContainer.getInstance().addFlag(antiRedstoneClockFlag);
    }

    @Override
    public boolean isAllowedPlot(Location location) {
        com.plotsquared.core.location.Location plotSquaredLocation = new com.plotsquared.core.location.Location(location.getWorld().getName(), location.getBlockX(), 1, location.getBlockZ());
        PlotArea applicablePlotArea = PlotSquared.get().getPlotAreaManager().getPlotArea(plotSquaredLocation);
        return checkFlag(plotSquaredLocation, applicablePlotArea);
    }

    private boolean checkFlag(com.plotsquared.core.location.Location plotSquaredLocation, PlotArea applicablePlotArea) {
        try {
            return !Objects.requireNonNull(applicablePlotArea.getPlot(plotSquaredLocation)).getFlagContainer().getFlag(AntiRedstoneClockFlag.class).getValue();
        } catch (NullPointerException npe) {
            return false;
        }
    }

    @Override
    public String getVersion() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlotSquared");
        return plugin.getDescription().getVersion().split("\\.")[0];
    }
}
