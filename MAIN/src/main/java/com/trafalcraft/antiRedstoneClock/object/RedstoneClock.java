package com.trafalcraft.antiRedstoneClock.object;

import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.Location;

public class RedstoneClock {

    private final long endTime;
    private int numberOfClock;
    private final Location loc;
    private int value;
    private boolean detected;

    public RedstoneClock(Location loc) {
        endTime = System.currentTimeMillis() / 1000 + Main.getInstance().getConfig().getInt("Delay");
        numberOfClock = 0;
        this.loc = loc;
        detected = false;
    }

    public void addOneToClock() {
        numberOfClock++;
    }

    public int getNumberOfClock() {
        return numberOfClock;
    }

    public Location getLocation() {
        return loc;
    }

    public void updateStatus(int value) {
        this.value = value;
    }

    public int getLastStatus() {
        return value;
    }

    public boolean isTimedOut() {
        return (System.currentTimeMillis() / 1000) >= endTime;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    public boolean getDetected() {
        addOneToClock();
        return detected;
    }
}
