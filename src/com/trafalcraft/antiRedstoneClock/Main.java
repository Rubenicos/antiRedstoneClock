package com.trafalcraft.antiRedstoneClock;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.error.YAMLException;

import com.trafalcraft.antiRedstoneClock.object.RedstoneClock;
import com.trafalcraft.antiRedstoneClock.object.RedstoneClockController;
import com.trafalcraft.antiRedstoneClock.util.CustomConfig;

public class Main extends JavaPlugin{

	private RedstoneClockController rdc;
	private static Main instance;
	private static JavaPlugin plugin;
	private int maxImpulsions;
	private int delay;
	private boolean notifyAdmin;
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	
	private static ArrayList<String> ignoreWorld = new ArrayList<String>();
	
	public void onEnable(){
		long startTime = System.nanoTime();
		instance = this;
		plugin = this;
		this.rdc = new RedstoneClockController(); 
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		checkTimer();
		
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		
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
		
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		
		//redstoneOverload();
		this.getLogger().info("Plugin chargé en "+duration/1000000+"ms");  //2ms
	}
	
	private void checkTimer(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			
			@Override
			public void run() {
				for(RedstoneClock brdc : getRDC().getAll()){
					if(brdc.getMinutes() > brdc.getEndTimerInMinutes()){
						getRDC().removeRedstoneByObject(brdc);
					}
				}
			}
		}, 0, 20*Main.getDelay());
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[]args){
		//Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("antiredstoneclock")){
			if(sender.isOp() || sender.hasPermission("antiRedstoneClock.Admin")){
				if(args[0].equalsIgnoreCase("reload")){
					try{
						getPlugin().reloadConfig();
						CustomConfig.load();
						sender.sendMessage(CustomConfig.Prefix+CustomConfig.reloadSuccess.toString());
					}catch(YAMLException e){
						if(sender instanceof Player){
							sender.sendMessage(CustomConfig.ERREUR+"An error as occured in the config.yml please check the log!");
						}
						instance.getLogger().warning("An error as occured in the config.yml please fix it!");
						e.printStackTrace();
					}
				}/*else if(args[0].equalsIgnoreCase("help")){
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
	}		//if(instance.getConfig().getInt("version") != 0.1){
	//plus tard
//}
	public static JavaPlugin getPlugin(){
		return plugin;
	}
	
	
	public static RedstoneClockController getRDC(){
		return instance.rdc;
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
	
	public static boolean getNotifyAdmin(){
		return instance.notifyAdmin;
	}
	
	public static void setNotifyAdmin(boolean value ){
		instance.notifyAdmin = value;
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
	
	public static Collection<String> getAllowedWorld(){
		return ignoreWorld;
	}
	

}
