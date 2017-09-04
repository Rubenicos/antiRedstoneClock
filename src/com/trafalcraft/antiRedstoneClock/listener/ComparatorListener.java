package com.trafalcraft.antiRedstoneClock.listener;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class ComparatorListener implements Listener{

    @EventHandler
    public void onComparatorUpdate(BlockPhysicsEvent e){
        if(e.getBlock().getType() == Material.REDSTONE_COMPARATOR_OFF){
            if (Util.checkIgnoreWorldsAndRegions(e)) return;
            org.bukkit.material.Comparator comparator = (org.bukkit.material.Comparator) e.getBlock().getState().getData();
            if(!RedstoneClockController.contains(e.getBlock().getLocation())){
                try {
                    RedstoneClockController.addRedstone(e.getBlock().getLocation());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }else {
                RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
                byte comparatorData = comparator.getData();
                if(redstoneClock.getLastStatus() != comparatorData){
                    if (!redstoneClock.isEnd()) {
                        if (redstoneClock.getClock() >= Main.getMaximumPulses()) {
                            Util.removeRedstoneClock(e);
                        } else {
                            if(redstoneClock.getLastStatus() > comparatorData) {
                                redstoneClock.addOneToClock();
                            }
                            redstoneClock.updateStatus(comparatorData);
                        }
                    }
                }
            }
        }
    }
}
