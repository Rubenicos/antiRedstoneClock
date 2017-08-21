package com.trafalcraft.antiRedstoneClock;

import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;
import com.trafalcraft.antiRedstoneClock.util.WorldGuardLink;
import org.bukkit.material.Observer;

public class PlayerListener implements Listener {
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onRedstoneClock(BlockRedstoneEvent e){
        if (checkIgnoreWorldsAndRegions(e)) return;
        if(e.getBlock().getType() == Material.REDSTONE_WIRE){
			if(e.getOldCurrent() == 0){
                checkAndUpdateRedstoneClockState(e);
            }
		}else if(e.getBlock().getType() == Material.DIODE_BLOCK_ON
				|| e.getBlock().getType() == Material.REDSTONE_COMPARATOR_ON){

			if(!RedstoneClockController.contains(e.getBlock().getLocation())){
				try {
					RedstoneClockController.addRedstone(e.getBlock().getLocation());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}else{

                RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
                if(!redstoneClock.isEnd()){
					if(redstoneClock.getClock() >= Main.getMaximumPulses()){
						final Block b = e.getBlock();
						if(Main.isDropItems()){
							e.getBlock().breakNaturally();
						}else{
							e.getBlock().setType(Material.AIR);
						}
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

							@Override
							public void run() {
								b.setType(Material.SIGN_POST);
								BlockState block = b.getState();
								Sign sign = (Sign)block;
								sign.setLine(0, Main.getLine1());
								sign.setLine(1, Main.getLine2());
								sign.setLine(2, Main.getLine3());
								sign.setLine(3, Main.getLine4());
								sign.update();
								Bukkit.getLogger().info(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", b.getX()+"").replace("$Y", b.getY()+"").replace("$Z", b.getZ()+"").replace("$World", b.getWorld().getName()));
								if(Main.isNotifyAdmin()){
									for(Player p : Bukkit.getOnlinePlayers()){
										if(p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")){
											p.sendMessage(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", b.getX()+"").replace("$Y", b.getY()+"").replace("$Z", b.getZ()+"").replace("$World", b.getWorld().getName()));
										}
									}
								}
								RedstoneClockController.removeRedstoneByLocation(b.getLocation());
							}
						}, 1L);
					}else{
						redstoneClock.addOneToClock();
					}
				}
			}


		}
	}

	@EventHandler
	public void onObserverUpdate(BlockPhysicsEvent e){
		if(e.getBlock().getType() == Material.OBSERVER){
			Observer obs = (Observer) e.getBlock().getState().getData();
            if(obs.isPowered()){
                if (checkIgnoreWorldsAndRegions(e)) return;
                checkAndUpdateRedstoneClockState(e);
            }
		}
	}

    @EventHandler
    public void onComparatorUpdate(BlockPhysicsEvent e){
       if(e.getBlock().getType() == Material.REDSTONE_COMPARATOR_OFF){
           if (checkIgnoreWorldsAndRegions(e)) return;
           org.bukkit.material.Comparator comparator = (org.bukkit.material.Comparator) e.getBlock().getState().getData();
           if(!RedstoneClockController.contains(e.getBlock().getLocation())){
               try {
                   RedstoneClockController.addRedstone(e.getBlock().getLocation());
               } catch (Exception e1) {
                   e1.printStackTrace();
               }
           }else {
               RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(e.getBlock().getLocation());
               byte comparatorData = comparator.getData();
               if(redstoneClock.getLastStatus() != comparatorData){
                   if (!redstoneClock.isEnd()) {
                       if (redstoneClock.getClock() >= Main.getMaximumPulses()) {
                           removeRedstoneClock(e);
                       } else {
                           if(redstoneClock.getLastStatus() > comparatorData) {
                               redstoneClock.addOneToClock();
                           }
                           redstoneClock.updateStatus(comparatorData);
                       }
                   }
               }
           }
        }
    }

    private void checkAndUpdateRedstoneClockState(BlockEvent e) {
        if(!RedstoneClockController.contains(e.getBlock().getLocation())){
            try {
                RedstoneClockController.addRedstone(e.getBlock().getLocation());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else{
            if(!RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).isEnd()){
                if(RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).getClock() >= Main.getMaximumPulses()){
                    removeRedstoneClock(e);
                }else{
                    RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).addOneToClock();
                }
            }
        }
    }

    private boolean checkIgnoreWorldsAndRegions(BlockEvent e) {
        for(String ignoreWorld: Main.getIgnoredWorlds()){
            if(e.getBlock().getWorld().getName().equals(ignoreWorld)){
                return true;
            }
        }
        return WorldGuardLink.checkAllowedRegion(e.getBlock().getLocation());
    }

    private void removeRedstoneClock(BlockEvent e) {
        if (Main.isDropItems()) {
            e.getBlock().breakNaturally();
        }
        e.getBlock().setType(Material.SIGN_POST);
        BlockState block = e.getBlock().getState();
        Sign sign = (Sign) block;
        sign.setLine(0, Main.getLine1());
        sign.setLine(1, Main.getLine2());
        sign.setLine(2, Main.getLine3());
        sign.setLine(3, Main.getLine4());
        sign.update();
        Main.getInstance().getLogger().info(CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX() + "").replace("$Y", e.getBlock().getY() + "").replace("$Z", e.getBlock().getZ() + "").replace("$World", e.getBlock().getWorld().getName()));
        if (Main.isNotifyAdmin()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                    p.sendMessage(CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX() + "").replace("$Y", e.getBlock().getY() + "").replace("$Z", e.getBlock().getZ() + "").replace("$World", e.getBlock().getWorld().getName()));
                }
            }
        }
        RedstoneClockController.removeRedstoneByLocation(e.getBlock().getLocation());
    }
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerBreakRedstone(BlockBreakEvent e){
		if(e.getBlock().getType() == Material.REDSTONE_WIRE 
				|| e.getBlock().getType() == Material.DIODE_BLOCK_ON
				|| e.getBlock().getType() == Material.DIODE_BLOCK_OFF
				|| e.getBlock().getType() == Material.REDSTONE_COMPARATOR
				|| e.getBlock().getType() == Material.REDSTONE_COMPARATOR_OFF
				|| e.getBlock().getType() == Material.REDSTONE_COMPARATOR_ON
                || e.getBlock().getType() == Material.OBSERVER){
			if(RedstoneClockController.contains(e.getBlock().getLocation())){
				RedstoneClockController.removeRedstoneByLocation(e.getBlock().getLocation());
			}
		}
		if(e.getBlock().getType() == Material.SIGN || e.getBlock().getType() ==  Material.SIGN_POST){
			BlockState block = e.getBlock().getState();
			Sign sign = (Sign)block;
			if(sign.getLine(0).equalsIgnoreCase(Main.getLine1())
					&& sign.getLine(1).equalsIgnoreCase(Main.getLine2())
					&& sign.getLine(2).equalsIgnoreCase(Main.getLine3())
					&& sign.getLine(3).equalsIgnoreCase(Main.getLine4())){
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onItemDrop(BlockPhysicsEvent e){
		if(e.getBlock().getType() == Material.SIGN || e.getBlock().getType() ==  Material.SIGN_POST){
			BlockState block = e.getBlock().getState();
			Sign sign = (Sign)block;
			if(sign.getLine(0).equalsIgnoreCase(Main.getLine1())
					&& sign.getLine(1).equalsIgnoreCase(Main.getLine2())
					&& sign.getLine(2).equalsIgnoreCase(Main.getLine3())
					&& sign.getLine(3).equalsIgnoreCase(Main.getLine4())
					&& e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR){
				
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
			}
		}
	}


	
}
