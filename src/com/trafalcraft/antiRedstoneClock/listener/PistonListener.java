package com.trafalcraft.antiRedstoneClock.listener;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener implements Listener {
    @EventHandler
    public void onPistonExtendEvent(BlockPistonExtendEvent e) {
        if (Util.checkIgnoreWorldsAndRegions(e.getBlock()))
            return;
        if (RedstoneClockController.contains(e.getBlock().getLocation())) {
            RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
            if (redstoneClock.getLastStatus() == 1) {
                if (!redstoneClock.isTimedOut()) {
                    if (redstoneClock.getNumberOfClock() >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                        Util.removeRedstoneClock(e.getBlock());
                    } else {
                        redstoneClock.addOneToClock();
                        redstoneClock.updateStatus(0);
                    }
                } else {
                    RedstoneClockController.removeRedstoneByObject(redstoneClock);
                }
            }
        } else {
            try {
                RedstoneClockController.addRedstone(e.getBlock().getLocation());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPistonRetractEvent(BlockPistonRetractEvent e) {
        if (RedstoneClockController.contains(e.getBlock().getLocation())) {
            RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
            redstoneClock.updateStatus(1);
        }
    }
}
