package com.trafalcraft.antiRedstoneClock.object;

import org.bukkit.Location;

import com.trafalcraft.antiRedstoneClock.Main;

public class RedstoneClock {
	
	private long endTime;
	private int boucle;
	private Location loc;
	//only for comparator
	private boolean lastStatus;
	
	public RedstoneClock(Location loc){
		endTime = (long) System.currentTimeMillis()/1000+Main.getDelay();
		boucle = 0;
		this.loc = loc;
		//only for comparator
		lastStatus = false;
	}
	
	public void addBoucle(){
		boucle++;
	}
	
	public int getBoucle(){
		return boucle;
	}
	
	public long getSecondes(){
		return endTime;
	}
	
	public Location getLocation(){
		return loc;
	}
	
	//only for comparator
	public void updateStatus(){
		if(lastStatus){
			lastStatus = false;
		}else{
			lastStatus = true;
		}
	}
	//only for comparator
	public boolean getlastStatus(){
		return lastStatus;
	}
	
	public boolean isEnd(){
		return ((long) System.currentTimeMillis()/1000)>=endTime;
	}
}
