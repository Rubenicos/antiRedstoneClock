package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.command.CommandSender;

public class SetDelay {
    private static final SetDelay ourInstance = new SetDelay();

    public static SetDelay getInstance() {
        return ourInstance;
    }

    private SetDelay() {
    }

    public void performCMD(CommandSender sender, String... args) {
        try {
            Main.getInstance().getConfig().set("Delay", Integer.parseInt(args[1]));
            Main.getInstance().saveConfig();
            sender.sendMessage(
                    Msg.PREFIX + Msg.NEW_VALUE_IN_CONFIG.toString().replace("$setting", "\"Delay\"")
                            .replace("$value", args[1]));
        } catch (NumberFormatException e) {
            sender.sendMessage(Msg.COMMAND_USE.toString().replace("$command", "SetDelay <number>"));
        }
    }
}
