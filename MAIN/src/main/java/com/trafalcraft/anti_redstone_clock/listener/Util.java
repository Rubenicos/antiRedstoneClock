package com.trafalcraft.anti_redstone_clock.listener;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.trafalcraft.anti_redstone_clock.Main;
import com.trafalcraft.anti_redstone_clock.exception.DuplicateRedstoneClockObjectException;
import com.trafalcraft.anti_redstone_clock.object.RedstoneClock;
import com.trafalcraft.anti_redstone_clock.object.RedstoneClockController;
import com.trafalcraft.anti_redstone_clock.util.Msg;
import com.trafalcraft.anti_redstone_clock.util.plotSquared.VersionPlotSquared;
import com.trafalcraft.anti_redstone_clock.util.worldGuard.VersionWG;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

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
        if (block.getY() > Main.getInstance().getConfig().getInt("disableRedstoneClockCheckAbove")
            || Main.getIgnoredWorlds().contains(block.getWorld().getName())) {
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
            Bukkit.getLogger().info(getFormatedStringForMsgToAdmin(block).getText());
            if (Main.getInstance().getConfig().getBoolean("NotifyAdmins")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.isOp() || p.hasPermission("antiRedstoneClock.NotifyAdmin")) {
                        TextComponent textComponent = getFormatedStringForMsgToAdmin(block);
                        String teleportCMD = Main.getInstance().getConfig().getString("teleportCMD", "tp $x $y $z");
                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + teleportCMD
                            .replace("$x", Integer.toString(block.getX()))
                            .replace("$y", Integer.toString(block.getY()))
                            .replace("$z", Integer.toString(block.getZ()))
                            .replace("$world", block.getWorld().getName())
                            .replace("$player", p.getName())));
                        sendFormatedMessageToPlayer(p, textComponent);
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
            if (Main.getInstance().getConfig().getBoolean("SummonLigthningAtRedstoneLocation")) {
                block.getWorld().strikeLightningEffect(block.getLocation());
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

    private static TextComponent getFormatedStringForMsgToAdmin(Block block) {
        TextComponent textComponent = new TextComponent(Msg.PREFIX + Msg.MSG_TO_ADMIN.toString()
            .replace("$X", block.getX() + "")
            .replace("$Y", block.getY() + "")
            .replace("$Z", block.getZ() + "")
            .replace("$World", block.getWorld().getName()));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new Text("Click to teleport you to the redstoneclock")));
        return textComponent;
    }

    private static void sendFormatedMessageToPlayer(Player player, TextComponent textComponent) {
        try {
            player.getClass().getDeclaredMethod("spigot");
            player.spigot().sendMessage(textComponent);
        } catch (NoSuchMethodException e) {
            player.sendMessage(textComponent.getText());
        }
    }
}
