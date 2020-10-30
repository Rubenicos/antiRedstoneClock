package com.trafalcraft.antiRedstoneClock.listener;

import java.util.logging.Level;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.exception.DuplicateRedstoneClockObjectException;
import com.trafalcraft.antiRedstoneClock.util.plotSquared.VersionPlotSquared;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import com.trafalcraft.antiRedstoneClock.util.worldGuard.VersionWG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

class Util {
    private Util() {}

    static void checkAndUpdateRedstoneClockState(Block block) {
        RedstoneClock redstoneClock = RedstoneClockController.getRedstoneClock(block.getLocation());
        if (redstoneClock == null) {
            try {
                RedstoneClockController.addRedstone(block.getLocation());
            } catch (DuplicateRedstoneClockObjectException e1) {
                Main.getInstance().getLogger().log(Level.SEVERE, "[antiRedstoneClock]", e1);
            }
        } else {
            if (!redstoneClock.isTimedOut()) {
                if (redstoneClock.getNumberOfClock()
                        >= Main.getInstance().getConfig().getInt("MaxPulses")) {
                    removeRedstoneClock(redstoneClock, block);
                } else {
                    redstoneClock.addOneToClock();
                }
            } else {
                RedstoneClockController.removeRedstoneByObject(redstoneClock);
            }
        }
    }

    static boolean checkIgnoreWorldsAndRegions(Block block) {
        if (Main.getIgnoredWorlds().contains(block.getWorld().getName())) {
            return true;
        }
        if (VersionWG.getInstance().getWG() != null && VersionWG.getInstance().getWG()
                .isAllowedRegion(block.getLocation())) {
            return true;
        } else return VersionPlotSquared.getInstance().getPlotSquared() != null
                && VersionPlotSquared.getInstance().getPlotSquared().isAllowedPlot(block.getLocation());
    }

    static void removeRedstoneClock(RedstoneClock redstoneClock, Block block) {
        if (Main.getInstance().getConfig().getBoolean("AutomaticallyBreakDetectedClock")) {
            removeRedstoneBlock(block);
        }
        if (!redstoneClock.getDetected()) {
            redstoneClock.setDetected(true);
            Bukkit.getLogger().info(getFormatedStringForMsgToAdmin(block));
            if (Main.getInstance().getConfig().getBoolean("NotifyAdmins")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                        p.sendMessage(getFormatedStringForMsgToAdmin(block));
                    }
                }
            }

        }
    }

    private static void removeRedstoneBlock(Block block) {
        if (Main.getInstance().getConfig().getBoolean("DropItems")) {
            block.breakNaturally();
        } else {
            block.setType(Material.AIR);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            if (Main.getInstance().getConfig().getBoolean("CreateSignWhenClockIsBreak")) {
                placeSign(block);
            } else {
                block.setType(Material.AIR);
            }
            RedstoneClockController.removeRedstoneByLocation(block.getLocation());
        }, 1L);
    }

    private static void placeSign(Block block) {
        Sign sign;
        if (Material.getMaterial("OAK_SIGN") != null) {
            block.setType(Material.getMaterial("OAK_SIGN"), false);
        } else if (Material.getMaterial("SIGN_POST") != null) {
            block.setType(Material.getMaterial("SIGN_POST"), false);
        } else if (Material.getMaterial("SIGN") != null) {
            block.setType(Material.getMaterial("SIGN"), false);
        } else {
            Bukkit.getLogger().warning(Msg.PREFIX + "No valid sign found for this minecraft version!!!"
                +"\nplease disable CreateSignWhenClockIsBreak in config file");
        }
        BlockState blockState = block.getState();
        try {
            sign = (Sign) blockState;
            sign.setLine(0, Main.getInstance().getConfig().getString("Sign.Line1").replace("&", "§"));
            sign.setLine(1, Main.getInstance().getConfig().getString("Sign.Line2").replace("&", "§"));
            sign.setLine(2, Main.getInstance().getConfig().getString("Sign.Line3").replace("&", "§"));
            sign.setLine(3, Main.getInstance().getConfig().getString("Sign.Line4").replace("&", "§"));
            sign.update(false, false);
        } catch (ClassCastException error) {
            Bukkit.getLogger().warning(Msg.PREFIX + "No valid sign found for this minecraft version!!!"
                +"\nplease disable CreateSignWhenClockIsBreak in config file" 
                +"\nMore infos: " + block.getType());
        }
        block.getDrops().clear();
    }

    private static String getFormatedStringForMsgToAdmin(Block block) {
        return Msg.PREFIX + Msg.MSG_TO_ADMIN.toString()
                .replace("$X", block.getX() + "")
                .replace("$Y", block.getY() + "")
                .replace("$Z", block.getZ() + "")
                .replace("$World", block.getWorld().getName());
    }
}
