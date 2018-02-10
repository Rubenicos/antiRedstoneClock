package com.trafalcraft.antiRedstoneClock.commands;

import com.trafalcraft.antiRedstoneClock.Main;
import com.trafalcraft.antiRedstoneClock.util.Msg;
import org.bukkit.command.CommandSender;

public class CreateSignWhenClockIsBreak {
        private static CreateSignWhenClockIsBreak ourInstance = new CreateSignWhenClockIsBreak();

        public static CreateSignWhenClockIsBreak getInstance() {
                return ourInstance;
        }

        private CreateSignWhenClockIsBreak() {
        }

        public void performCMD(CommandSender sender, String... args) {
                if (args.length == 1) {
                        if (Main.getInstance().getConfig().getBoolean("CreateSignWhenClockIsBreak")) {
                                Main.getInstance().getConfig().set("CreateSignWhenClockIsBreak", false);
                                Main.getInstance().saveConfig();
                                sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString()
                                        .replace("$setting", "\"CreateSignWhenClockIsBreak\"")
                                        .replace("$value", "false"));
                        } else {
                                Main.getInstance().getConfig().set("CreateSignWhenClockIsBreak", true);
                                Main.getInstance().saveConfig();
                                sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString()
                                        .replace("$setting", "\"CreateSignWhenClockIsBreak\"")
                                        .replace("$value", "true"));
                        }
                } else {
                        if (Boolean.parseBoolean(args[1])) {
                                Main.getInstance().getConfig().set("CreateSignWhenClockIsBreak", true);
                                Main.getInstance().saveConfig();
                                sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString()
                                        .replace("$setting", "\"CreateSignWhenClockIsBreak\"")
                                        .replace("$value", args[1]));
                        } else if (!Boolean.parseBoolean(args[1])) {
                                Main.getInstance().getConfig().set("CreateSignWhenClockIsBreak", false);
                                Main.getInstance().saveConfig();
                                sender.sendMessage(Msg.Prefix + Msg.newValueInConfig.toString()
                                        .replace("$setting", "\"CreateSignWhenClockIsBreak\"")
                                        .replace("$value", args[1]));
                        }
                }
        }
}
