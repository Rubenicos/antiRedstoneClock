package com.trafalcraft.antiRedstoneClock.object;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Location;

import com.google.common.collect.Maps;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;

public class RedstoneClockController {
	private final Map<Location, RedstoneClock> activeMap = Maps.newHashMap();
	
	public void addRedstone(Location location) throws Exception{
		if(contains(location)){
			 throw new Exception(CustomConfig.ERREUR+CustomConfig.duplicate_object.toString());
		}else{
			activeMap.put(location, new RedstoneClock());
			return;
		}
	}
	
	public boolean contains(Location location){
		if(this.activeMap.containsKey(location)){
			return true;
		}
		return false;
	}
	
	public void removeRedstoneByLocation(Location location){
		if(this.activeMap.containsKey(location)){
			activeMap.remove(location);
		}
	}
	
	public void removeRedstoneByObject(RedstoneClock rc){
		if(this.activeMap.containsKey(rc)){
			activeMap.remove(rc);
		}
	}
	
	public RedstoneClock getRedstoneClock(Location location){
		return activeMap.get(location);
	}
	
	public int size(Location location){
		return activeMap.size();
	}

    public Collection<RedstoneClock> getAll() {
        return activeMap.values();
    }
}
