package com.trafalcraft.antiRedstoneClock.object;

import org.bukkit.Location;

import com.trafalcraft.antiRedstoneClock.Main;

public class RedstoneClock {
	
	private final long endTime;
	private int clock;
	private final Location loc;
	//only for comparator
	private int value;
	
	public RedstoneClock(Location loc){
		endTime = System.currentTimeMillis() /1000+Main.getDelay();
		clock = 0;
		this.loc = loc;
		//only for comparator
		value = 0;
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
}
