package me.Aubli.BowWarfare.Sign;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Location;

import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Game.BowArena;
import me.Aubli.BowWarfare.Game.GameManager;

public class SignManager {

	private static SignManager manager;
	
	private ArrayList<BowSign> signs;
	private File signFolder;
	
	public SignManager(){
		manager = this;
		
		load();
	}
	
	public static SignManager getManager(){
		return manager;
	}
	
	private void load(){
		signs = new ArrayList<BowSign>();
		signFolder = new File(BowWarfare.getInstance().getDataFolder().getPath() + "/signs");
		signFolder.mkdirs();
		
		for(File f : signFolder.listFiles()){
			BowSign sign = new BowSign(f);
			if(sign.getWorld()!=null){signs.add(sign);}
		}
	}
	
	
	public BowSign createSign(BowArena arena, Location location){
		BowSign sign = new BowSign(GameManager.getManager().getNewID(signFolder.getPath()), signFolder.getPath(), arena, location);
		signs.add(sign);
		return sign;
	}
	
	public void removeSign(BowSign sign){
		sign.delete();
		signs.remove(sign);
	}
	
	
	public void updateSign(BowSign sign){
		sign.update();
	}
	
	public void updatedSigns(){
		for(BowSign s : signs){
			s.update();
		}
	}
}
