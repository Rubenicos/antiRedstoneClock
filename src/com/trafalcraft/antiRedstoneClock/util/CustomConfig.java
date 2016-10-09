package com.trafalcraft.antiRedstoneClock.util;


import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.trafalcraft.antiRedstoneClock.Main;

public enum CustomConfig {
	
	Prefix("&bAntiRedstoneClock &9&l> &r&b "),
	ERREUR("&4AntiRedstoneClock &l> &r&c "),
	NO_PERMISSIONS("&4Erreur &9&l> &r&bYou dont have permission to do that!"),
	Command_Use("&4SnowBallWar &l> &r&cCommand usage: &6/arc $commande"),

	//Msg
	MsgToAdmin("Redstone clock disable in x:$X y:$Y Z:$Z. In the world $World"),
	reloadSuccess("Reload Success!"),
	unknownCmd("Unknown command. Type \"/help\" for help."),
	newValueInConfig("The new value of $setting is $value"),
	RedStoneClockListHeader("RedstoneClockList: $page"),
	RedStoneClockListFooter(""),
	//Exception
	duplicate_object("This list already contains this redstone");

	static JavaPlugin plugin = Main.getInstance();
	  public static void getHelp(Player sender){
	        sender.sendMessage("");
	        sender.sendMessage("§3§l-------------AntiRedstoneClock-------------");
	        sender.sendMessage("§3/arc checkList <nom de l'arene> §b- display the active redstoneclock.");
	        sender.sendMessage("§3/arc setMaxImpulsion <numero> §b- Change the 0\"MaxImpulsion\" setting.");
	        sender.sendMessage("§3/arc setDelay <numero> §b- Change the \"Delay\" setting.");
	        sender.sendMessage("§3/arc notifyAdmin <numero> §b- change the \"notifyAdmin\" setting.");
	        sender.sendMessage("§3/arc reload <numero> §b- To reload the config file.");
	        sender.sendMessage("                       §3Version: §6" + plugin.getDescription().getVersion());
	        sender.sendMessage("§3------------------------------------------------");
	        sender.sendMessage("");
		  }
	  
	  
	    private String value;

		private CustomConfig(String value) {
			this.value = value;
	    }
		
	    public String toString(){
	    	return value;
	    }
	    public void replaceby(String value){
			this.value = value;
	    }
	    
	    public static void load(){

	    	Prefix.replaceby(Main.getPlugin().getConfig().getString("Msg.default.prefix").replace("&", "§"));
	    	ERREUR.replaceby(Main.getPlugin().getConfig().getString("Msg.default.error").replace("&", "§"));
	    	NO_PERMISSIONS.replaceby(Main.getPlugin().getConfig().getString("Msg.default.no_permission").replace("&", "§"));
	    	Command_Use.replaceby(Main.getPlugin().getConfig().getString("Msg.default.command_use").replace("&", "§"));
	    	
	    	MsgToAdmin.replaceby(Main.getPlugin().getConfig().getString("Msg.message.MsgToAdmin").replace("&", "§"));
	    	reloadSuccess.replaceby(Main.getPlugin().getConfig().getString("Msg.message.reloadSuccess").replace("&", "§"));
	    	unknownCmd.replaceby(Main.getPlugin().getConfig().getString("Msg.message.unknownCmd").replace("&", "§"));
	    	newValueInConfig.replaceby(Main.getPlugin().getConfig().getString("Msg.message.newValueInConfig").replace("&", "§"));
	    	RedStoneClockListHeader.replaceby(Main.getPlugin().getConfig().getString("Msg.message.RedStoneClockListHeader").replace("&", "§"));
	    	RedStoneClockListFooter.replaceby(Main.getPlugin().getConfig().getString("Msg.message.RedStoneClockListFooter").replace("&", "§"));
	    	
	    	duplicate_object.replaceby(Main.getPlugin().getConfig().getString("Msg.Exception.duplicate_object").replace("&", "§"));
	    	
	    	Main.setMaxImpulsions(Main.getPlugin().getConfig().getInt("MaxImpulsion"));
	    	Main.setDelay(Main.getPlugin().getConfig().getInt("Delay"));
	    	Main.setNotifyAdmin(Main.getPlugin().getConfig().getBoolean("NotifyAdmins"));
	    	Main.setDropItems(Main.getPlugin().getConfig().getBoolean("DropItems"));
	    	Main.setLine1(Main.getPlugin().getConfig().getString("Sign.Line1").replace("&", "§"));
	    	Main.setLine2(Main.getPlugin().getConfig().getString("Sign.Line2").replace("&", "§"));
	    	Main.setLine3(Main.getPlugin().getConfig().getString("Sign.Line3").replace("&", "§"));
	    	Main.setLine4(Main.getPlugin().getConfig().getString("Sign.Line4").replace("&", "§"));
	    }
	    
}