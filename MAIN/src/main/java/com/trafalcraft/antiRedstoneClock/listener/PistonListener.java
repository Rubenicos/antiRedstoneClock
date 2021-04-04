package com.trafalcraft.antiRedstoneClock.listener;

import java.util.logging.Level;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.exception.DuplicateRedstoneClockObjectException;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.CheckTPS;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonListener implements Listener {

    @EventHandler
    public void onPistonExtendEvent(BlockPistonExtendEvent e) {
        if (!CheckTPS.isTpsOK() && Util.checkIgnoreWorldsAndRegions(e.getBlock()))
            return;
        RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
        if (redstoneClock == null) {
            try {
                RedstoneClockController.addRedstone(e.getBlock().getLocation());
            } catch (DuplicateRedstoneClockObjectException e1) {
                Main.getInstance().getLogger().log(Level.SEVERE, "[antiRedstoneClock]", e1);
            }
        } else if (!redstoneClock.getDetected()) {
            updateOrRemoveRedstoneClock(redstoneClock, e.getBlock());
        }
    }

    private void updateOrRemoveRedstoneClock(RedstoneClock redstoneClock, Block block) {
        if (redstoneClock.getLastStatus() == 1) {
            if (!redstoneClock.isTimedOut()) {
                if (redstoneClock.getNumberOfClock() >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                    Util.removeRedstoneClock(redstoneClock, block);
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
        if (CheckTPS.isTpsOK()) {
            RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
            if (redstoneClock != null) {
                redstoneClock.updateStatus(1);
            }
        }
    }
}
