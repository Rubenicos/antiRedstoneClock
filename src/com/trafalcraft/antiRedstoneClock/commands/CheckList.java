package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;
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
            sender.sendMessage(CustomConfig.RedStoneClockListHeader.toString().replace("$page", "("+test/5+"/"+((RedstoneClockController.getAllLoc().size()/5)+1)+")"));
            for(Location loc : RedstoneClockController.getAllLoc()){
                if(!(i+1 > test+1) && !(i+1 < test-4)){
                    if(RedstoneClockController.getRedstoneClock(loc).getClock() > Main.getMaximumPulses()*0.750){
                        sender.sendMessage("§4RedStoneClock> §fWorld:"+loc.getWorld().getName()+",X:"+loc.getX()+",Y:"+loc.getY()+",Z:"+loc.getZ()+" b:"+RedstoneClockController.getRedstoneClock(loc).getClock()+"/"+Main.getMaximumPulses());
                    }else if (RedstoneClockController.getRedstoneClock(loc).getClock() > Main.getMaximumPulses()*0.5){
                        sender.sendMessage("§eRedStoneClock> §fWorld:"+loc.getWorld().getName()+",X:"+loc.getX()+",Y:"+loc.getY()+",Z:"+loc.getZ()+" b:"+RedstoneClockController.getRedstoneClock(loc).getClock()+"/"+Main.getMaximumPulses());
                    }else if (RedstoneClockController.getRedstoneClock(loc).getClock() > Main.getMaximumPulses()*0.250){
                        sender.sendMessage("§aRedStoneClock> §fWorld:"+loc.getWorld().getName()+",X:"+loc.getX()+",Y:"+loc.getY()+",Z:"+loc.getZ()+" b:"+RedstoneClockController.getRedstoneClock(loc).getClock()+"/"+Main.getMaximumPulses());
                    }else{
                        sender.sendMessage("§2RedStoneClock> §fWorld:"+loc.getWorld().getName()+",X:"+loc.getX()+",Y:"+loc.getY()+",Z:"+loc.getZ()+" b:"+RedstoneClockController.getRedstoneClock(loc).getClock()+"/"+Main.getMaximumPulses());
                    }
                }
                i++;
            }
            sender.sendMessage(CustomConfig.RedStoneClockListFooter.toString());
        }catch(NumberFormatException e){
            sender.sendMessage(CustomConfig.Command_Use.toString().replace("$command", "checkList <number>"));
        }
    }
}
