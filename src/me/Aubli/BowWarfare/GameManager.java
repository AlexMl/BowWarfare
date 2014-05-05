package me.Aubli.BowWarfare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameManager {
	
	private File arenaFolder;
	
	private ArrayList<BowArena> arenas;
	
	private static GameManager instance;
	
	public enum ArenaStatus{
		WAITING,
		RUNNING,
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
		save();
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
	
	
	public boolean createArena(Location min, Location max){
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
			return true;
		}
		return false;
	}
	
	public boolean removeArena(BowArena arena){
		arena.delete();
		return arenas.remove(arena);
	}
	
	
	public void createPlayer(Player p, BowArena arena){
		
		BowPlayer player = new BowPlayer(p, arena, p.getLocation().clone());
		
		arena.addPlayer(player);
		
	}
	
	public void startArena(BowArena arena){
		arena.start();
	}
	
	public void stopArena(BowArena arena){
		arena.stop();
	}
	
	public void stopArenas(){
		for(BowArena a : arenas){
			a.stop();
		}
	}
}
