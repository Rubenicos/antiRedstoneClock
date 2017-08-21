package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;
import org.bukkit.command.CommandSender;

public class NotifyAdmin {
    private static NotifyAdmin ourInstance = new NotifyAdmin();

    public static NotifyAdmin getInstance() {
        return ourInstance;
    }

    private NotifyAdmin() {
    }

    public void performCMD(CommandSender sender, String... args){
        if(args.length == 1){
            if(Main.getInstance().getConfig().getBoolean("NotifyAdmins")){
                Main.getInstance().getConfig().set("NotifyAdmins", false);
                Main.setNotifyAdmin(false);
                Main.getInstance().saveConfig();
                sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"NotifyAdmins\"").replace("$value", "false"));
            }else{
                Main.getInstance().getConfig().set("NotifyAdmins", true);
                Main.setNotifyAdmin(true);
                Main.getInstance().saveConfig();
                sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"NotifyAdmins\"").replace("$value", "true"));
            }
        }else{
            if(Boolean.parseBoolean(args[1])){
                Main.getInstance().getConfig().set("NotifyAdmins", true);
                Main.setNotifyAdmin(true);
                Main.getInstance().saveConfig();
                sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"NotifyAdmins\"").replace("$value", args[1]));
            }else if(!Boolean.parseBoolean(args[1])){
                Main.getInstance().getConfig().set("NotifyAdmins", false);
                Main.setNotifyAdmin(false);
                Main.getInstance().saveConfig();
                sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"NotifyAdmins\"").replace("$value", args[1]));
            }
        }
    }
}
