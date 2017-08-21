package com.trafalcraft.antiRedstoneClock;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.error.YAMLException;

import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;

public class Main extends JavaPlugin{

	private static Main instance;
	private static JavaPlugin plugin;
	private int maxImpulsions;
	private int delay;
	private boolean notifyAdmin;
	private boolean DropItems;
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	
	private static final ArrayList<String> ignoreWorld = new ArrayList<>();
	private static final ArrayList<String> ignoreRegion = new ArrayList<>();
	
	public void onEnable(){
		long startTime = System.currentTimeMillis();
		
		instance = this;
		plugin = this;
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		
		if(!getConfig().getString("version").equals("0.4")){
			if(getConfig().getString("version").equals("0.1")){
				this.getLogger().info("update config file to 0.2");
				getConfig().set("version", "0.2");
				getConfig().set("IgnoreWorlds", "redstoneWorld/survival");
				getConfig().set("IgnoreRegions", "redstone/admins");
				getConfig().set("Msg.message.newValueInConfig", "The new value of $setting is $value");
				getConfig().set("Msg.message.RedStoneClockListHeader", "RedstoneClockList: $page");
				getConfig().set("Msg.message.RedStoneClockListFooter", "");
				plugin.saveConfig();
				plugin.reloadConfig();
			}
			if(getConfig().getString("version").equals("0.2")){
				this.getLogger().info("update config file to 0.3");
				getConfig().set("version", "0.3");
				getConfig().set("IgnoreRegions", "redstone/admins");
				getConfig().set("Msg.message.newValueInConfig", "The new value of $setting is $value");
				getConfig().set("Msg.message.RedStoneClockListHeader", "RedstoneClockList: $page");
				getConfig().set("Msg.message.RedStoneClockListFooter", "");
				plugin.saveConfig();
				plugin.reloadConfig();
			}
			if(getConfig().getString("version").equals("0.3")){
				this.getLogger().info("update config file to 0.4");
				getConfig().set("version", "0.4");
				getConfig().set("DropItems", true);
				plugin.saveConfig();
				plugin.reloadConfig();
			}
		}
		
		//if(instance.getConfig().getInt("version") != 0.1){
			//plus tard
		//}

		line1 = "";
		line2 = "";
		line3 = "";
		line4 = "";
		
		//PlayerListener pl = new PlayerListener();

		//pl.onRedstoneClock(null);
		
		try{
			CustomConfig.load();
			String sIgnoreWorld = getPlugin().getConfig().getString("IgnoreWorlds");
			for(int i = 0;i<sIgnoreWorld.split("/").length;i++){
				ignoreWorld.add(sIgnoreWorld.split("/")[i]);
			}
			String sIgnoreRegion = getPlugin().getConfig().getString("IgnoreRegions");
			for(int i = 0;i<sIgnoreRegion.split("/").length;i++){
				ignoreRegion.add(sIgnoreRegion.split("/")[i]);
			}
		}catch(YAMLException e){
			setMaxImpulsions(150);
			setDelay(300);
			setNotifyAdmin(true);
			setLine1("The redstoneClock");
			setLine2("are");
			setLine3("§4PROHIBITED");
			setLine4("");
			instance.getLogger().warning("An error as occured in the config.yml please fix it!");
			e.printStackTrace();
		}
		
		checkTimer(getDelay());
		long endTime = System.currentTimeMillis();

		long duration = (endTime - startTime);
		//redstoneOverload();
		this.getLogger().info("Plugin chargé en "+duration+"ms");  //2ms
	}
	
	private static void checkTimer(int delay){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {

			@Override
			public void run() {
				for(RedstoneClock brdc : RedstoneClockController.getAll()){
					if(brdc.isEnd()){
						RedstoneClockController.removeRedstoneByObject(brdc);
					}
				}
			}
		}, 100, 20*delay);
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[]args){
		//Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("antiredstoneclock")){
			if(sender.isOp() || sender.hasPermission("antiRedstoneClock.Admin")){
				if(args.length == 0){
					CustomConfig.getHelp((Player) sender);
					return false;
				}
				if(args[0].equalsIgnoreCase("reload")){
					try{
						getPlugin().reloadConfig();
						CustomConfig.load();
						String sIgnoreWorld = getPlugin().getConfig().getString("IgnoreWorlds");
						ignoreWorld.clear();
						for(int i = 0;i<sIgnoreWorld.split("/").length;i++){
							ignoreWorld.add(sIgnoreWorld.split("/")[i]);
						}
						sender.sendMessage(CustomConfig.Prefix+CustomConfig.reloadSuccess.toString());
					}catch(YAMLException e){
						if(sender instanceof Player){
							sender.sendMessage(CustomConfig.ERREUR+"An error as occured in the config.yml please check the log!");
						}
						instance.getLogger().warning("An error as occured in the config.yml please fix it!");
						e.printStackTrace();
					}
				} else if(args[0].equalsIgnoreCase("checkList")){
					try{
						int test = 5;
						if(args.length > 1){
							test = Integer.parseInt(args[1]) * 5;
						}
						int indice = 0;
						sender.sendMessage(CustomConfig.RedStoneClockListHeader.toString().replace("$page", "("+test/5+"/"+((RedstoneClockController.getAllLoc().size()/5)+1)+")"));
						for(Location loc : RedstoneClockController.getAllLoc()){
							if(!(indice+1 > test+1) && !(indice+1 < test-4)){
								if(RedstoneClockController.getRedstoneClock(loc).getBoucle() > Main.getMaxImpulsions()*0.750){
									sender.sendMessage("§4RedStoneClock> §fWorld:"+loc.getWorld().getName()+",X:"+loc.getX()+",Y:"+loc.getY()+",Z:"+loc.getZ()+" b:"+RedstoneClockController.getRedstoneClock(loc).getBoucle()+"/"+getMaxImpulsions());
								}else if (RedstoneClockController.getRedstoneClock(loc).getBoucle() > Main.getMaxImpulsions()*0.5){
									sender.sendMessage("§eRedStoneClock> §fWorld:"+loc.getWorld().getName()+",X:"+loc.getX()+",Y:"+loc.getY()+",Z:"+loc.getZ()+" b:"+RedstoneClockController.getRedstoneClock(loc).getBoucle()+"/"+getMaxImpulsions());
								}else if (RedstoneClockController.getRedstoneClock(loc).getBoucle() > Main.getMaxImpulsions()*0.250){
									sender.sendMessage("§aRedStoneClock> §fWorld:"+loc.getWorld().getName()+",X:"+loc.getX()+",Y:"+loc.getY()+",Z:"+loc.getZ()+" b:"+RedstoneClockController.getRedstoneClock(loc).getBoucle()+"/"+getMaxImpulsions());
								}else{
									sender.sendMessage("§2RedStoneClock> §fWorld:"+loc.getWorld().getName()+",X:"+loc.getX()+",Y:"+loc.getY()+",Z:"+loc.getZ()+" b:"+RedstoneClockController.getRedstoneClock(loc).getBoucle()+"/"+getMaxImpulsions());
								}
							}
							indice++;
						}
						sender.sendMessage(CustomConfig.RedStoneClockListFooter.toString());
					}catch(NumberFormatException e){
						sender.sendMessage(CustomConfig.Command_Use.toString().replace("$commande", "checkList <number>"));
					}
				}else if(args[0].equalsIgnoreCase("setMaxImpulsion")){
					try{
						Integer.parseInt(args[1]);
						getConfig().set("MaxImpulsion", args[1]);
						saveConfig();
						sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"MaxImpulsion\"").replace("$value", args[1]));
					}catch(NumberFormatException e){
						sender.sendMessage(CustomConfig.Command_Use.toString().replace("$commande", "setMaxImpulsion <number>"));
					}
				}else if(args[0].equalsIgnoreCase("setDelay")){
					try{
						Integer.parseInt(args[1]);
						getConfig().set("Delay", args[1]);
						saveConfig();
						sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"Delay\"").replace("$value", args[1]));
					}catch(NumberFormatException e){
						sender.sendMessage(CustomConfig.Command_Use.toString().replace("$commande", "setDelay <number>"));
					}
				}else if(args[0].equalsIgnoreCase("notifyAdmin")){
					if(args.length == 1){
						if(getConfig().getBoolean("NotifyAdmins")){
							getConfig().set("NotifyAdmins", false);
							saveConfig();
							sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"NotifyAdmins\"").replace("$value", "false"));
						}else{
							getConfig().set("NotifyAdmins", true);
							saveConfig();
							sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"NotifyAdmins\"").replace("$value", "true"));
						}
					}else{
						if(Boolean.parseBoolean(args[1])){
							getConfig().set("NotifyAdmins", true);
							saveConfig();
							sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"NotifyAdmins\"").replace("$value", args[1]));
						}else if(!Boolean.parseBoolean(args[1])){
							getConfig().set("NotifyAdmins", false);
							saveConfig();
							sender.sendMessage(CustomConfig.Prefix+CustomConfig.newValueInConfig.toString().replace("$setting", "\"NotifyAdmins\"").replace("$value", args[1]));
						}
					}
				}else{
					CustomConfig.getHelp((Player) sender);
				}
				
					/*else if(args[0].equalsIgnoreCase("help")){
				}
					if(sender instanceof Player){
						CustomConfig.getHelp((Player) sender);
					}
				}*/
			}else{
				sender.sendMessage(CustomConfig.unknownCmd.toString());
			}
		}else{
			sender.sendMessage(CustomConfig.unknownCmd.toString());
		}
		return false;
	}
	
	
	public static Main getInstance(){
		return instance;
	}
	public static JavaPlugin getPlugin(){
		return plugin;
	}
	
	
	public static int getMaxImpulsions(){
		return instance.maxImpulsions;
	}
	
	public static void setMaxImpulsions(int value){
		instance.maxImpulsions = value;
	}

	public static int getDelay(){
		return instance.delay;
	}
	
	public static void setDelay(int delay){
		instance.delay = delay;
	}
	
	public static boolean isNotifyAdmin(){
		return instance.notifyAdmin;
	}
	
	public static void setNotifyAdmin(boolean value ){
		instance.notifyAdmin = value;
	}
	
	public static boolean isDropItems() {
		return instance.DropItems;
	}

	public static void setDropItems(boolean dropItems) {
		instance.DropItems = dropItems;
	}
	
	public static String getLine1(){
		return instance.line1;
	}
	public static void setLine1(String value){
		instance.line1 = value;
	}
	
	public static String getLine2(){
		return instance.line2;
	}
	public static void setLine2(String value){
		instance.line2 = value;
	}
	
	public static String getLine3(){
		return instance.line3;
	}
	public static void setLine3(String value){
		instance.line3 = value;
	}
	
	public static String getLine4(){
		return instance.line4;
	}
	public static void setLine4(String value){
		instance.line4 = value;
	}
	
	public static Collection<String> getAllowedWorlds(){
		return ignoreWorld;
	}
	public static Collection<String> getAllowedRegions(){
		return ignoreRegion;
	}
	

}
