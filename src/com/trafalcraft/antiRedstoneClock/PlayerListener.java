package com.trafalcraft.antiRedstoneClock;

import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBreakRedstone(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (checkRedStoneItems_1_13(block.getType())
                || checkRedStoneItemsOlderThan_1_13(block.getType())) {
            cleanRedstone(block);
        } else if (block.getType() == Material.getMaterial("SIGN")
                || block.getType() == Material.getMaterial("SIGN_POST")) {
            BlockState blockState = block.getState();
            Sign sign = (Sign) blockState;
            if (checkSign(sign)) {
                e.setCancelled(true);
                block.setType(Material.AIR);
            }
        } else {
            block = block.getRelative(BlockFace.UP);
            if (checkRedStoneItems_1_13(block.getType())
                    || checkRedStoneItemsOlderThan_1_13(block.getType())) {
                cleanRedstone(block);
            } else if (block.getType() == Material.getMaterial("SIGN")
                    || block.getType() == Material.getMaterial("SIGN_POST")) {
                BlockState blockState = block.getState();
                Sign sign = (Sign) blockState;
                if (checkSign(sign)) {
                    block.setType(Material.AIR);
                }
            }
        }
    }

    private boolean checkRedStoneItems_1_13(Material type) {
        return type == Material.getMaterial("REDSTONE_WIRE")
                || type == Material.getMaterial("REPEATER")
                || type == Material.getMaterial("PISTON")
                || type == Material.getMaterial("STICKY_PISTON")
                || type == Material.getMaterial("COMPARATOR")
                || type == Material.getMaterial("OBSERVER");
    }

    private boolean checkRedStoneItemsOlderThan_1_13(Material type) {
        return type == Material.getMaterial("DIODE_BLOCK_OFF")
                || type == Material.getMaterial("DIODE_BLOCK_ON")
                || type == Material.getMaterial("PISTON_BASE")
                || type == Material.getMaterial("PISTON_EXTENSION")
                || type == Material.getMaterial("PISTON_STICKY_BASE")
                || type == Material.getMaterial("REDSTONE_COMPARATOR_OFF")
                || type == Material.getMaterial("REDSTONE_COMPARATOR_ON");
    }

    private void cleanRedstone(Block block) {
        if (RedstoneClockController.contains(block.getLocation())) {
            RedstoneClockController.removeRedstoneByLocation(block.getLocation());
        }
    }

    private boolean checkSign(Sign sign) {
        return (sign.getLine(0).equalsIgnoreCase(Main.getInstance().getConfig().getString("Sign.Line1")
                .replace("&", "§"))
                && sign.getLine(1).equalsIgnoreCase(Main.getInstance().getConfig().getString("Sign.Line2")
                .replace("&", "§"))
                && sign.getLine(2).equalsIgnoreCase(Main.getInstance().getConfig().getString("Sign.Line3")
                .replace("&", "§"))
                && sign.getLine(3).equalsIgnoreCase(Main.getInstance().getConfig().getString("Sign.Line4")
                .replace("&", "§")));
    }
}
