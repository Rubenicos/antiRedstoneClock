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
    public void onComparatorUpdate(BlockPhysicsEvent e) {
        if (e.getBlock().getType() == Material.REDSTONE_COMPARATOR_OFF) {
                if (Util.checkIgnoreWorldsAndRegions(e.getBlock()))
                return;
            if (!RedstoneClockController.contains(e.getBlock().getLocation())) {
                if(e.getBlock().isBlockPowered()
                        || e.getBlock().isBlockIndirectlyPowered()) {
                    try {
                        RedstoneClockController.addRedstone(e.getBlock().getLocation());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }else{
                RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
                int status = 0;
                if(e.getBlock().isBlockPowered()
                        || e.getBlock().isBlockIndirectlyPowered()){
                    status = 1;
                }
                if(redstoneClock.getLastStatus() != status){
                        if(status == 0) {
                                if (!redstoneClock.isEnd()) {
                                        if (redstoneClock.getClock() >= Main.getMaximumPulses()) {
                                                Util.removeRedstoneClock(e.getBlock());
                                        } else {
                                                redstoneClock.addOneToClock();
                                        }
                                }
                        }
                        redstoneClock.updateStatus(status);
                }
            }
        }
    }
}
