package com.trafalcraft.antiRedstoneClock.listener;

import java.util.logging.Level;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.exception.DuplicateRedstoneClockObjectException;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;

import org.bukkit.block.Block;
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
            updateOrRemoveRedstoneClock(e.getBlock());
        } else {
            try {
                RedstoneClockController.addRedstone(e.getBlock().getLocation());
            } catch (DuplicateRedstoneClockObjectException e1) {
                Main.getInstance().getLogger().log(Level.SEVERE, "[antiRedstoneClock]", e1);
            }
        }
    }

    private void updateOrRemoveRedstoneClock(Block block) {
        RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(block.getLocation());
        if (redstoneClock.getLastStatus() == 1) {
            if (!redstoneClock.isTimedOut()) {
                if (redstoneClock.getNumberOfClock() >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                    Util.removeRedstoneClock(block);
                } else {
                    redstoneClock.addOneToClock();
                    redstoneClock.updateStatus(0);
                }
            } else {
                RedstoneClockController.removeRedstoneByObject(redstoneClock);
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
