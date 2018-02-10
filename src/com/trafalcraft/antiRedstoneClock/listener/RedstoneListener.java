package com.trafalcraft.antiRedstoneClock.listener;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class RedstoneListener implements Listener{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRedstoneClock(BlockRedstoneEvent e){
        if (Util.checkIgnoreWorldsAndRegions(e.getBlock()))
            return;
        if(e.getBlock().getType() == Material.REDSTONE_WIRE){
            if(e.getOldCurrent() == 0){
                Util.checkAndUpdateRedstoneClockState(e.getBlock());
            }
        }else if(e.getBlock().getType() == Material.DIODE_BLOCK_ON
                || e.getBlock().getType() == Material.REDSTONE_COMPARATOR_ON){

            if(!RedstoneClockController.contains(e.getBlock().getLocation())){
                try {
                    RedstoneClockController.addRedstone(e.getBlock().getLocation());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }else{

                RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
                if(!redstoneClock.isEnd()){
                    if (redstoneClock.getNumberOfClock() >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                        Util.removeRedstoneClock(e.getBlock());
                    }else{
                        redstoneClock.addOneToClock();
                    }
                }
            }


        }
    }
}
