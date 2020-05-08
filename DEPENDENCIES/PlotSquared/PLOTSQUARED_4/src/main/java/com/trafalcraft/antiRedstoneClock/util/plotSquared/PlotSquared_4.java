package com.trafalcraft.antiRedstoneClock.util.plotSquared;

import com.github.intellectualsites.plotsquared.api.PlotAPI;
import com.github.intellectualsites.plotsquared.plot.flag.BooleanFlag;
import com.github.intellectualsites.plotsquared.plot.flag.Flag;
import com.github.intellectualsites.plotsquared.plot.flag.Flags;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

class PlotSquared_4 implements IPlotSquared {

    private static final PlotAPI plotAPI = new PlotAPI();;

    @Override
    public void init() {
        BooleanFlag antiRedstoneClock = new AntiRedstoneClockFlag("anti-redstone-clock");
        plotAPI.addFlag(antiRedstoneClock);
    }

    @Override
    public boolean isAllowedPlot(Location loc) {
        com.github.intellectualsites.plotsquared.plot.object.Location location;
        location = new com.github.intellectualsites.plotsquared.plot.object.Location(loc.getWorld().getName(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
        Plot plot = Plot.getPlot(location);
        if(plot != null) {
            Flag<?> redstoneClock = Flags.getFlag("anti-redstone-clock");
            BooleanFlag booleanFlag = (BooleanFlag) redstoneClock;
            return booleanFlag.isFalse(plot);
        }
        return false;
    }

    @Override
    public String getVersion() {
        Plugin plotSquaredPlugin = Main.getInstance().getServer().getPluginManager().getPlugin("PlotSquared");
        return plotSquaredPlugin.getDescription().getVersion();
    }

    public static class AntiRedstoneClockFlag extends BooleanFlag {

        public AntiRedstoneClockFlag(String name) {
            super(name);
        }
    }
}
