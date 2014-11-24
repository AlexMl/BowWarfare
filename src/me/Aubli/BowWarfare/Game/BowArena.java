package me.Aubli.BowWarfare.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Game.GameManager.ArenaStatus;
import me.Aubli.BowWarfare.Sign.SignManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.util.SortMap.SortMap;

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
	
	public Location getNewRandomStartLoc(){
		
		int x;
		int y;
		int z;
		
		Random rand = new Random();		
		x = rand.nextInt((getMax().getBlockX()-getMin().getBlockX()-1)) + getMin().getBlockX() + 1;
		y = getMin().getBlockY();
		z = rand.nextInt((getMax().getBlockZ()-getMin().getBlockZ()-1)) + getMin().getBlockZ() + 1;
		
		Location startLoc = new Location(getWorld(), x, y, z);
		
		if(containsLocation(startLoc)){
			return startLoc.clone();
		}else{
			return getNewRandomStartLoc();
		}
	}
	
	public int getTaskID(){
		return TaskID;
	}
	
	public ArenaStatus getStatus(){
		return status;
	}
	
	public BowPlayer getPlayer(Player player){
		if(this.containsPlayer(player)){
			for(BowPlayer p : getPlayers()){
				if(p.getPlayer().getUniqueId()==player.getUniqueId()){
					return p;
				}
			}
		}
		return null;
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
		SignManager.getManager().updateSign(SignManager.getManager().getSign(this));
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
		return ((getMax().getX()>=location.getX() && getMin().getX()<=location.getX()) && (getMax().getZ()>=location.getZ() && getMin().getZ()<=location.getZ()));
	}
	
	
	public boolean addPlayer(BowPlayer player){
		
		if(!isFull() && !isRunning() && !players.contains(player)){
			players.add(player);
			if(players.size()==minPlayers){
				GameManager.getManager().startArena(this);
			}
			try {
				player.setStartLocation(getNewRandomStartLoc());
				player.getReady();
			} catch (Exception e) {				
				e.printStackTrace();
				return false;
			}
			BowWarfare.getPluginLog().log(Level.INFO, "Player " + player.getName() + " joined arena " + getID() + "!", true);
			return true;
		}
		return false;
	}
	
	public boolean removePlayer(BowPlayer player){		
		if(players.contains(player)){
			players.remove(player);
			player.reset();
			if(isRunning() && getPlayers().length==0){
				GameManager.getManager().restartArena(this);
			}
			return true;
		}
		return false;		
	}
	
	
	public void sendMessage(String message){
		for(BowPlayer p : getPlayers()){
			p.sendMessage(message);
		}
		BowWarfare.getPluginLog().log(Level.FINEST, "[Message Arena " + getID() + "] " + message, true);
	}
	
	void start(){
		TaskID = new GameRunnable(this).runTaskTimer(BowWarfare.getInstance(), 0L, 20L).getTaskId();
		BowWarfare.getPluginLog().log(Level.INFO, "Arena " + getID() + " started!", true);
	}
	
	void stop(){
			
		if(isRunning() && getPlayers().length>0){
			Map<String, Integer> playerKills = new HashMap<String, Integer>();
			
			for(BowPlayer p : getPlayers()){
				playerKills.put(p.getUuid().toString(), p.getKills());
			}
			
			BowWarfare.getPluginLog().log(Level.FINER, playerKills.toString(), true);			
			playerKills = SortMap.sortByValue(playerKills);		
			BowWarfare.getPluginLog().log(Level.FINER, playerKills.toString(), true);
			
			Player winner = Bukkit.getPlayer(UUID.fromString(playerKills.entrySet().toArray()[playerKills.size()-1].toString().split("=")[0]));
					
			winner.sendMessage(BowWarfare.getPrefix() + ChatColor.GOLD  + "You have won!! " + ChatColor.BOLD + "Congrats!");
			
			removePlayer(getPlayer(winner));			
			sendMessage(BowWarfare.getPrefix() + ChatColor.GOLD + "Player " + ChatColor.BLUE + winner.getName() + ChatColor.GOLD + " has won!! " + ChatColor.BOLD + "Congrats!");
		}		
		
		if(Bukkit.getScheduler().isCurrentlyRunning(getTaskID())){
			setStatus(ArenaStatus.SUSPEND);
			Bukkit.getScheduler().runTaskLater(BowWarfare.getInstance(), new Runnable() {			
				@Override
				public void run() {
					BowArena.this.setStatus(ArenaStatus.WAITING);				
				}
			}, 5*20L);
		}else {
			setStatus(ArenaStatus.WAITING);
		}
		
		for(BowPlayer p : players){
			p.reset();
		}
		players.clear();		
		
		Bukkit.getScheduler().cancelTask(getTaskID());
		BowWarfare.getPluginLog().log(Level.INFO, "Arena " + getID() + " stopped with no errors!", true);
	}	
	
	public void restart(){
		stop();
		
		setStatus(ArenaStatus.SUSPEND);
		Bukkit.getScheduler().runTaskLater(BowWarfare.getInstance(), new Runnable() {			
			@Override
			public void run() {
				BowArena.this.setStatus(ArenaStatus.WAITING);				
			}
		}, 5*20L);
	}
	
	
	@Override
	public boolean equals(Object arena){
		if(arena instanceof BowArena){
			BowArena a = (BowArena) arena;
			if(a.getID() == this.ID && a.getWorld().equals(this.getWorld())){
				return true;
			}			
		}
		return false;
	}
	
}
