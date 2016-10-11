package com.trafalcraft.antiRedstoneClock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
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
			//Player p2 = Bukkit.getPlayer("Amosar");
			//p2.sendMessage(e.getOldCurrent()+"");
			if(e.getOldCurrent() == 0){
				if(!RedstoneClockController.contains(e.getBlock().getLocation())){
					try {
						RedstoneClockController.addRedstone(e.getBlock().getLocation());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}else{
					if(!RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).isEnd()){
						if(RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).getBoucle() >= Main.getMaxImpulsions()){
							if(Main.isDropItems()){

								e.getBlock().breakNaturally();
							}
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
							if(Main.isNotifyAdmin()){
								for(Player p : Bukkit.getOnlinePlayers()){
									if(p.isOp() || p.hasPermission("antiRedstoneClock.notifyAdmin")){
										p.sendMessage(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", e.getBlock().getX()+"").replace("$Y", e.getBlock().getY()+"").replace("$Z", e.getBlock().getZ()+"").replace("$World", e.getBlock().getWorld().getName()));
									}
								}
							}
							RedstoneClockController.removeRedstoneByLocation(e.getBlock().getLocation());
						}else{
							System.out.println(RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).getBoucle());
							RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).addBoucle();
						}
					}
				}

				
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
				if(!RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).isEnd()){
					if(RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).getBoucle() >= Main.getMaxImpulsions()){
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
										if(p.isOp() || p.hasPermission("antiRedstoneClock.notifyAdmin")){
											p.sendMessage(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", b.getX()+"").replace("$Y", b.getY()+"").replace("$Z", b.getZ()+"").replace("$World", b.getWorld().getName()));
										}
									}
								}
								RedstoneClockController.removeRedstoneByLocation(b.getLocation());
							}
						}, 1L);
					}else{
						RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).addBoucle();
					}
				}
			}

		
		}
	}
	
/*	@EventHandler (priority = EventPriority.LOWEST)
	public void onRedstoneClock2(BlockPhysicsEvent e){
		System.out.println(e.getBlock().getType());
		if(e.getBlock().getType() == Material.REDSTONE_COMPARATOR_OFF ){
			if(!RedstoneClockController.contains(e.getBlock().getLocation())){
				try {
					RedstoneClockController.addRedstone(e.getBlock().getLocation());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}else{
				if(!RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).isEnd()){
					if(e.getBlock().getBlockPower() == 15){
						if(RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).getlastStatus()){
							RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).updateStatus();
						}else{
							return;
						}
					}else if(e.getBlock().getBlockPower() == 0){
						if(!RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).getlastStatus()){
							RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).updateStatus();
						}else{
							return;
						}
					}else{
						return;
					}
					if(RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).getBoucle() >= Main.getMaxImpulsions()){
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
										if(p.isOp() || p.hasPermission("antiRedstoneClock.notifyAdmin")){
											p.sendMessage(CustomConfig.Prefix+CustomConfig.MsgToAdmin.toString().replace("$X", b.getX()+"").replace("$Y", b.getY()+"").replace("$Z", b.getZ()+"").replace("$World", b.getWorld().getName()));
										}
									}
								}
								RedstoneClockController.removeRedstoneByLocation(b.getLocation());
							}
						}, 1L);
					}else{
						RedstoneClockController.getRedstoneClock(e.getBlock().getLocation()).addBoucle();
					}
				}
			}

		
		}
	}*/
	
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerBreakRedstone(BlockBreakEvent e){
		if(e.getBlock().getType() == Material.REDSTONE_WIRE 
				|| e.getBlock().getType() == Material.DIODE_BLOCK_ON
				|| e.getBlock().getType() == Material.DIODE_BLOCK_OFF
				|| e.getBlock().getType() == Material.REDSTONE_COMPARATOR
				|| e.getBlock().getType() == Material.REDSTONE_COMPARATOR_OFF
				|| e.getBlock().getType() == Material.REDSTONE_COMPARATOR_ON){
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
