package com.trafalcraft.anti_redstone_clock.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.trafalcraft.anti_redstone_clock.Main;
import com.trafalcraft.anti_redstone_clock.util.Msg;

public class NotifyAdmin {
    private static final NotifyAdmin ourInstance = new NotifyAdmin();

    public static NotifyAdmin getInstance() {
        return ourInstance;
    }

    private NotifyAdmin() {
    }

    public void performCMD(CommandSender sender, String... args) {
        FileConfiguration config = Main.getInstance().getConfig();
        if (args.length == 1) {
            changeValueAndSendMessage(sender, config, !config.getBoolean("NotifyAdmins"));
        } else {
            if (args[1].equalsIgnoreCase("true")) {
                changeValueAndSendMessage(sender, config, true);
            } else if (!args[1].equalsIgnoreCase("false")) {
                sender.sendMessage(Msg.COMMAND_USE.toString()
                        .replace("$command", "NotifyAdmins <true/false>"));
            }
        }
    }

    private void changeValueAndSendMessage(CommandSender sender, FileConfiguration config, boolean newValue) {
        config.set("NotifyAdmins", newValue);
        Main.getInstance().saveConfig();
        sender.sendMessage(Msg.PREFIX + Msg.NEW_VALUE_IN_CONFIG.toString()
                .replace("$setting", "\"NotifyAdmins\"")
                .replace("$value", String.valueOf(newValue)));
    }
}
