package me.Aubli.BowWarfare.Sign;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import me.Aubli.BowWarfare.Game.BowArena;
import me.Aubli.BowWarfare.Game.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class BowSign {
	
	private File signFile;
	private FileConfiguration signConfig;

	private World world;
	private Location signLoc;
	private int signID;
	
	private BowArena arena;
	
	public BowSign(int ID, String signFolder, BowArena arena, Location signLoc){
		this.signID = ID;
		this.signLoc = signLoc.clone();
		this.world = signLoc.getWorld();
		this.arena = arena;
		
		signFile = new File(signFolder + "/" + ID + ".yml");
		signConfig = YamlConfiguration.loadConfiguration(signFile);
		try {
			save();
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}
	
	public BowSign(File signFile){
		this.signFile = signFile;
		this.signConfig = YamlConfiguration.loadConfiguration(signFile);
		
		this.signID = signConfig.getInt("sign.ID");
		this.arena = GameManager.getManager().getArena(signConfig.getInt("sign.arena"));
		
		this.world = Bukkit.getWorld(UUID.fromString(signConfig.getString("sign.location.world")));
		this.signLoc = new Location(world, 
				signConfig.getInt("sign.location.X"), 
				signConfig.getInt("sign.location.Y"),
				signConfig.getInt("sign.location.Z"));
		
	}
	
	private void save() throws IOException{
		if(!signFile.exists()){
			signFile.createNewFile();
		}
		
		signConfig.set("sign.ID", signID);
		signConfig.set("sign.arena", arena.getID());
		
		signConfig.set("sign.location.world", world.getUID().toString());
		signConfig.set("sign.location.X", signLoc.getBlockX());
		signConfig.set("sign.location.Y", signLoc.getBlockY());
		signConfig.set("sign.location.Z", signLoc.getBlockZ());
		
		signConfig.save(signFile);
	}
}
