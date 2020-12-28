package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.CheckTPS;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.error.YAMLException;

public class Reload {
    private static final Reload ourInstance = new Reload();

    public static Reload getInstance() {
        return ourInstance;
    }

    private Reload() {
    }

    public void performCMD(CommandSender sender) {
        try {
            Main.getInstance().reloadConfig();
            Main.getIgnoredWorlds().clear();
            Main.getIgnoredRegions().clear();
            CheckTPS.initCheckTPS(Main.getInstance().getConfig().getInt("checkTPS.minimumTPS")
                ,Main.getInstance().getConfig().getInt("checkTPS.maximumTPS")
                ,Main.getInstance().getConfig().getInt("checkTPS.intervalInSecond"));
            Msg.load();
            sender.sendMessage(Msg.PREFIX + Msg.RELOAD_SUCCESS.toString());
        } catch (YAMLException e) {
            if (sender instanceof Player) {
                sender.sendMessage(Msg.ERROR + "An error as occurred in the config.yml please check the log!");
            }
            Main.getInstance().getLogger().severe("An error as occurred in the config.yml please fix it!\n" + e);
        }
    }
}
