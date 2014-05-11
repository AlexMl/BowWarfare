package me.Aubli.BowWarfare.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Game.GameManager.ArenaStatus;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
	
	private ArenaStatus status;
	
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
		
		status = ArenaStatus.WAITING;
		
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
		
		status = ArenaStatus.WAITING;
		
		this.players = new ArrayList<BowPlayer>();		
	}
	
	
	void save() throws IOException{
		
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
	
	void delete(){
		this.arenaFile.delete();
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
	
	public ArenaStatus getStatus(){
		return status;
	}
	
	public BowPlayer[] getPlayers(){
		BowPlayer[] p = new BowPlayer[players.size()];
		
		for(int i=0;i<players.size();i++){
			p[i] = players.get(i);
		}
		return p;
	}
	
	
	public void setStatus(ArenaStatus s){
		this.status = s;
	}
	
	public void setCounter(int second){
		for(BowPlayer p : getPlayers()){
			p.setXPLevel(second);
		}
	}
	
	
	
	public boolean isFull(){
		return !(players.size()<maxPlayers);
	}
	
	public boolean isRunning(){
		return getStatus()==ArenaStatus.RUNNING;
	}
	
	
	public boolean containsPlayer(Player player){
		for(BowPlayer p : players){
			if(p.getPlayer().getUniqueId()==player.getUniqueId()){
				return true;
			}
		}
		return false;
	}
	
	public boolean containsLocation(Location location){
		return ((getMax().getX()>location.getX() && getMin().getX()<location.getX()) && (getMax().getZ()>location.getZ() && getMin().getZ()<location.getZ()));
	}
	
	
	public boolean addPlayer(BowPlayer player){
		
		if(!isFull() && !isRunning() && !players.contains(player)){
			players.add(player);
			if(players.size()>=minPlayers){
				GameManager.getManager().startArena(this);
			}
			try {
				player.setStartLocation(getMin());
				player.getReady();
			} catch (Exception e) {				
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public void sendMessage(String message){
		for(BowPlayer p : getPlayers()){
			p.sendMessage(message);
		}
	}
	
	void start(){
		TaskID = new GameRunnable(this).runTaskTimer(BowWarfare.getInstance(), 20L, 20L).getTaskId();
	}
	
	void stop(){
		
		for(BowPlayer p : players){
			p.reset();
		}
		players.clear();
		
		Bukkit.getScheduler().cancelTask(getTaskID());
	}
}
