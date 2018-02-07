package com.trafalcraft.antiRedstoneClock;

import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;

public class PlayerListener implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerBreakRedstone(BlockBreakEvent e) {
        try{
            if (e.getBlock().getType() == Material.REDSTONE_WIRE
                    || e.getBlock().getType() == Material.DIODE_BLOCK_ON
                    || e.getBlock().getType() == Material.DIODE_BLOCK_OFF
                    || e.getBlock().getType() == Material.PISTON_BASE
                    || e.getBlock().getType() == Material.PISTON_EXTENSION
                    || e.getBlock().getType() == Material.PISTON_MOVING_PIECE
                    || e.getBlock().getType() == Material.PISTON_STICKY_BASE
                    || e.getBlock().getType() == Material.REDSTONE_COMPARATOR
                    || e.getBlock().getType() == Material.REDSTONE_COMPARATOR_OFF
                    || e.getBlock().getType() == Material.REDSTONE_COMPARATOR_ON
                    || e.getBlock().getType() == Material.OBSERVER) {
                if (RedstoneClockController.contains(e.getBlock().getLocation())) {
                    RedstoneClockController.removeRedstoneByLocation(e.getBlock().getLocation());
                }
            }
        }catch(java.lang.NoSuchFieldError ignored){}
            if (e.getBlock().getType() == Material.SIGN || e.getBlock().getType() == Material.SIGN_POST) {
                    BlockState block = e.getBlock().getState();
                    Sign sign = (Sign) block;
                    if (sign.getLine(0).equalsIgnoreCase(Main.getLine1())
                            && sign.getLine(1).equalsIgnoreCase(Main.getLine2())
                            && sign.getLine(2).equalsIgnoreCase(Main.getLine3())
                            && sign.getLine(3).equalsIgnoreCase(Main.getLine4())) {

                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                    }
            }
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onItemDrop(BlockPhysicsEvent e){
            if (e.getBlock().getType() == Material.SIGN || e.getBlock().getType() == Material.SIGN_POST) {
                    BlockState block = e.getBlock().getState();
                    Sign sign = (Sign) block;
                    if ((sign.getLine(0).equalsIgnoreCase(Main.getLine1())
                            && sign.getLine(1).equalsIgnoreCase(Main.getLine2())
                            && sign.getLine(2).equalsIgnoreCase(Main.getLine3())
                            && sign.getLine(3).equalsIgnoreCase(Main.getLine4())
                            && e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)) {

                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                    }
            }
	}
}
