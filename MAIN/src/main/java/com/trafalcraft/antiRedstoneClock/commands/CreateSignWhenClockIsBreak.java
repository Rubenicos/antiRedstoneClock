package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class CreateSignWhenClockIsBreak {
    private static final CreateSignWhenClockIsBreak ourInstance = new CreateSignWhenClockIsBreak();

    public static CreateSignWhenClockIsBreak getInstance() {
        return ourInstance;
    }

    private CreateSignWhenClockIsBreak() {
    }

    public void performCMD(CommandSender sender, String... args) {
        FileConfiguration config = Main.getInstance().getConfig();
        if (args.length == 1) {
            changeValueAndSendMessage(sender, config, !config.getBoolean("CreateSignWhenClockIsBreak"));
        } else {
            if (args[1].equalsIgnoreCase("true")) {
                changeValueAndSendMessage(sender, config, true);
            } else if (!args[1].equalsIgnoreCase("false")) {
                sender.sendMessage(Msg.COMMAND_USE.toString()
                        .replace("$command", "CreateSignWhenClockIsBreak <true/false>"));
            }
        }
    }

    private void changeValueAndSendMessage(CommandSender sender, FileConfiguration config, boolean newValue) {
        config.set("CreateSignWhenClockIsBreak", newValue);
        Main.getInstance().saveConfig();
        sender.sendMessage(Msg.PREFIX + Msg.NEW_VALUE_IN_CONFIG.toString()
                .replace("$setting", "\"CreateSignWhenClockIsBreak\"")
                .replace("$value", String.valueOf(newValue)));
    }
}
