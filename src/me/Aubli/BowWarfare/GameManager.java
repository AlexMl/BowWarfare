package me.Aubli.BowWarfare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameManager {
	
	private File arenaFolder;
	
	private ArrayList<BowArena> arenas;
	
	
	public GameManager(){
				
		arenaFolder = new File(BowWarfare.getInstance().getDataFolder().getPath() + "/Arenas");
		
		load();		
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
}
