package com.trafalcraft.anti_redstone_clock.commands;

import org.bukkit.command.CommandSender;

import com.trafalcraft.anti_redstone_clock.Main;
import com.trafalcraft.anti_redstone_clock.util.Msg;

public class SetMaxPulses {
    private static final SetMaxPulses ourInstance = new SetMaxPulses();

    public static SetMaxPulses getInstance() {
        return ourInstance;
    }

    private SetMaxPulses() {
    }

    public void performCMD(CommandSender sender, String... args) {
        try {
            Main.getInstance().getConfig().set("MaxPulses", Integer.parseInt(args[1]));
            Main.getInstance().saveConfig();
            sender.sendMessage(Msg.PREFIX + Msg.NEW_VALUE_IN_CONFIG.toString().replace("$setting", "\"MaxPulses\"")
                    .replace("$value", args[1]));
        } catch (NumberFormatException e) {
            sender.sendMessage(Msg.COMMAND_USE.toString().replace("$command", "setMaxPulses <number>"));
        }
    }
}
