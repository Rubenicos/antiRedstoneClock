package com.trafalcraft.antiRedstoneClock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.trafalcraft.antiRedstoneClock.util.CustomConfig;
import com.trafalcraft.antiRedstoneClock.util.WorldGuardLink;

public class PlayerListener implements Listener {
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onRedstoneClock(BlockRedstoneEvent e){
		for(String ignoreWorld:Main.getAllowedWorlds()){
			if(e.getBlock().getWorld().getName().equals(ignoreWorld)){
				return;
			}
		}
		if(WorldGuardLink.checkAllowedRegion(e.getBlock().getLocation())){
			return;
		}
		if(e.getBlock().getType() == Material.REDSTONE_WIRE){
			if(e.getOldCurrent() == 0){
				if(!Main.getRDC().contains(e.getBlock().getLocation())){
					try {
						Main.getRDC().addRedstone(e.getBlock().getLocation());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}else{
					if(Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).getMinutes() <= Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).getEndTimerInMinutes()){
						if(Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).getBoucle() >= Main.getMaxImpulsions()){
							e.getBlock().setType(Material.SIGN_POST);
							BlockState block = e.getBlock().getState();
							Sign sign = (Sign)block;
							sign.setLine(0, Main.getLine1());
							sign.setLine(1, Main.getLine2());
							sign.setLine(2, Main.getLine3());
							sign.setLine(3, Main.getLine4());
							sign.update();
							Main.getInstance().getLogger().info(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX()+"").replace("$Y", e.getBlock().getY()+"").replace("$Z", e.getBlock().getZ()+"").replace("$World", e.getBlock().getWorld().getName()));
							//Main.getInstance().getLogger().info("Boucle de redstone désactivé au coordoonée x:"+e.getBlock().getX()+" y:"+e.getBlock().getY()+" z:"+e.getBlock().getZ()+". Dans le monde:"+e.getBlock().getWorld().getName());
							if(Main.getNotifyAdmin()){
								for(Player p : Bukkit.getOnlinePlayers()){
									if(p.isOp() || p.hasPermission("antiRedstoneClock.notifyAdmin")){
										p.sendMessage(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX()+"").replace("$Y", e.getBlock().getY()+"").replace("$Z", e.getBlock().getZ()+"").replace("$World", e.getBlock().getWorld().getName()));
									}
								}
							}
							Main.getRDC().removeRedstoneByLocation(e.getBlock().getLocation());
						}else{
							Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).addBoucle();
						}
					}else{
						if(Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).getBoucle() >= Main.getMaxImpulsions()){
							e.getBlock().setType(Material.SIGN_POST);
							BlockState block = e.getBlock().getState();
							Sign sign = (Sign)block;
							sign.setLine(0, Main.getLine1());
							sign.setLine(1, Main.getLine2());
							sign.setLine(2, Main.getLine3());
							sign.setLine(3, Main.getLine4());
							sign.update();
							Bukkit.getLogger().info(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX()+"").replace("$Y", e.getBlock().getY()+"").replace("$Z", e.getBlock().getZ()+"").replace("$World", e.getBlock().getWorld().getName()));
							if(Main.getNotifyAdmin()){
								for(Player p : Bukkit.getOnlinePlayers()){
									if(p.isOp() || p.hasPermission("antiRedstoneClock.notifyAdmin")){
										p.sendMessage(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX()+"").replace("$Y", e.getBlock().getY()+"").replace("$Z", e.getBlock().getZ()+"").replace("$World", e.getBlock().getWorld().getName()));
									}
								}
							}
						}
						Main.getRDC().removeRedstoneByLocation(e.getBlock().getLocation());
					}
				}

			
			}
		}else if(e.getBlock().getType() == Material.DIODE_BLOCK_ON){

			if(!Main.getRDC().contains(e.getBlock().getLocation())){
				try {
					Main.getRDC().addRedstone(e.getBlock().getLocation());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}else{
				if(Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).getMinutes() <= Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).getEndTimerInMinutes()){
					if(Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).getBoucle() >= Main.getMaxImpulsions()){
						final Location b1 = e.getBlock().getLocation();
						b1.setY(e.getBlock().getY()-1);
						b1.getBlock().setType(Material.AIR);
						final Block b2 = e.getBlock();
						b2.getDrops().clear();
						b2.setType(Material.AIR);
						b2.getDrops().clear();
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								b1.getBlock().setType(Material.DIRT);
								b2.setType(Material.SIGN_POST);
								BlockState block = b2.getState();
								Sign sign = (Sign)block;
								sign.setLine(0, Main.getLine1());
								sign.setLine(1, Main.getLine2());
								sign.setLine(2, Main.getLine3());
								sign.setLine(3, Main.getLine4());
								sign.update();
								Bukkit.getLogger().info(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", b2.getX()+"").replace("$Y", b2.getY()+"").replace("$Z", b2.getZ()+"").replace("$World", b2.getWorld().getName()));
								if(Main.getNotifyAdmin()){
									for(Player p : Bukkit.getOnlinePlayers()){
										if(p.isOp() || p.hasPermission("antiRedstoneClock.notifyAdmin")){
											p.sendMessage(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", b2.getX()+"").replace("$Y", b2.getY()+"").replace("$Z", b2.getZ()+"").replace("$World", b2.getWorld().getName()));
										}
									}
								}
								Main.getRDC().removeRedstoneByLocation(b2.getLocation());
							}
						}, 5L);
					}else{
						Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).addBoucle();
					}
				}else{
					if(Main.getRDC().getRedstoneClock(e.getBlock().getLocation()).getBoucle() >= Main.getMaxImpulsions()){
						e.getBlock().setType(Material.SIGN_POST);
						BlockState block = e.getBlock().getState();
						Sign sign = (Sign)block;
						sign.setLine(0, Main.getLine1());
						sign.setLine(1, Main.getLine2());
						sign.setLine(2, Main.getLine3());
						sign.setLine(3, Main.getLine4());
						sign.update();
						Bukkit.getLogger().info(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX()+"").replace("$Y", e.getBlock().getY()+"").replace("$Z", e.getBlock().getZ()+"").replace("$World", e.getBlock().getWorld().getName()));
						if(Main.getNotifyAdmin()){
							for(Player p : Bukkit.getOnlinePlayers()){
								if(p.isOp() || p.hasPermission("antiRedstoneClock.notifyAdmin")){
									p.sendMessage(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX()+"").replace("$Y", e.getBlock().getY()+"").replace("$Z", e.getBlock().getZ()+"").replace("$World", e.getBlock().getWorld().getName()));
								}
							}
						}
					}
				}
			}

		
		
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerBreakRedstone(BlockBreakEvent e){
		if(e.getBlock().getType() == Material.REDSTONE_WIRE || e.getBlock().getType() == Material.DIODE_BLOCK_ON){
			if(Main.getRDC().contains(e.getBlock().getLocation())){
				Main.getRDC().removeRedstoneByLocation(e.getBlock().getLocation());
			}
		}
	}


	
}
