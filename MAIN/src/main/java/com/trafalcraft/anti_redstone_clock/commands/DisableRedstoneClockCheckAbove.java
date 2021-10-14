package com.trafalcraft.anti_redstone_clock.commands;

import org.bukkit.command.CommandSender;

import com.trafalcraft.anti_redstone_clock.Main;
import com.trafalcraft.anti_redstone_clock.util.Msg;

public class DisableRedstoneClockCheckAbove {
    private static final DisableRedstoneClockCheckAbove ourInstance = new DisableRedstoneClockCheckAbove();

    public static DisableRedstoneClockCheckAbove getInstance() {
        return ourInstance;
    }

    private DisableRedstoneClockCheckAbove() {
    }

    public void performCMD(CommandSender sender, String... args) {
        try {
            Main.getInstance().getConfig().set("disableRedstoneClockCheckAbove", Integer.parseInt(args[1]));
            Main.getInstance().saveConfig();
            sender.sendMessage(
                    Msg.PREFIX + Msg.NEW_VALUE_IN_CONFIG.toString().replace("$setting", "\"disableRedstoneClockCheckAbove\"")
                            .replace("$value", args[1]));
        } catch (NumberFormatException e) {
            sender.sendMessage(Msg.COMMAND_USE.toString().replace("$command", "disableRedstoneClockCheckAbove <number>"));
        }
    }
}
