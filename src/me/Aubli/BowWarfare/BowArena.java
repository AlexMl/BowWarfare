package me.Aubli.BowWarfare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BowArena {

	private int ID;
	
	private File arenaFile;
	private FileConfiguration arenaConfig;
	
	private World world;
	private Location min;
	private Location max;
	
	private int maxPlayers;
	private int minPlayers;
	
	private int TaskID;
	
	private boolean running;	
	
	private ArrayList<BowPlayer> players;
	
	public BowArena(String filePath, int ID, Location min, Location max, int maxP, int minP){
		
		this.arenaFile = new File(filePath + "/" + ID + ".yml");
		this.arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
		
		this.ID = ID;
		
		this.world = min.getWorld();
		this.min = min.clone();
		this.max = max.clone();
		
		this.maxPlayers = maxP;
		this.minPlayers = minP;
		
		this.running = false;
		
		players = new ArrayList<BowPlayer>();
		
		try {
			arenaFile.createNewFile();
			save();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public BowArena(File arenaFile){
		
		this.arenaFile = arenaFile;
		this.arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
		
		this.ID = arenaConfig.getInt("arena.ID");
		this.maxPlayers = arenaConfig.getInt("arena.maxP");
		this.minPlayers = arenaConfig.getInt("arena.minP");
		
		this.world = Bukkit.getWorld(UUID.fromString(arenaConfig.getString("arena.Location.world")));
		this.max = new Location(world, 
				arenaConfig.getDouble("arena.Location.max.X"),
				arenaConfig.getDouble("arena.Location.max.Y"),
				arenaConfig.getDouble("arena.Location.max.Z"));
		this.min = new Location(world, 
				arenaConfig.getDouble("arena.Location.min.X"),
				arenaConfig.getDouble("arena.Location.min.Y"),
				arenaConfig.getDouble("arena.Location.min.Z"));
		
		this.running = false;
		
		this.players = new ArrayList<BowPlayer>();		
	}
	
	
	private void save() throws IOException{
		
		arenaConfig.set("arena.ID", ID);
		arenaConfig.set("arena.maxP", maxPlayers);
		arenaConfig.set("arena.minP", minPlayers);
		
		arenaConfig.set("arena.Location.world", min.getWorld().getUID().toString());
		arenaConfig.set("arena.Location.min.X", min.getX());
		arenaConfig.set("arena.Location.min.Y", min.getY());
		arenaConfig.set("arena.Location.min.Z", min.getZ());
		arenaConfig.set("arena.Location.max.X", max.getX());
		arenaConfig.set("arena.Location.max.Y", max.getY());
		arenaConfig.set("arena.Location.max.Z", max.getZ());
		
		arenaConfig.save(arenaFile);
	}


	public int getID(){
		return ID;
	}
	
	public World getWorld(){
		return world;
	}
	
	public Location getMin(){
		return min.clone();
	}
	
	public Location getMax(){
		return max.clone();
	}
	
	public int getTaskID(){
		return TaskID;
	}
	
	
	public boolean isFull(){
		return !(players.size()<maxPlayers);
	}
	
	public boolean isRunning(){
		return running;
	}
	
	
	public boolean addPlayer(BowPlayer player){
		
		if(!isFull()){
			
		}
		return false;
	}
}
