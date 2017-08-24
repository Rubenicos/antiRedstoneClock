package com.trafalcraft.antiRedstoneClock.util;


import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.trafalcraft.antiRedstoneClock.Main;

import java.util.Arrays;

public enum CustomConfig {
	
	Prefix("&bAntiRedstoneClock &9&l> &r&b "),
	ERROR("&4AntiRedstoneClock &l> &r&c "),
	NO_PERMISSIONS("&4ERROR &9&l> &r&bYou don't have permission to do that!"),
	Command_Use("&4AntiRedstoneClock &l> &r&cCommand usage: &6/arc $command"),

	//Msg
	MsgToAdmin("Redstone clock disable in x:$X y:$Y Z:$Z. In the world $World"),
	reloadSuccess("Reload Success!"),
	unknownCmd("Unknown command. Type \"/help\" for help."),
	newValueInConfig("The new value of $setting is $value"),
	RedStoneClockListHeader("RedstoneClockList: $page"),
	RedStoneClockListFooter(""),
	//Exception
	duplicate_object("This list already contains this redstone");

	static final JavaPlugin plugin = Main.getInstance();
	  public static void getHelp(Player sender){
	        sender.sendMessage("");
	        sender.sendMessage("§3§l-------------AntiRedstoneClock-------------");
	        sender.sendMessage("§3/arc checkList <Page number> §b- display the active redstoneclock.");
	        sender.sendMessage("§3/arc setMaxPulses <number> §b- Change the \"MaxPulses\" setting.");
	        sender.sendMessage("§3/arc SetDelay <number> §b- Change the \"Delay\" setting.");
	        sender.sendMessage("§3/arc NotifyAdmin <number> §b- change the \"NotifyAdmin\" setting.");
	        sender.sendMessage("§3/arc Reload §b- To Reload the config file.");
	        sender.sendMessage("                       §3Version: §6" + plugin.getDescription().getVersion());
	        sender.sendMessage("§3------------------------------------------------");
	        sender.sendMessage("");
		  }
	  
	  
	    private String value;

		CustomConfig(String value) {
			this.value = value;
	    }
		
	    public String toString(){
	    	return value;
	    }
	    private void replaceBy(String value){
			this.value = value;
	    }

	    public static void setDefaultsValues(){
            Main.setMaximumPulses(150);
            Main.setDelay(300);
            Main.setNotifyAdmin(true);
            Main.setLine1("The redstoneClock");
            Main.setLine2("are");
            Main.setLine3("§4PROHIBITED");
            Main.setLine4("");
            Main.getInstance().getLogger().warning("An error as occurred in the config.yml please fix it!");
        }
	    
	    public static void load(){

	    	Prefix.replaceBy(Main.getPlugin().getConfig().getString("Msg.default.prefix").replace("&", "§"));
	    	ERROR.replaceBy(Main.getPlugin().getConfig().getString("Msg.default.error").replace("&", "§"));
	    	NO_PERMISSIONS.replaceBy(Main.getPlugin().getConfig().getString("Msg.default.no_permission").replace("&", "§"));
	    	Command_Use.replaceBy(Main.getPlugin().getConfig().getString("Msg.default.command_use").replace("&", "§"));
	    	
	    	MsgToAdmin.replaceBy(Main.getPlugin().getConfig().getString("Msg.message.MsgToAdmin").replace("&", "§"));
	    	reloadSuccess.replaceBy(Main.getPlugin().getConfig().getString("Msg.message.reloadSuccess").replace("&", "§"));
	    	unknownCmd.replaceBy(Main.getPlugin().getConfig().getString("Msg.message.unknownCmd").replace("&", "§"));
	    	newValueInConfig.replaceBy(Main.getPlugin().getConfig().getString("Msg.message.newValueInConfig").replace("&", "§"));
	    	RedStoneClockListHeader.replaceBy(Main.getPlugin().getConfig().getString("Msg.message.RedStoneClockListHeader").replace("&", "§"));
	    	RedStoneClockListFooter.replaceBy(Main.getPlugin().getConfig().getString("Msg.message.RedStoneClockListFooter").replace("&", "§"));
	    	
	    	duplicate_object.replaceBy(Main.getPlugin().getConfig().getString("Msg.Exception.duplicate_object").replace("&", "§"));
	    	
	    	Main.setMaximumPulses(Main.getPlugin().getConfig().getInt("MaxPulses"));
	    	Main.setDelay(Main.getPlugin().getConfig().getInt("Delay"));
	    	Main.setNotifyAdmin(Main.getPlugin().getConfig().getBoolean("NotifyAdmins"));
	    	Main.setDropItems(Main.getPlugin().getConfig().getBoolean("DropItems"));
	    	Main.setLine1(Main.getPlugin().getConfig().getString("Sign.Line1").replace("&", "§"));
	    	Main.setLine2(Main.getPlugin().getConfig().getString("Sign.Line2").replace("&", "§"));
	    	Main.setLine3(Main.getPlugin().getConfig().getString("Sign.Line3").replace("&", "§"));
	    	Main.setLine4(Main.getPlugin().getConfig().getString("Sign.Line4").replace("&", "§"));

            String sIgnoreWorld = Main.getPlugin().getConfig().getString("IgnoreWorlds");
            Main.getIgnoredWorlds().addAll(Arrays.asList(sIgnoreWorld.split("/")));
            String sIgnoreRegion = Main.getPlugin().getConfig().getString("IgnoreRegions");
            Main.getIgnoredRegions().addAll(Arrays.asList(sIgnoreRegion.split("/")));
	    }
	    
}
