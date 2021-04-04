package com.trafalcraft.antiRedstoneClock.util.worldGuard;

import com.trafalcraft.antiRedstoneClock.Main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class VersionWG {

    private static VersionWG instance = null;
    private IWorldGuard worldGuard;

    private VersionWG() {
        super();
    }

    public static synchronized VersionWG getInstance() {
        if (VersionWG.instance == null) {
            VersionWG.instance = new VersionWG();
            VersionWG.instance.init();
        }
        return VersionWG.instance;
    }

    private void init() {
        if (Main.getInstance().getConfig().getBoolean("worldGuardSupport")) {
            Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
            if (plugin == null) {
                Bukkit.getLogger().warning("WorldGuard hasn't been found!");
                return;
            }
            String wgVersion = plugin.getDescription().getVersion().split("\\.")[0];
            try {
                ClassLoader classLoader = Main.class.getClassLoader();
                classLoader.loadClass("com.trafalcraft.antiRedstoneClock.util.worldGuard.WorldGuard_" + wgVersion);
                Class<?> aClass = Class.forName("com.trafalcraft.antiRedstoneClock.util.worldGuard.WorldGuard_" + wgVersion);
                worldGuard = (IWorldGuard) aClass.getDeclaredConstructors()[0].newInstance();
            } catch (Exception e) {
                Main.getInstance().getLogger().warning("WorldGuard " + wgVersion + " is not supported");
            }
        }
    }

    public IWorldGuard getWG() {
        return worldGuard;
    }
} 
