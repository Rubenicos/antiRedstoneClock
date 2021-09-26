package com.trafalcraft.antiRedstoneClock.util;

import com.trafalcraft.antiRedstoneClock.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public enum Msg {

    PREFIX("&bAntiRedstoneClock &9&l> &r&b "),
    ERROR("&4AntiRedstoneClock &l> &r&c "),
    NO_PERMISSIONS("&4ERROR &9&l> &r&bYou don't have permission to do that!"),
    COMMAND_USE("&4AntiRedstoneClock &l> &r&cCommand usage: &6/arc $command"),

    //Msg
    MSG_TO_ADMIN("Redstone clock disable in x:$X y:$Y Z:$Z. In the world $World"),
    RELOAD_SUCCESS("Reload Success! Redstone listener and third party plugin are not affected!!!"),
    UNKNOWN_CMD("Unknown command. Type \"/help\" for help."),
    NEW_VALUE_IN_CONFIG("The new value of $setting is $value"),
    RED_STONE_CLOCK_LIST_HEADER("RedstoneClockList: $page"),
    RED_STONE_CLOCK_LIST_FOOTER(""),
    //Exception
    DUPLICATE_OBJECT("This list already contains this redstone");

    static final JavaPlugin plugin = Main.getInstance();

    public static void getHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("§3§l-------------AntiRedstoneClock-------------");
        sender.sendMessage("§3/arc checkList <Page number> §b- display the active redstoneclock.");
        sender.sendMessage("§3/arc setMaxPulses <number> §b- Change the \"MaxPulses\" setting.");
        sender.sendMessage("§3/arc setDelay <number> §b- Change the \"Delay\" setting.");
        sender.sendMessage("§3/arc notifyAdmin <true/false> §b- change the \"NotifyAdmin\" setting.");
        sender.sendMessage(
                "§3/arc autoRemoveDetectedClock <true/false> §b- change the \"AutoRemoveDetectedClock\" setting.");
        sender.sendMessage(
                "§3/arc createSignWhenClockIsBreak <true/false> §b- change the \"CreateSignWhenClockIsBreak\" setting.");
        sender.sendMessage("§3/arc reload §b- To Reload the config file. (doesn't work redstone listener and third party support)");
        sender.sendMessage("                       §3Version: §6" + plugin.getDescription().getVersion());
        sender.sendMessage("§3------------------------------------------------");
        sender.sendMessage("");
    }

    private String value;

    Msg(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    private void replaceBy(String value) {
        this.value = value;
    }

    public static void load() {
        FileConfiguration config = plugin.getConfig();
        PREFIX.replaceBy(config.getString("Msg.default.prefix").replace("&", "§"));
        ERROR.replaceBy(config.getString("Msg.default.error").replace("&", "§"));
        NO_PERMISSIONS.replaceBy(config.getString("Msg.default.no_permission").replace("&", "§"));
        COMMAND_USE.replaceBy(config.getString("Msg.default.command_use").replace("&", "§"));

        MSG_TO_ADMIN.replaceBy(config.getString("Msg.message.MsgToAdmin").replace("&", "§"));
        RELOAD_SUCCESS.replaceBy(config.getString("Msg.message.reloadSuccess").replace("&", "§"));
        UNKNOWN_CMD.replaceBy(config.getString("Msg.message.unknownCmd").replace("&", "§"));
        NEW_VALUE_IN_CONFIG.replaceBy(config.getString("Msg.message.newValueInConfig").replace("&", "§"));
        RED_STONE_CLOCK_LIST_HEADER.replaceBy(
                config.getString("Msg.message.RedStoneClockListHeader").replace("&", "§"));
        RED_STONE_CLOCK_LIST_FOOTER.replaceBy(
                config.getString("Msg.message.RedStoneClockListFooter").replace("&", "§"));

        DUPLICATE_OBJECT.replaceBy(config.getString("Msg.Exception.duplicate_object").replace("&", "§"));

        String sIgnoreWorld = config.getString("IgnoreWorlds");
        Main.getIgnoredWorlds().addAll(Arrays.asList(sIgnoreWorld.split("/")));
        String sIgnoreRegion = config.getString("IgnoreRegions");
        Main.getIgnoredRegions().addAll(Arrays.asList(sIgnoreRegion.split("/")));
    }

}
