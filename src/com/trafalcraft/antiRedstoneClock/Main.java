package com.trafalcraft.antiRedstoneClock;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.trafalcraft.antiRedstoneClock.commands.*;
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

	private static Main instance;
	private static JavaPlugin plugin;
	private int maximumPulses;
	private int delay;
	private boolean notifyAdmin;
	private boolean DropItems;
	private String line1;
	private String line2;
	private String line3;
	private String line4;
	
	private static final ArrayList<String> ignoredWorlds = new ArrayList<>();
	private static final ArrayList<String> ignoredRegions = new ArrayList<>();
	
	public void onEnable(){
		long startTime = System.currentTimeMillis();
		
		instance = this;
		plugin = this;
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveDefaultConfig();
		plugin.reloadConfig();

        if(!getConfig().getString("version").equals("0.5")){
            File f = new File(getPlugin().getDataFolder().getPath()+"//config.yml");
            File newFile = new File(f.getPath()+"-"+getConfig().getString("version")+".old");
            f.renameTo(newFile);
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveDefaultConfig();
            plugin.reloadConfig();
        }
		
		try{
			CustomConfig.load();
		}catch(YAMLException e){
            CustomConfig.setDefaultsValues();
			e.printStackTrace();
		}
		
		checkTimer(getDelay());
		long endTime = System.currentTimeMillis();

		long duration = (endTime - startTime);
		this.getLogger().info("Plugin loaded in "+duration+"ms");  //2ms
	}
	
	private static void checkTimer(int delay){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {

			@Override
			public void run() {
				for(RedstoneClock rdc : RedstoneClockController.getAll()){
					if(rdc.isEnd()){
						RedstoneClockController.removeRedstoneByObject(rdc);
					}
				}
			}
		}, 100, 20*delay);
	}
	
	public void onDisable(){

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[]args){
		if(cmd.getName().equalsIgnoreCase("antiredstoneclock")){
			if(sender.isOp() || sender.hasPermission("antiRedstoneClock.Admin")){
				if(args.length == 0){
					CustomConfig.getHelp((Player) sender);
					return false;
				}
				if(args[0].equalsIgnoreCase("Reload")){
                    Reload.getInstance().performCMD(sender);
				} else if(args[0].equalsIgnoreCase("checkList")){
                    CheckList.getInstance().performCMD(sender, args);
				}else if(args[0].equalsIgnoreCase("setMaxPulses")){
                    SetMaxPulses.getInstance().performCMD(sender, args);
				}else if(args[0].equalsIgnoreCase("SetDelay")){
                    SetDelay.getInstance().performCMD(sender, args);
				}else if(args[0].equalsIgnoreCase("NotifyAdmin")){
                    NotifyAdmin.getInstance().performCMD(sender, args);
				}else{
					CustomConfig.getHelp((Player) sender);
				}
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
	
	
	public static int getMaximumPulses(){
		return instance.maximumPulses;
	}
	
	public static void setMaximumPulses(int value){
		instance.maximumPulses = value;
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
	
	public static Collection<String> getIgnoredWorlds(){
		return ignoredWorlds;
	}
	public static Collection<String> getIgnoredRegions(){
		return ignoredRegions;
	}
	

}
