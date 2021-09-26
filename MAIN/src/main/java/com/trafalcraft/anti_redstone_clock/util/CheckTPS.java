package com.trafalcraft.anti_redstone_clock.util;

import org.bukkit.Bukkit;

import com.trafalcraft.anti_redstone_clock.Main;

public class CheckTPS {

    private static long lastPoll = System.currentTimeMillis();
    private static boolean tpsIsOK = true;
    private static int repeatingTaskID = -1;
    private static long tps = 20;

    public static boolean isTpsOK() {
        return tpsIsOK;
    }

    public static long getTPS() {
        return tps;
    }

    public static void initCheckTPS(int minimumTPS, int maximumTPS, int interval) {
        if (repeatingTaskID != -1) {
            Bukkit.getScheduler().cancelTask(repeatingTaskID);
            tpsIsOK = true;
        }
        if (minimumTPS > 0 || maximumTPS > 0) {
            repeatingTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
                long now = System.currentTimeMillis();
                long timeSpent = (now - lastPoll) / 1000;
                if (timeSpent == 0){
                    timeSpent = 1;
                }
                tps = (20*interval / timeSpent);
                if (minimumTPS < 0) {
                    tpsIsOK = tps <= maximumTPS;
                } else if (maximumTPS < 0) {
                    tpsIsOK = tps >= minimumTPS;
                } else {
                    tpsIsOK = tps >= minimumTPS && tps <= maximumTPS;
                }
                lastPoll = now;
            }, 0, 20*interval);
        } else {
            tpsIsOK = true;
        }
    }
}
