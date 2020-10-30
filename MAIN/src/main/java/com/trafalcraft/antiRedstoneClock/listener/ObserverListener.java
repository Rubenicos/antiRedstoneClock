package com.trafalcraft.antiRedstoneClock.listener;

import java.util.logging.Level;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.exception.DuplicateRedstoneClockObjectException;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class ObserverListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRedstoneComparatorClock(BlockRedstoneEvent e) {
        // Observer move from 0 to 15 when it detect something then move back to 0
        if (e.getOldCurrent() == 0 && e.getBlock().getType() == Material.OBSERVER) {
            if (Util.checkIgnoreWorldsAndRegions(e.getBlock()))
                return;
            RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
            if (redstoneClock == null) {
                try {
                    RedstoneClockController.addRedstone(e.getBlock().getLocation());
                } catch (DuplicateRedstoneClockObjectException e1) {
                    Main.getInstance().getLogger().log(Level.SEVERE, "[antiRedstoneClock]", e1);
                }
            } else {
                updateOrRemoveRedstoneClock(redstoneClock, e.getBlock());
            }
        }
    }

    private void updateOrRemoveRedstoneClock(RedstoneClock redstoneClock, Block block) {
        if (redstoneClock.getLastStatus() == 1) {
            if (redstoneClock.isTimedOut()) {
                RedstoneClockController.removeRedstoneByObject(redstoneClock);
            } else {
                if (redstoneClock.getNumberOfClock() >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                    Util.removeRedstoneClock(redstoneClock, block);
                } else {
                    redstoneClock.addOneToClock();
                    redstoneClock.updateStatus(0);
                }
            }
        } else {
            redstoneClock.updateStatus(1);
        }
    }
}
