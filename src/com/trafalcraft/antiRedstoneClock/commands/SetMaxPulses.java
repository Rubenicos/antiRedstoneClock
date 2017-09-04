package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;
import org.bukkit.command.CommandSender;

public class SetMaxPulses {
    private static SetMaxPulses ourInstance = new SetMaxPulses();

    public static SetMaxPulses getInstance() {
        return ourInstance;
    }

    private SetMaxPulses() {
    }

    public void performCMD(CommandSender sender, String... args){
        try{
            Main.setMaximumPulses(Integer.parseInt(args[1]));
            Main.getInstance().getConfig().set("MaxPulses", Main.getMaximumPulses());
            Main.getInstance().saveConfig();
            sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"MaxPulses\"").replace("$value", args[1]));
        }catch(NumberFormatException e){
            sender.sendMessage(CustomConfig.Command_Use.toString().replace("$command", "setMaxPulses <number>"));
        }
    }
}
