package com.trafalcraft.antiRedstoneClock;

import com.trafalcraft.antiRedstoneClock.commands.*;
import com.trafalcraft.antiRedstoneClock.listener.ComparatorListener;
import com.trafalcraft.antiRedstoneClock.listener.ObserverListener;
import com.trafalcraft.antiRedstoneClock.listener.PistonListener;
import com.trafalcraft.antiRedstoneClock.listener.RedstoneListener;
import org.bstats.bukkit.Metrics;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import com.trafalcraft.antiRedstoneClock.util.WorldGuard.WorldGuardHook;

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

        //Register events depend on user preferences in config.yml file
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        if (instance.getConfig().getBoolean("checkedClock.comparator")) {
            Bukkit.getServer().getPluginManager().registerEvents(new ComparatorListener(), this);
        }
        if (Material.getMaterial("OBSERVER") != null && instance.getConfig().getBoolean("checkedClock.observer")) {
            Bukkit.getServer().getPluginManager().registerEvents(new ObserverListener(), this);
        }
        if (instance.getConfig().getBoolean("checkedClock.piston")) {
            Bukkit.getServer().getPluginManager().registerEvents(new PistonListener(), this);
        }
        if (instance.getConfig().getBoolean("checkedClock.redstoneAndRepeater")) {
            Bukkit.getServer().getPluginManager().registerEvents(new RedstoneListener(), this);
        }
        long endTime = System.currentTimeMillis();

        long duration = (endTime - startTime);
        this.getLogger().info(String.format("Plugin loaded in %d ms", duration));  //2ms
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("antiredstoneclock")) {
            if (sender.isOp() || sender.hasPermission("antiRedstoneClock.Admin")) {
                if (args.length == 0) {
                    Msg.getHelp(sender);
                    return false;
                }
                if (args[0].equalsIgnoreCase("Reload")) {
                    Reload.getInstance().performCMD(sender);
                } else if (args[0].equalsIgnoreCase("checkList")) {
                    CheckList.getInstance().performCMD(sender, args);
                } else if (args[0].equalsIgnoreCase("setMaxPulses")) {
                    SetMaxPulses.getInstance().performCMD(sender, args);
                } else if (args[0].equalsIgnoreCase("SetDelay")) {
                    SetDelay.getInstance().performCMD(sender, args);
                } else if (args[0].equalsIgnoreCase("NotifyAdmin")) {
                    NotifyAdmin.getInstance().performCMD(sender, args);
                } else if (args[0].equalsIgnoreCase("AutoRemoveDetectedClock")) {
                    AutoRemoveDetectedClock.getInstance().performCMD(sender, args);
                } else if (args[0].equalsIgnoreCase("CreateSignWhenClockIsBreak")) {
                    CreateSignWhenClockIsBreak.getInstance().performCMD(sender, args);
                } else {
                    Msg.getHelp(sender);
                }
            } else {
                sender.sendMessage(Msg.UNKNOWN_CMD.toString());
            }
        } else {
            sender.sendMessage(Msg.UNKNOWN_CMD.toString());
        }
        return false;
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
                return WorldGuardHook.getVersion();
            }
        }));

        metrics.addCustomChart(new Metrics.DrilldownPie("config_maxpulses", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            int maxPulses = Main.getInstance().getConfig().getInt("MaxPulses");
            if (maxPulses < 100) {
                entry.put(""+maxPulses, 1);
                map.put("<100", entry);
            } else if (maxPulses < 500) {
                entry.put(""+maxPulses, 1);
                map.put("<500", entry);
            } else if (maxPulses < 1000) {
                entry.put(""+maxPulses, 1);
                map.put("<1000", entry);
            } else if (maxPulses < 10000) {
                entry.put(""+maxPulses, 1);
                map.put("<10000", entry);
            }
            return map;
        }));

        metrics.addCustomChart(new Metrics.DrilldownPie("config_delay", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            int delay = Main.getInstance().getConfig().getInt("Delay");
            if (delay < 100) {
                entry.put(""+delay, 1);
                map.put("<100", entry);
            } else if (delay < 500) {
                entry.put(""+delay, 1);
                map.put("<500", entry);
            } else if (delay < 1000) {
                entry.put(""+delay, 1);
                map.put("<1000", entry);
            } else if (delay < 10000) {
                entry.put(""+delay, 1);
                map.put("<10000", entry);
            }
            return map;
        }));


    } 

}