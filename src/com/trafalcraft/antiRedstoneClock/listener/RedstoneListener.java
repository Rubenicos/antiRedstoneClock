package com.trafalcraft.antiRedstoneClock.listener;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class RedstoneListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRedstoneClock(BlockRedstoneEvent e) {
        if (Util.checkIgnoreWorldsAndRegions(e.getBlock()))
            return;
        if (e.getBlock().getType() == Material.REDSTONE_WIRE) {
            if (e.getOldCurrent() == 0) {
                Util.checkAndUpdateRedstoneClockState(e.getBlock());
            }
        } else if (checkTypeAndItemPowered(e.getBlock())) {
            if (!RedstoneClockController.contains(e.getBlock().getLocation())) {
                try {
                    RedstoneClockController.addRedstone(e.getBlock().getLocation());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
                if (!redstoneClock.isEnd()) {
                    if (redstoneClock.getNumberOfClock() >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                        Util.removeRedstoneClock(e.getBlock());
                    } else {
                        redstoneClock.addOneToClock();
                    }
                }
            }
        }
    }

    private boolean checkTypeAndItemPowered(Block block) {
        boolean result = false;
        try {
            if (block.getType() == Material.REPEATER) {
                Powerable powerable = (Powerable) block.getBlockData();
                if (powerable.isPowered()) {
                    result = true;
                }
            }
        } catch (NoSuchFieldError e) {
            //1.12.2 and older version compatibility
            if (block.getType() == Material.getMaterial("DIODE_BLOCK_ON")) {
                result = true;
            }
        }
        return result;
    }
}
