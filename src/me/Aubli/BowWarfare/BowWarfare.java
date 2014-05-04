package me.Aubli.BowWarfare;

import java.util.logging.Logger;

import me.Aubli.BowWarfare.Listeners.EntityDamageListener;
import me.Aubli.BowWarfare.Listeners.PlayerInteractListener;
import me.Aubli.BowWarfare.Listeners.PlayerRespawnListener;
import me.Aubli.BowWarfare.Listeners.SignChangeListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BowWarfare extends JavaPlugin{
	
	public static Logger log = Bukkit.getLogger();
	
	@Override
	public void onDisable(){
		log.info("[" + getDescription().getName() + "] disabled!");
	}
	
	@Override
	public void onEnable(){
		
		init();
		
		log.info("[" + getDescription().getName() + "] enabled!");
	}
	
	private void init(){
		registerListeners();
	}
	
	private void registerListeners(){
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new SignChangeListener(), this);
		pm.registerEvents(new PlayerRespawnListener(), this);
		pm.registerEvents(new EntityDamageListener(), this);
		pm.registerEvents(new PlayerInteractListener(), this);
	}
	
}
