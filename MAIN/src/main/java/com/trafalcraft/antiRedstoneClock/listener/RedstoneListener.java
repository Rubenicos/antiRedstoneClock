package com.trafalcraft.antiRedstoneClock.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class RedstoneListener implements Listener {

    private Material repeaterMaterial;

    public RedstoneListener(Material repeaterMaterial) {
        this.repeaterMaterial = repeaterMaterial;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRedstoneClock(BlockRedstoneEvent e) {
        if ((e.getBlock().getType() == Material.REDSTONE_WIRE || e.getBlock().getType() == repeaterMaterial)
                && e.getOldCurrent() == 0
                && Util.checkIgnoreWorldsAndRegions(e.getBlock())) {
            Util.checkAndUpdateRedstoneClockState(e.getBlock());
        }
    }
}
