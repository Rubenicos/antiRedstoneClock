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
        if (Util.checkIgnoreWorldsAndRegions(e.getBlock()))
            return;
        // Observer move from 0 to 15 when it detect something then move back to 0
        if (e.getBlock().getType() == Material.OBSERVER && e.getOldCurrent() == 0) {
            if (!RedstoneClockController.contains(e.getBlock().getLocation())) {
                try {
                    RedstoneClockController.addRedstone(e.getBlock().getLocation());
                } catch (DuplicateRedstoneClockObjectException e1) {
                    Main.getInstance().getLogger().log(Level.SEVERE, "[antiRedstoneClock]", e1);
                }
            } else {
                updateOrRemoveRedstoneClock(e.getBlock());
            }
        }
    }

    private void updateOrRemoveRedstoneClock(Block block) {
        RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(block.getLocation());
        if (redstoneClock.getLastStatus() == 1) {
            if (redstoneClock.isTimedOut()) {
                RedstoneClockController.removeRedstoneByObject(redstoneClock);
            } else {
                if (redstoneClock.getNumberOfClock() >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                    Util.removeRedstoneClock(block);
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
