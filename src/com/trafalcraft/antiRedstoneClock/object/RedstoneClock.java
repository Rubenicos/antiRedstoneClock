package com.trafalcraft.antiRedstoneClock.object;

import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.Location;

public class RedstoneClock {

	private final long endTime;
	private int clock;
	private final Location loc;
	//only for comparator
	private int value;
	private boolean detected;

	public RedstoneClock(Location loc){
		endTime = System.currentTimeMillis() /1000+Main.getDelay();
		clock = 0;
		this.loc = loc;
		detected = false;
	}

	public void addOneToClock(){
		clock++;
	}

	public int getClock(){
		return clock;
	}

	public Location getLocation(){
		return loc;
	}

	//only for comparator
	public void updateStatus(int value){
		this.value = value;
	}

	//only for comparator
	public int getLastStatus(){
		return value;
	}

	public boolean isEnd(){
		return (System.currentTimeMillis() /1000)>=endTime;
	}

	public void setDetected(boolean detected) {
		this.detected = detected;
	}

	public boolean getDetected() {
		addOneToClock();
		return detected;
	}
}