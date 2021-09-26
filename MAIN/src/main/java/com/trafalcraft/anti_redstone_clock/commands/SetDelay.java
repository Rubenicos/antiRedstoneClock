package com.trafalcraft.anti_redstone_clock.commands;

import org.bukkit.command.CommandSender;

import com.trafalcraft.anti_redstone_clock.Main;
import com.trafalcraft.anti_redstone_clock.util.Msg;

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
