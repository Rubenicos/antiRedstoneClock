package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class AutoRemoveDetectedClock {
    private static final AutoRemoveDetectedClock ourInstance = new AutoRemoveDetectedClock();

    public static AutoRemoveDetectedClock getInstance() {
        return ourInstance;
    }

    private AutoRemoveDetectedClock() {
    }

    public void performCMD(CommandSender sender, String... args) {
        FileConfiguration config = Main.getInstance().getConfig();
        if (args.length == 1) {
            changeValueAndSendMessage(sender, config, !config.getBoolean("AutomaticallyBreakDetectedClock"));
        } else {
            if (args[1].equalsIgnoreCase("true")) {
                changeValueAndSendMessage(sender, config, true);
            } else if (!args[1].equalsIgnoreCase("false")) {
                sender.sendMessage(Msg.COMMAND_USE.toString()
                        .replace("$command", "AutomaticallyBreakDetectedClock <true/false>"));
            }
        }
    }

    private void changeValueAndSendMessage(CommandSender sender, FileConfiguration config, boolean newValue) {
        config.set("AutomaticallyBreakDetectedClock", newValue);
        Main.getInstance().saveConfig();
        sender.sendMessage(Msg.PREFIX + Msg.NEW_VALUE_IN_CONFIG.toString()
                .replace("$setting", "\"AutomaticallyBreakDetectedClock\"")
                .replace("$value", String.valueOf(newValue)));
    }
}
