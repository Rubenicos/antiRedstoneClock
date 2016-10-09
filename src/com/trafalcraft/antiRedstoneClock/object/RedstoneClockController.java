package com.trafalcraft.antiRedstoneClock.object;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Location;

import com.google.common.collect.Maps;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;

public class RedstoneClockController {
	//private static Map<Location, RedstoneClock> activeMap = Maps.newHashMap();
	private static ConcurrentMap<Location, RedstoneClock> activeMap = Maps.newConcurrentMap();
	
	public static void addRedstone(Location location) throws Exception{
		if(contains(location)){
			 throw new Exception(CustomConfig.ERREUR+CustomConfig.duplicate_object.toString());
		}else{
			activeMap.put(location, new RedstoneClock(location));
			return;
		}
	}
	
	public static boolean contains(Location location){
		if(activeMap.containsKey(location)){
			return true;
		}
		return false;
	}
	
	public static void removeRedstoneByLocation(Location location){
		if(activeMap.containsKey(location)){
			activeMap.remove(location);
		}
	}
	
	public static void removeRedstoneByObject(RedstoneClock rc){
		if(activeMap.containsValue(rc)){
			activeMap.remove(rc.getLocation());
		}
	}
	
	public static RedstoneClock getRedstoneClock(Location location){
		return activeMap.get(location);
	}
	
	public static int size(Location location){
		return activeMap.size();
	}

	public static Map<Location, RedstoneClock> getHashMap(){
		return activeMap;
	}
	
    public static Collection<RedstoneClock> getAll() {
        return activeMap.values();
    }
    
    public static Collection<Location> getAllLoc() {
        return activeMap.keySet();
    }
    
}
