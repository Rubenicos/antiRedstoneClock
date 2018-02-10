package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class CheckList {
    private static CheckList ourInstance = new CheckList();

    public static CheckList getInstance() {
        return ourInstance;
    }

    private CheckList() {
    }

    public void performCMD(CommandSender sender, String... args){
        try{
            int test = 5;
            if(args.length > 1){
                test = Integer.parseInt(args[1]) * 5;
            }
            int i = 0;
            sender.sendMessage(Msg.RedStoneClockListHeader.toString().replace("$page",
                    "(" + test / 5 + "/" + ((RedstoneClockController.getAllLoc().size() / 5) + 1) + ")"));
            for(Location loc : RedstoneClockController.getAllLoc()){
                if(!(i+1 > test+1) && !(i+1 < test-4)){
                    int maxPulses = Main.getInstance().getConfig().getInt("MaxPulses");
                    int clock = RedstoneClockController.getRedstoneClock(loc).getNumberOfClock();
                    if (clock > maxPulses * 0.75) {
                        sender.sendMessage(
                                "§4RedStoneClock> §fWorld:" + loc.getWorld().getName() + ",X:" + loc.getX() + ",Y:"
                                        + loc.getY() + ",Z:" + loc.getZ() + " b:" + clock
                                        + "/" + maxPulses);
                    } else if (clock > maxPulses * 0.5) {
                        sender.sendMessage(
                                "§eRedStoneClock> §fWorld:" + loc.getWorld().getName() + ",X:" + loc.getX() + ",Y:"
                                        + loc.getY() + ",Z:" + loc.getZ() + " b:" + clock
                                        + "/" + maxPulses);
                    } else if (clock > maxPulses * 0.250) {
                        sender.sendMessage(
                                "§aRedStoneClock> §fWorld:" + loc.getWorld().getName() + ",X:" + loc.getX() + ",Y:"
                                        + loc.getY() + ",Z:" + loc.getZ() + " b:" + clock
                                        + "/" + maxPulses);
                    }else{
                        sender.sendMessage(
                                "§2RedStoneClock> §fWorld:" + loc.getWorld().getName() + ",X:" + loc.getX() + ",Y:"
                                        + loc.getY() + ",Z:" + loc.getZ() + " b:" + clock
                                        + "/" + maxPulses);
                    }
                }
                i++;
            }
            sender.sendMessage(Msg.RedStoneClockListFooter.toString());
        }catch(NumberFormatException e){
            sender.sendMessage(Msg.Command_Use.toString().replace("$command", "checkList <number>"));
        }
    }
}
