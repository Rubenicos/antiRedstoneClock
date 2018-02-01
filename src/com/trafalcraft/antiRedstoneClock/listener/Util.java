package com.trafalcraft.antiRedstoneClock.listener;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;
import com.trafalcraft.antiRedstoneClock.util.WorldGuardLink;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;

class Util {
        static void checkAndUpdateRedstoneClockState(BlockEvent e) {
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

        static boolean checkIgnoreWorldsAndRegions(BlockEvent e) {
                for(String ignoreWorld: Main.getIgnoredWorlds()){
                        if(e.getBlock().getWorld().getName().equals(ignoreWorld)){
                                return true;
                        }
                }
                return WorldGuardLink.checkAllowedRegion(e.getBlock().getLocation());
        }

        static void removeRedstoneClock(BlockEvent e) {
                Block b = e.getBlock();
                if (Main.automaticallyDropDetectedItem()) {
                        if (Main.isDropItems()) {
                                e.getBlock().breakNaturally();
                        } else {
                                e.getBlock().setType(Material.AIR);
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                                b.setType(Material.SIGN_POST);
                                BlockState block = b.getState();
                                Sign sign = (Sign) block;
                                sign.setLine(0, Main.getLine1());
                                sign.setLine(1, Main.getLine2());
                                sign.setLine(2, Main.getLine3());
                                sign.setLine(3, Main.getLine4());
                                sign.update();
                                Bukkit.getLogger()
                                        .info(CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString()
                                                .replace("$X", b.getX() + "")
                                                .replace("$Y", b.getY() + "").replace("$Z", b.getZ() + "")
                                                .replace("$World", b.getWorld().getName()));
                                if (Main.isNotifyAdmin()) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                                if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                                                        p.sendMessage(
                                                                CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString()
                                                                        .replace("$X", b.getX() + "")
                                                                        .replace("$Y", b.getY() + "")
                                                                        .replace("$Z", b.getZ() + "")
                                                                        .replace("$World", b.getWorld().getName()));
                                                }
                                        }
                                }
                                RedstoneClockController.removeRedstoneByLocation(b.getLocation());
                        }, 1L);
                } else {
                        RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(b.getLocation());
                        if (!redstoneClock.getDetected()) {
                                redstoneClock.setDetected(true);
                                Bukkit.getLogger()
                                        .info(CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString()
                                                .replace("$X", b.getX() + "")
                                                .replace("$Y", b.getY() + "").replace("$Z", b.getZ() + "")
                                                .replace("$World", b.getWorld().getName()));
                                if (Main.isNotifyAdmin()) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                                if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                                                        p.sendMessage(
                                                                CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString()
                                                                        .replace("$X", b.getX() + "")
                                                                        .replace("$Y", b.getY() + "")
                                                                        .replace("$Z", b.getZ() + "")
                                                                        .replace("$World", b.getWorld().getName()));
                                                }
                                        }
                                }

                        }
                }
        }
}