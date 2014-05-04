package me.Aubli.BowWarfare;

import java.util.UUID;

import org.bukkit.entity.Player;

public class BowPlayer {

	private Player player;
	private UUID uuid;
	
	private int kills;
	
	private boolean dead;
	
	private BowArena arena;
	
	public BowPlayer(Player player, BowArena arena){
		this.player = player;
		this.uuid = player.getUniqueId();
		
		this.kills = 0;
		this.dead = false;
		
		this.arena = arena;	
	}
	
	
	public UUID getUuid(){
		return uuid;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public int getKills(){
		return kills;
	}
	
	public BowArena getArena(){
		return arena;
	}
	
	
	public boolean isDead(){
		return dead;
	}
	
	
	public void reset(){
		
	}
	
	public void getReady(){
		
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
