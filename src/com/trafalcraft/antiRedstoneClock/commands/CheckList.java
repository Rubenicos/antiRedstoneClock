package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Collection;

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
            }
            Collection<Location> allLocation = RedstoneClockController.getAllLoc();
            int totalPage = (int) Math.ceil(allLocation.size() / 5.0);
            sender.sendMessage(Msg.RED_STONE_CLOCK_LIST_HEADER.toString().replace("$page",
                    "(" + page + "/" + totalPage + ")"));

            int i = 1;
            int minElements = 5 * (page - 1);
            int maxElements = 5 * page;
            for (Location loc : allLocation) {
                if (i > minElements && i <= maxElements) {
                    int maxPulses = Main.getInstance().getConfig().getInt("MaxPulses");
                    int clock = RedstoneClockController.getRedstoneClock(loc).getNumberOfClock();
                    String color = "§2";    //Dark_Green
                    if (clock > maxPulses * 0.75) {
                        color = "§4";       //Dark_Red
                    } else if (clock > maxPulses * 0.5) {
                        color = "§e";       //yellow
                    } else if (clock > maxPulses * 0.250) {
                        color = "§a";       // green
                    }
                    sender.sendMessage(color + "RedStoneClock> §fWorld:" + loc.getWorld().getName()
                            + ",X:" + loc.getX()
                            + ",Y:" + loc.getY()
                            + ",Z:" + loc.getZ()
                            + " b:" + clock + "/" + maxPulses);
                }
                i++;
            }
            sender.sendMessage(Msg.RED_STONE_CLOCK_LIST_FOOTER.toString());
        } catch (NumberFormatException e) {
            sender.sendMessage(Msg.COMMAND_USE.toString().replace("$command", "checkList <number>"));
        }
    }
}
