package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;
import org.bukkit.command.CommandSender;

public class SetDelay {
    private static SetDelay ourInstance = new SetDelay();

    public static SetDelay getInstance() {
        return ourInstance;
    }

    private SetDelay() {
    }

    public void performCMD(CommandSender sender, String... args){
        try{
            Main.setDelay(Integer.parseInt(args[1]));
            Main.getInstance().getConfig().set("Delay", Main.getDelay());
            Main.getInstance().saveConfig();
            sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"Delay\"").replace("$value", args[1]));
        }catch(NumberFormatException e){
            sender.sendMessage(CustomConfig.Command_Use.toString().replace("$command", "SetDelay <number>"));
        }
    }
}
