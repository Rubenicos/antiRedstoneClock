package com.trafalcraft.antiRedstoneClock;

import com.trafalcraft.antiRedstoneClock.commands.*;
import com.trafalcraft.antiRedstoneClock.listener.ComparatorListener;
import com.trafalcraft.antiRedstoneClock.listener.ObserverListener;
import com.trafalcraft.antiRedstoneClock.listener.RedstoneListener;
import com.trafalcraft.antiRedstoneClock.util.plotSquared.VersionPlotSquared;
import com.trafalcraft.antiRedstoneClock.listener.PistonListener;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import com.trafalcraft.antiRedstoneClock.util.worldGuard.VersionWG;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class Main extends JavaPlugin {

    private static Main instance;
    //Store region and world ignored by the plugin
    private static final ArrayList<String> ignoredWorlds = new ArrayList<>();
    private static final ArrayList<String> ignoredRegions = new ArrayList<>();

    @Override
    public void onLoad() {
        super.onLoad();
        if (VersionWG.getInstance().getWG() != null) {
            this.getLogger().info(String.format("WorldGuard %s found", VersionWG.getInstance().getWG().getVersion()));
            if (VersionWG.getInstance().getWG().registerFlag()) {
                this.getLogger().info("Flag antiredstoneclock registered");
            } else {
                this.getLogger().severe("An error occurred while registering antiredstoneclock flag");
            }
        }
    }

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        instance = this;

        instance.saveDefaultConfig();
        instance.getConfig().options().copyDefaults(true);
        instance.saveConfig();
        instance.reloadConfig();

        if (instance.getConfig().getBoolean("metrics")) {
            this.getLogger().info("Enabling Metrics");
            try {
                Class.forName("org.bstats.bukkit.Metrics");
                Metrics metrics = new Metrics(this, 3091);
                initMetricsChart(metrics);
                this.getLogger().info("Metrics loaded");
            } catch (Exception e) {
                this.getLogger().info("An error occured while trying to enable metrics. Skipping...");
            }
        }

        try {
            Msg.load();
        } catch (YAMLException e) {
            e.printStackTrace();
        }

        registerPluginEvents();

        if (VersionPlotSquared.getInstance().getPlotSquared() != null) {
            VersionPlotSquared.getInstance().getPlotSquared().init();
            this.getLogger().info(String.format("PlotSquared %s found and flag loaded", VersionPlotSquared.getInstance().getPlotSquared().getVersion()));
        }

        long endTime = System.currentTimeMillis();

        long duration = (endTime - startTime);
        this.getLogger().info(String.format("Plugin loaded in %d ms", duration));  //2ms
    }

    //Register events depend on user preferences in config.yml file
    private void registerPluginEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        if (instance.getConfig().getBoolean("checkedClock.comparator")) {
            Material comparator = Material.getMaterial("COMPARATOR");
            if (comparator != null) {
                Bukkit.getServer().getPluginManager().registerEvents(new ComparatorListener(comparator), this);
            } else {
                comparator = Material.getMaterial("REDSTONE_COMPARATOR_OFF");
                Bukkit.getServer().getPluginManager().registerEvents(new ComparatorListener(comparator), this);
                Material comparator2 = Material.getMaterial("REDSTONE_COMPARATOR_ON");
                Bukkit.getServer().getPluginManager().registerEvents(new ComparatorListener(comparator2), this);
            }
        }
        if (Material.getMaterial("OBSERVER") != null && instance.getConfig().getBoolean("checkedClock.observer")) {
            Bukkit.getServer().getPluginManager().registerEvents(new ObserverListener(), this);
        }
        if (instance.getConfig().getBoolean("checkedClock.piston")) {
            Bukkit.getServer().getPluginManager().registerEvents(new PistonListener(), this);
        }
        if (instance.getConfig().getBoolean("checkedClock.redstoneAndRepeater")) {
            Material repeater = Material.getMaterial("REPEATER");
            if (repeater != null) {
                Bukkit.getServer().getPluginManager().registerEvents(new RedstoneListener(repeater), this);
            } else {
                repeater = Material.getMaterial("DIODE_BLOCK_ON");
                Bukkit.getServer().getPluginManager().registerEvents(new RedstoneListener(repeater), this);
                Material repeater2 = Material.getMaterial("DIODE_BLOCK_OFF");
                Bukkit.getServer().getPluginManager().registerEvents(new RedstoneListener(repeater2), this);

            }
        }
    }

    @Override
    public void onDisable() {
        //Nothing to do
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean validCMD = true;
        if (cmd.getName().equalsIgnoreCase("antiredstoneclock")) {
            if (sender.isOp() || sender.hasPermission("antiRedstoneClock.Admin")) {
                if (args.length == 0) {
                    Msg.getHelp(sender);
                    validCMD = false;
                } else {
                    switch (args[0].toUpperCase()) {
                        case "RELOAD":
                            Reload.getInstance().performCMD(sender);
                            break;
                        case "CHECKLIST":
                            CheckList.getInstance().performCMD(sender, args);
                            break;
                        case "SETMAXPULSES":
                            SetMaxPulses.getInstance().performCMD(sender, args);
                            break;
                        case "SETDELAY":
                            SetDelay.getInstance().performCMD(sender, args);
                            break;
                        case "NOTIFYADMIN":
                            NotifyAdmin.getInstance().performCMD(sender, args);
                            break;
                        case "AUTOREMOVEDETECTEDCLOCK":
                            AutoRemoveDetectedClock.getInstance().performCMD(sender, args);
                            break;
                        case "CREATESIGNWHENCLOCKISBREAK":
                            CreateSignWhenClockIsBreak.getInstance().performCMD(sender, args);
                            break;
                        default:
                            Msg.getHelp(sender);
                            validCMD = false;
                    }
                }
            } else {
                sender.sendMessage(Msg.UNKNOWN_CMD.toString());
                validCMD = false;
            }
        } else {
            sender.sendMessage(Msg.UNKNOWN_CMD.toString());
            validCMD = false;
        }
        return validCMD;
    }


    public static Main getInstance() {
        return instance;
    }

    public static Collection<String> getIgnoredWorlds() {
        return ignoredWorlds;
    }

    public static Collection<String> getIgnoredRegions() {
        return ignoredRegions;
    }

    public void initMetricsChart(Metrics metrics) {
        metrics.addCustomChart(new Metrics.SimplePie("worldguard_version", new Callable<String>(){
        
            @Override
            public String call() throws Exception {
                return VersionWG.getInstance().getWG().getVersion();
            }
        }));

        metrics.addCustomChart(new Metrics.SimplePie("plotsquared_version", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return VersionPlotSquared.getInstance().getPlotSquared().getVersion();
            }
        }));

        metrics.addCustomChart(new Metrics.DrilldownPie("config_maxpulses", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            int maxPulses = Main.getInstance().getConfig().getInt("MaxPulses");
            if (maxPulses < 100) {
                entry.put(Integer.toString(maxPulses), 1);
                map.put("<100", entry);
            } else if (maxPulses < 500) {
                entry.put(Integer.toString(maxPulses), 1);
                map.put("<500", entry);
            } else if (maxPulses < 1000) {
                entry.put(Integer.toString(maxPulses), 1);
                map.put("<1000", entry);
            } else if (maxPulses < 10000) {
                entry.put(Integer.toString(maxPulses), 1);
                map.put("<10000", entry);
            }
            return map;
        }));

        metrics.addCustomChart(new Metrics.DrilldownPie("config_delay", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            int delay = Main.getInstance().getConfig().getInt("Delay");
            if (delay < 100) {
                entry.put(Integer.toString(delay), 1);
                map.put("<100", entry);
            } else if (delay < 500) {
                entry.put(Integer.toString(delay), 1);
                map.put("<500", entry);
            } else if (delay < 1000) {
                entry.put(Integer.toString(delay), 1);
                map.put("<1000", entry);
            } else if (delay < 10000) {
                entry.put(Integer.toString(delay), 1);
                map.put("<10000", entry);
            }
            return map;
        }));
    }
}
