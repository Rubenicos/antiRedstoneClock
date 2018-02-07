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

class Util {
        static void checkAndUpdateRedstoneClockState(Block block) {
                if (!RedstoneClockController.contains(block.getLocation())) {
                        try {
                                RedstoneClockController.addRedstone(block.getLocation());
                        } catch (Exception e1) {
                                e1.printStackTrace();
                        }
                }else{
                        if (!RedstoneClockController.getRedstoneClock(block.getLocation()).isEnd()) {
                                if (RedstoneClockController.getRedstoneClock(block.getLocation()).getClock() >= Main
                                        .getMaximumPulses()) {
                                        removeRedstoneClock(block);
                                }else{
                                        RedstoneClockController.getRedstoneClock(block.getLocation()).addOneToClock();
                                }
                        }
                }
        }

        static boolean checkIgnoreWorldsAndRegions(Block block) {
                for(String ignoreWorld: Main.getIgnoredWorlds()){
                        if (block.getWorld().getName().equals(ignoreWorld)) {
                                return true;
                        }
                }
                return WorldGuardLink.checkAllowedRegion(block.getLocation());
        }

        static synchronized void removeRedstoneClock(Block block) {
                if (Main.automaticallyDropDetectedItem()) {
                        if (Main.isDropItems()) {
                                block.breakNaturally();
                        } else {
                                block.setType(Material.AIR);
                        }
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                                block.setType(Material.SIGN_POST);
                                BlockState blockState = block.getState();
                                Sign sign = (Sign) blockState;
                                sign.setLine(0, Main.getLine1());
                                sign.setLine(1, Main.getLine2());
                                sign.setLine(2, Main.getLine3());
                                sign.setLine(3, Main.getLine4());
                                sign.update(false, false);
                                Bukkit.getLogger()
                                        .info(CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString()
                                                .replace("$X", block.getX() + "")
                                                .replace("$Y", block.getY() + "").replace("$Z", block.getZ() + "")
                                                .replace("$World", block.getWorld().getName()));
                                if (Main.isNotifyAdmin()) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                                if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                                                        p.sendMessage(
                                                                CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString()
                                                                        .replace("$X", block.getX() + "")
                                                                        .replace("$Y", block.getY() + "")
                                                                        .replace("$Z", block.getZ() + "")
                                                                        .replace("$World", block.getWorld().getName()));
                                                }
                                        }
                                }
                                RedstoneClockController.removeRedstoneByLocation(block.getLocation());
                        }, 1L);
                } else {
                        RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(block.getLocation());
                        if (!redstoneClock.getDetected()) {
                                redstoneClock.setDetected(true);
                                Bukkit.getLogger()
                                        .info(CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString()
                                                .replace("$X", block.getX() + "")
                                                .replace("$Y", block.getY() + "").replace("$Z", block.getZ() + "")
                                                .replace("$World", block.getWorld().getName()));
                                if (Main.isNotifyAdmin()) {
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                                if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                                                        p.sendMessage(
                                                                CustomConfig.Prefix + CustomConfig.MsgToAdmin.toString()
                                                                        .replace("$X", block.getX() + "")
                                                                        .replace("$Y", block.getY() + "")
                                                                        .replace("$Z", block.getZ() + "")
                                                                        .replace("$World", block.getWorld().getName()));
                                                }
                                        }
                                }

                        }
                }
        }
}