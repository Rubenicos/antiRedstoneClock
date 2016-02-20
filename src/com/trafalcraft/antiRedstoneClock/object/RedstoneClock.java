package com.trafalcraft.antiRedstoneClock.object;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.trafalcraft.antiRedstoneClock.Main;

public class RedstoneClock {
	
	//Date  initTime = SystemClockFactory.getDatetime();
	private static SimpleDateFormat formater = new SimpleDateFormat("mm");
	private String initTime;
	private int boucle;
	int minutes;
	
	public RedstoneClock(){
		initTime = formater.format(new Date());
		minutes = Integer.parseInt(initTime);
		boucle = 0;
	}
	
	public void addBoucle(){
		boucle++;
	}
	
	public int getBoucle(){
		return boucle;
	}
	
	public int getMinutes(){
		return minutes;
	}
	
	public int getEndTimerInMinutes(){
		String endTime = formater.format(new Date());
		int endMinutes = Integer.parseInt(endTime)+(Main.getDelay()/60);
		return endMinutes;
	}
}
