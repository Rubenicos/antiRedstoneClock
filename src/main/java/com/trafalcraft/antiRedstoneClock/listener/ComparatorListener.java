package com.trafalcraft.antiRedstoneClock.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class ComparatorListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRedstoneComparatorClock(BlockRedstoneEvent e) {
        if (Util.checkIgnoreWorldsAndRegions(e.getBlock()))
            return;
        if (checkType(e.getBlock())) {
            if (e.getOldCurrent() == 0) {
                Util.checkAndUpdateRedstoneClockState(e.getBlock());
            }
        }
    }

    private boolean checkType(Block block) {
        boolean result = false;
        try {
            if (block.getType() == Material.COMPARATOR) {
                result = true;
            }
        } catch (NoSuchFieldError e) {
            //1.12.2 and older version compatibility
            if (block.getType() == Material.getMaterial("REDSTONE_COMPARATOR_OFF")
                    || block.getType() == Material.getMaterial("REDSTONE_COMPARATOR_ON")) {
                result = true;
            }
        }
        return result;
    }

}
