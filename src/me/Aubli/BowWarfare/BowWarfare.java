package me.Aubli.BowWarfare;

import java.io.IOException;
import java.util.logging.Level;

import me.Aubli.BowWarfare.Game.GameManager;
import me.Aubli.BowWarfare.Listeners.BlockListener;
import me.Aubli.BowWarfare.Listeners.EntityDamageListener;
import me.Aubli.BowWarfare.Listeners.PlayerInteractListener;
import me.Aubli.BowWarfare.Listeners.SignChangeListener;
import me.Aubli.BowWarfare.Sign.SignManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.util.Logger.PluginOutput;
import org.util.Metrics.Metrics;

public class BowWarfare extends JavaPlugin{
	
	public static PluginOutput log;
	
	private static BowWarfare instance;
	
	private static int maxP;
	private static int minP;
	private static int COUNTDOWN;
	private static int GAME_DURATION;
	
	private boolean useMetrics;
	private boolean debugMode;
	private int logLevel;
	
	private static String pluginPrefix = ChatColor.GOLD + "[" + ChatColor.DARK_PURPLE + "BW" + ChatColor.GOLD + "]" + ChatColor.RESET + " ";
	
	@Override
	public void onDisable(){
		GameManager.getManager().shutdown();
		log.log("Plugin disabled!");
	}
	
	@Override
	public void onEnable(){		
		init();	
		log.log("Plugin enabled!");
	}
	
	private void init(){
		instance = this;
		
		loadConfig();
		log = new PluginOutput(instance, debugMode, logLevel);
		
		new GameManager();
		new SignManager();
		
		registerListeners();
		
		getCommand("bw").setExecutor(new BowExecuter());
		
		if(useMetrics==true){
			try {
			    Metrics metrics = new Metrics(this);
			    metrics.start();			   
			} catch (IOException e) {
				log.log(Level.WARNING, "Can't start Metrics! Skip!" , false, e);
			}
		}
	}
	
	private void registerListeners(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new SignChangeListener(), this);
		pm.registerEvents(new EntityDamageListener(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
		pm.registerEvents(new BlockListener(), this);
	}
	
	private void loadConfig(){
		
		getConfig().addDefault("plugin.enableMetrics", true);
		getConfig().addDefault("plugin.debugMode", false);
		getConfig().addDefault("plugin.loglevel", Level.FINE.intValue());
		
		useMetrics = getConfig().getBoolean("plugin.enableMetrics");		
		debugMode = getConfig().getBoolean("plugin.debugMode");
		logLevel = getConfig().getInt("plugin.loglevel");
		
		getConfig().addDefault("game.minPlayers", 5);
		getConfig().addDefault("game.maxPlayers", 24);
		
		getConfig().addDefault("game.times.countdown", 30);
		getConfig().addDefault("game.times.gameDuration", 10);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		maxP = getConfig().getInt("game.maxPlayers");
		minP = getConfig().getInt("game.minPlayers");
		
		GAME_DURATION = getConfig().getInt("game.times.gameDuration");
		COUNTDOWN = getConfig().getInt("game.times.countdown");		
	}
	
	
	public static BowWarfare getInstance(){
		return instance;
	}
	
	public static PluginOutput getPluginLog() {
		return log;
	}
	
	public static int getMaxPlayers(){
		return maxP;
	}
	
	public static int getMinPlayers(){
		return minP;
	}
	
	public static int getCountdownTime(){
		return COUNTDOWN;
	}

	public static int getGameTime(){
		return (GAME_DURATION * 60);
	}
	
	public static String getPrefix(){
		return pluginPrefix;
	}	
	
}
