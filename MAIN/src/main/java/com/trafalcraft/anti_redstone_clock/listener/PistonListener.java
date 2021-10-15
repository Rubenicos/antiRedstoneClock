package com.trafalcraft.anti_redstone_clock.listener;

import java.util.logging.Level;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import com.trafalcraft.anti_redstone_clock.Main;
import com.trafalcraft.anti_redstone_clock.exception.DuplicateRedstoneClockObjectException;
import com.trafalcraft.anti_redstone_clock.object.RedstoneClock;
import com.trafalcraft.anti_redstone_clock.object.RedstoneClockController;
import com.trafalcraft.anti_redstone_clock.util.CheckTPS;

public class PistonListener implements Listener {

    @EventHandler
    public void onPistonExtendEvent(BlockPistonExtendEvent e) {
        if (!CheckTPS.isTpsOK() || Util.checkIgnoreWorldsAndRegions(e.getBlock()))
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
