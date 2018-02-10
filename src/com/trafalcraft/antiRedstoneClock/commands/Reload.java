package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.error.YAMLException;

public class Reload {
    private static Reload ourInstance = new Reload();

    public static Reload getInstance() {
        return ourInstance;
    }

    private Reload() {
    }

    public void performCMD(CommandSender sender){
        try{
            Main.getPlugin().reloadConfig();
            Main.getIgnoredWorlds().clear();
            Main.getIgnoredRegions().clear();
            Msg.load();
            sender.sendMessage(Msg.Prefix + Msg.reloadSuccess.toString());
        }catch(YAMLException e){
            if(sender instanceof Player){
                sender.sendMessage(Msg.ERROR + "An error as occurred in the config.yml please check the log!");
            }
                Main.getInstance().getLogger().severe("An error as occurred in the config.yml please fix it!");
            e.printStackTrace();
        }
    }
}
