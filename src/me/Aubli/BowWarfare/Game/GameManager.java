package me.Aubli.BowWarfare.Game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Sign.SignManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameManager {
	
	private File arenaFolder;
	
	private ArrayList<BowArena> arenas;
	
	private static GameManager instance;
	
	public enum ArenaStatus{
		WAITING,
		RUNNING,
		SUSPEND,
		;
	}
	
	
	public GameManager(){				
		instance = this;
		arenaFolder = new File(BowWarfare.getInstance().getDataFolder().getPath() + "/Arenas");		
		load();		
	}
	
	public static GameManager getManager(){
		return instance;
	}
	
	public void shutdown(){
		stopArenas();
		save();
	}
	
	public void reload(){
		restartArenas();
		load();
		SignManager.getManager().load();
	}
	
	private void load(){
		arenas = new ArrayList<BowArena>();
		
		if(!arenaFolder.exists()){
			arenaFolder.mkdirs();
		}
		
		for(File f : arenaFolder.listFiles()){
			BowArena a = new BowArena(f);
			if(a.getWorld()!=null){
				arenas.add(a);
			}
		}
	}
	
	private void save(){		
		try{
			for(BowArena a : arenas){
				a.save();
			}
			arenas.clear();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public int getNewID(String path){		
		File folder = new File(path);
		
		if(folder.listFiles().length==0){
			return 1;
		}else{			
			int[] fileIds = new int[folder.listFiles().length];
			
			for(int i=0;i<fileIds.length;i++){
				fileIds[i] = Integer.parseInt(folder.listFiles()[i].getName().split(".ym")[0]);
			}
			
			Arrays.sort(fileIds);
			
			for(int k=0;k<fileIds.length;k++){
				if(fileIds[k]!=(k+1)){
					return (k+1);
				}
			}
			return fileIds.length+1;
		}		
	}
	
	public BowArena getArena(int ID){
		
		for(BowArena a : arenas){
			if(a.getID()==ID){
				return a;
			}
		}
		return null;
	}
	
	public BowArena getArena(Player player){
		for(BowArena a : arenas){
			if(a.containsPlayer(player)){
				return a;
			}
		}
		return null;
	}
	
	public BowArena[] getArenas(){
		BowArena[] arenaArray = new BowArena[arenas.size()];
		
		for(int i=0;i<arenas.size();i++){
			arenaArray[i] = arenas.get(i);
		}
		return arenaArray;		
	}
	
	
	public boolean isInGame(Player player){
		for(BowArena a : arenas){			
			if(a.containsPlayer(player)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isInRunningGame(Player player){
		for(BowArena a : arenas){
			if(a.containsPlayer(player)){
				if(a.getStatus()==ArenaStatus.RUNNING){
					return true;
				}
			}
		}
		return false;
	}
	
	
	public BowArena createArena(Location min, Location max){
		if(min.getWorld().equals(max.getWorld())){
			
			double tempX;
			double tempY;		
			double tempZ;
			
			if(min.getX()>max.getX()){
				tempX = min.getX();
				min.setX(max.getX());
				max.setX(tempX);
			}
			
			if(min.getY()>max.getY()){
				tempY = min.getY();
				min.setY(max.getY());
				max.setY(tempY);
			}
			
			if(min.getZ()>max.getZ()){
				tempZ = min.getZ();
				min.setZ(max.getZ());
				max.setZ(tempZ);
			}			
			
			BowArena a = new BowArena(arenaFolder.getPath(), getNewID(arenaFolder.getPath()), min.clone(), max.clone(), BowWarfare.getMaxPlayers(), BowWarfare.getMinPlayers());
			arenas.add(a);
			return a;
		}
		return null;
	}
	
	public boolean removeArena(BowArena arena){
		arena.delete();
		return arenas.remove(arena);
	}
	
	
	public void createPlayer(Player p, BowArena arena){
		
		BowPlayer player = new BowPlayer(p, arena, p.getLocation().clone());
		arena.sendMessage(BowWarfare.getPrefix() + ChatColor.GREEN + "Player " + ChatColor.GOLD + player.getName() + ChatColor.GREEN + " has joined the Game!");
		player.sendMessage(BowWarfare.getPrefix() + ChatColor.GREEN + "You have joined the Game!");
		arena.addPlayer(player);		
		SignManager.getManager().updateSign(SignManager.getManager().getSign(arena));
	}
	
	public boolean removePlayer(Player player){		
		if(getArena(player)!=null){			
			BowArena arena = getArena(player);		
			boolean success = arena.removePlayer(arena.getPlayer(player));
			if(success){arena.sendMessage(BowWarfare.getPrefix() + ChatColor.GREEN + "Player " + player.getName() + " has left the Game!");}
			SignManager.getManager().updateSign(SignManager.getManager().getSign(arena));
			return success;
		}
		return false;
	}
	
	
	public void startArena(BowArena arena){
		arena.start();
	}
	
	public void stopArenas(){
		for(BowArena a : arenas){
			a.stop();
		}
	}
	
	public void restartArena(BowArena arena){
		arena.restart();
	}	
	
	public void restartArenas(){
		for(BowArena a : arenas){
			a.restart();
		}
	}
}
