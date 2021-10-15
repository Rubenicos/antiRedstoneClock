package com.trafalcraft.anti_redstone_clock.commands;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.trafalcraft.anti_redstone_clock.Main;
import com.trafalcraft.anti_redstone_clock.object.RedstoneClockController;
import com.trafalcraft.anti_redstone_clock.util.CheckTPS;
import com.trafalcraft.anti_redstone_clock.util.Msg;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CheckList {
    private static final CheckList ourInstance = new CheckList();

    public static CheckList getInstance() {
        return ourInstance;
    }

    private CheckList() {
    }

    public void performCMD(CommandSender sender, String... args) {
        try {
            int page = 1;
            if (args.length > 1) {
                page = Integer.parseInt(args[1]);
                if (page == -1) {
                    sender.sendMessage("§2TPS is " + CheckTPS.isTpsOK() + " §4TPS:" + CheckTPS.getTPS() 
                        + " §emin" + Main.getInstance().getConfig().getInt("checkTPS.minimumTPS")
                        + " §amax" + Main.getInstance().getConfig().getInt("checkTPS.maximumTPS"));
                    return;
                }
            }
            Collection<Location> allLocation = RedstoneClockController.getAllLoc();
            int totalPage = (int) Math.ceil(allLocation.size() / 5.0);
            sender.sendMessage(Msg.RED_STONE_CLOCK_LIST_HEADER.toString().replace("$page",
                    "(" + page + "/" + totalPage + ")"));
            int i = 1;
            int minElements = 5 * (page - 1);
            int maxElements = 5 * page;
            String teleportCMD = Main.getInstance().getConfig().getString("teleportCMD", "tp $x $y $z");
            int maxPulses = Main.getInstance().getConfig().getInt("MaxPulses");
            for (Location loc : allLocation) {
                if (i > minElements && i <= maxElements) {
                    int clock = RedstoneClockController.getRedstoneClock(loc).getNumberOfClock();
                    String color = "§2";    //Dark_Green
                    if (clock > maxPulses * 0.75) {
                        color = "§4";       //Dark_Red
                    } else if (clock > maxPulses * 0.5) {
                        color = "§e";       //yellow
                    } else if (clock > maxPulses * 0.250) {
                        color = "§a";       // green
                    }
                    TextComponent textComponent = new TextComponent(color + "RedStoneClock> §fWorld:" + loc.getWorld().getName()
                            + ",X:" + loc.getX()
                            + ",Y:" + loc.getY()
                            + ",Z:" + loc.getZ()
                            + " b:" + clock + "/" + maxPulses);
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + teleportCMD
                        .replace("$x", String.format("%.0f", loc.getX()))
                        .replace("$y", String.format("%.0f",  loc.getY()))
                        .replace("$z", String.format("%.0f",  loc.getZ()))
                        .replace("$world", loc.getWorld().getName())
                        .replace("$player", sender.getName())));
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new Text("Click to teleport you to the redstoneclock")));
                    sendFormatedMessageToPlayer(sender, textComponent);
                }
                i++; 
            }
            sender.sendMessage(Msg.RED_STONE_CLOCK_LIST_FOOTER.toString());
        } catch (NumberFormatException e) {
            sender.sendMessage(Msg.COMMAND_USE.toString().replace("$command", "checkList <number>"));
        }
    }

    private void sendFormatedMessageToPlayer(CommandSender sender, TextComponent textComponent) {
        try {
            sender.getClass().getDeclaredMethod("spigot");
            sender.spigot().sendMessage(textComponent);
        } catch (NoSuchMethodException e) {
            sender.sendMessage(textComponent.getText());
        }
    }
}
