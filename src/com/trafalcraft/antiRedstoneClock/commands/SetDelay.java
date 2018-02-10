package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.Msg;
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
            Main.getInstance().getConfig().set("Delay", Integer.parseInt(args[1]));
            Main.getInstance().saveConfig();
            sender.sendMessage(
                    Msg.Prefix + Msg.newValueInConfig.toString().replace("$setting", "\"Delay\"")
                            .replace("$value", args[1]));
        }catch(NumberFormatException e){
            sender.sendMessage(Msg.Command_Use.toString().replace("$command", "SetDelay <number>"));
        }
    }
}
