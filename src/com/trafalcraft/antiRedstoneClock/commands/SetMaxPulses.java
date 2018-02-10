package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.Msg;
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
            Main.getInstance().getConfig().set("MaxPulses", Integer.parseInt(args[1]));
            Main.getInstance().saveConfig();
            sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString().replace("$setting", "\"MaxPulses\"")
                    .replace("$value", args[1]));
        }catch(NumberFormatException e){
            sender.sendMessage(Msg.Command_Use.toString().replace("$command", "setMaxPulses <number>"));
        }
    }
}
