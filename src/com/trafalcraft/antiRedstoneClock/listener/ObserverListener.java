package com.trafalcraft.antiRedstoneClock.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.material.Observer;

public class ObserverListener implements Listener {

    @EventHandler
    public void onObserverUpdate(BlockPhysicsEvent e){
        if(e.getBlock().getType() == Material.OBSERVER){
            Observer obs = (Observer) e.getBlock().getState().getData();
            if(obs.isPowered()){
                if (Util.checkIgnoreWorldsAndRegions(e)) return;
                Util.checkAndUpdateRedstoneClockState(e);
            }
        }
    }
}
