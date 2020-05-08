package com.trafalcraft.antiRedstoneClock.util.plotSquared;

import com.trafalcraft.antiRedstoneClock.Main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class VersionPlotSquared {

    private static VersionPlotSquared instance = null;
    private IPlotSquared plotSquared;

    private VersionPlotSquared() {
        super();
    }

    public static synchronized VersionPlotSquared getInstance() {
        if (VersionPlotSquared.instance == null) {
            VersionPlotSquared.instance = new VersionPlotSquared();
            VersionPlotSquared.instance.init();
        }
        return VersionPlotSquared.instance;
    }

    private void init() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlotSquared");
        if (plugin == null) {
            Bukkit.getLogger().warning("PlotSquared hasn't been found!");
            return;
        }
        String plotSquaredVersion = plugin.getDescription().getVersion().split("\\.")[0];
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            classLoader.loadClass("com.trafalcraft.antiRedstoneClock.util.plotSquared.PlotSquared_" + plotSquaredVersion);
            Class<?> aClass = Class.forName("com.trafalcraft.antiRedstoneClock.util.plotSquared.PlotSquared_" + plotSquaredVersion);
            plotSquared = (IPlotSquared) aClass.getDeclaredConstructors()[0].newInstance();
        } catch (Exception e) {
            Main.getInstance().getLogger().warning("PlotSquared " + plotSquaredVersion + " is not supported");
        }
    }

    public IPlotSquared getPlotSquared() {
        return plotSquared;
    }
} 
