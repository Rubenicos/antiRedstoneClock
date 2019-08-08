package com.trafalcraft.antiRedstoneClock.listener;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import com.trafalcraft.antiRedstoneClock.util.WorldGuard.WorldGuardHook;
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
        } else {
            RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(block.getLocation());
            if (!redstoneClock.isTimedOut()) {
                if (redstoneClock.getNumberOfClock()
                        >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                    removeRedstoneClock(block);
                } else {
                    redstoneClock.addOneToClock();
                }
            } else {
                RedstoneClockController.removeRedstoneByObject(redstoneClock);
            }
        }
    }

    static boolean checkIgnoreWorldsAndRegions(Block block) {
        for (String ignoreWorld : Main.getIgnoredWorlds()) {
            if (block.getWorld().getName().equals(ignoreWorld)) {
                return true;
            }
        }
        return WorldGuardHook.checkAllowedRegion(block.getLocation());
    }

    static void removeRedstoneClock(Block block) {
        RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(block.getLocation());
        if (Main.getInstance().getConfig().getBoolean("AutomaticallyBreakDetectedClock")) {
            if (Main.getInstance().getConfig().getBoolean("DropItems")) {
                block.breakNaturally();
            } else {
                block.setType(Material.AIR);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                if (Main.getInstance().getConfig().getBoolean("CreateSignWhenClockIsBreak")) {
                    Sign sign;
                    if (Material.getMaterial("OAK_SIGN") != null) {
                        block.setType(Material.getMaterial("OAK_SIGN"), false);
                    } else if (Material.getMaterial("SIGN") != null) {
                        block.setType(Material.getMaterial("SIGN"), false);
                    } else if (Material.getMaterial("SIGN_POST") != null) {
                        block.setType(Material.getMaterial("SIGN_POST"), false);
                    }
                    BlockState blockState = block.getState();
                    sign = (Sign) blockState;
                    sign.setLine(0, Main.getInstance().getConfig().getString("Sign.Line1")
                            .replace("&", "§"));
                    sign.setLine(1, Main.getInstance().getConfig().getString("Sign.Line2")
                            .replace("&", "§"));
                    sign.setLine(2, Main.getInstance().getConfig().getString("Sign.Line3")
                            .replace("&", "§"));
                    sign.setLine(3, Main.getInstance().getConfig().getString("Sign.Line4")
                            .replace("&", "§"));
                    sign.update(false, false);
                } else {
                    block.setType(Material.AIR);
                }
                RedstoneClockController.removeRedstoneByLocation(block.getLocation());
            }, 1L);
        }
        if (!redstoneClock.getDetected()) {
            redstoneClock.setDetected(true);
            Bukkit.getLogger()
                    .info(Msg.PREFIX + Msg.MSG_TO_ADMIN.toString()
                            .replace("$X", block.getX() + "")
                            .replace("$Y", block.getY() + "")
                            .replace("$Z", block.getZ() + "")
                            .replace("$World", block.getWorld().getName()));
            if (Main.getInstance().getConfig().getBoolean("NotifyAdmins")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                        p.sendMessage(
                                Msg.PREFIX + Msg.MSG_TO_ADMIN.toString()
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