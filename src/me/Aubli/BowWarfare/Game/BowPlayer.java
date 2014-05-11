package me.Aubli.BowWarfare.Game;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BowPlayer {

	private Player player;
	private UUID uuid;
	
	private int kills;
	
	private boolean dead;
	
	private BowArena arena;
	
	private ItemStack[] content;
	private ItemStack[] armor;
	
	private int totalXP;
	private GameMode mode;
	
	private Location startLoc;
	private Location clickedLoc;
	
	public BowPlayer(Player player, BowArena arena, Location playerLoc){
		this.player = player;
		this.uuid = player.getUniqueId();
		
		this.kills = 0;
		this.dead = false;
		
		this.arena = arena;	
		
		this.clickedLoc = playerLoc.clone();
		
		this.content = getPlayer().getInventory().getContents();
		this.armor = getPlayer().getInventory().getArmorContents();
		
		this.totalXP = getPlayer().getTotalExperience();
		this.mode = getPlayer().getGameMode();
	}
	
	
	public UUID getUuid(){
		return uuid;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getName(){
		return getPlayer().getName();
	}
	
	public Location getLocation(){
		return player.getLocation();
	}
	
	public Location getStartLocation(){
		return startLoc;
	}
	
	public int getKills(){
		return kills;
	}
	
	public BowArena getArena(){
		return arena;
	}
	
	
	public void setStartLocation(Location location){
		this.startLoc = location.clone();
	}
	
	public void setXPLevel(int level){
		getPlayer().setLevel(level);
	}
	
	public void sendMessage(String message){
		getPlayer().sendMessage(message);
	}
	
	public boolean isDead(){
		return dead;
	}
	
	
	@SuppressWarnings("deprecation")
	public void reset(){		
		player.getInventory().clear();
		
		player.teleport(clickedLoc, TeleportCause.PLUGIN);
		player.setVelocity(new Vector(0, 0, 0));
		
		player.setHealth(20D);
		player.setFoodLevel(20);
		
		player.setGameMode(mode);
		player.setTotalExperience(totalXP);
		
		player.getInventory().setArmorContents(armor);		
		player.getInventory().setContents(content);		
		
		player.updateInventory();
	}
	
	@SuppressWarnings("deprecation")
	public void getReady() throws Exception{
		
		if(getStartLocation()!=null){
			
			player.getInventory().clear();
			player.getInventory().setHelmet(null);
			player.getInventory().setChestplate(null);
			player.getInventory().setLeggings(null);
			player.getInventory().setBoots(null);
			
			player.setTotalExperience(0);
			player.setLevel(0);
			player.setGameMode(GameMode.SURVIVAL);
			player.resetPlayerTime();
			player.resetPlayerWeather();
			
			player.setHealth(20D);
			player.setFoodLevel(20);
			player.resetMaxHealth();
			
			player.setFlying(false);
			player.setWalkSpeed((float) 0.2);
			player.setFlySpeed((float) 0.2);
			
			ItemStack bow = new ItemStack(Material.BOW);
			bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
			bow.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			
			player.getInventory().addItem(bow);
			player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
			
			player.updateInventory();
			
			player.teleport(getStartLocation(), TeleportCause.PLUGIN);
		}else{
			throw new Exception("Start Location is null!");
		}		
	}
	
	
	@Override
	public String toString(){
		return getName();
	}
	
	@Override
	public boolean equals(Object player){
		if(player instanceof BowPlayer){
			BowPlayer p = (BowPlayer) player;
			return p.getUuid().equals(this.getUuid());
		}	
		return false;	
	}
	
}
