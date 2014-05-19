package me.Aubli.BowWarfare.Game;

import me.Aubli.BowWarfare.Game.GameManager.ArenaStatus;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunnable extends BukkitRunnable{

	public GameRunnable(BowArena arena){
		this.arena = arena;
		this.game = GameManager.getManager();
	}
	
	private BowArena arena;
	private GameManager game;
	
	private int i = 1;
	
	private int countdown = 5;//BowWarfare.getCountdownTime();
	private int gameTime = 20;//BowWarfare.getGameTime();
	
	
	@Override
	public void run() {
		System.out.println("Arena: " + ChatColor.YELLOW + arena.getID() + " " + ChatColor.RED + i + " " + ChatColor.GREEN + arena.getStatus().toString());
		arena.sendMessage("Arena: " + ChatColor.YELLOW + arena.getID() + " " + ChatColor.RED + i + " " + ChatColor.GREEN + arena.getStatus().toString());
		
		if(i<countdown){		
			arena.setStatus(ArenaStatus.WAITING);
			arena.setCounter(countdown-i);
			
			
			
			
		}else if(i>=countdown && i<gameTime){
			arena.setStatus(ArenaStatus.RUNNING);
			arena.setCounter(gameTime-i);
		
			if(arena.getPlayers().length<2){
	//			game.stopArena(arena);
			}
			
			
		}else if(i>=gameTime){
			game.stopArena(arena);
		}
		i++;
	}

}
