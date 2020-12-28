package com.trafalcraft.antiRedstoneClock.util;

import com.trafalcraft.antiRedstoneClock.Main;

import org.bukkit.Bukkit;

public class CheckTPS {

    private static long lastPoll = System.currentTimeMillis();
    private static boolean tpsIsOK = false;
    private static int repeatingTaskID = -1;


    public static boolean isTpsOK() {
        return tpsIsOK;
    }

    public static void initCheckTPS(int minimumTPS, int maximumTPS, int interval) {
        if (repeatingTaskID != -1) {
            Bukkit.getScheduler().cancelTask(repeatingTaskID);
        }
        if (minimumTPS != -1 && maximumTPS != -1) {
            repeatingTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
                if (minimumTPS <= 0) {
                    Bukkit.getScheduler().cancelTask(repeatingTaskID);
                    repeatingTaskID = -1;
                } else {
                    long now = System.currentTimeMillis();
                    long timeSpent = (now - lastPoll) / 1000;
                    if (timeSpent == 0){
                        timeSpent = 1;
                    }
                    long tps = (20*interval / timeSpent);
                    if (minimumTPS < 0) {
                        tpsIsOK = tps <= maximumTPS;
                    } else if (maximumTPS < 0) {
                        tpsIsOK = tps >= minimumTPS;
                    } else {
                        tpsIsOK = tps >= minimumTPS && tps <= maximumTPS;
                    }
                    lastPoll = now;
                }
            }, 0, 20*interval);
        }
    }
}
