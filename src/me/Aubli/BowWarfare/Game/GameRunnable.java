package me.Aubli.BowWarfare.Game;

import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Game.GameManager.ArenaStatus;

import org.bukkit.scheduler.BukkitRunnable;

public class GameRunnable extends BukkitRunnable{

	public GameRunnable(BowArena arena){
		this.arena = arena;
		this.game = GameManager.getManager();
	}
	
	private BowArena arena;
	private GameManager game;
	
	private int i = 1;
	
	private int countdown = BowWarfare.getCountdownTime();
	private int gameTime = BowWarfare.getGameTime();
	
	
	@Override
	public void run() {
	//	System.out.println("Arena: " + arena.getID() + " " + i + " " + arena.getStatus().toString());
	//	arena.sendMessage("Arena: " + ChatColor.YELLOW + arena.getID() + " " + ChatColor.RED + i + " " + ChatColor.GREEN + arena.getStatus().toString());
		
		if(i<countdown){		
			arena.setStatus(ArenaStatus.WAITING);
			arena.setCounter(countdown-i);
			
		}else if(i>=countdown && i<(gameTime+countdown)){
			arena.setStatus(ArenaStatus.RUNNING);
			arena.setCounter((countdown+gameTime)-i);
		
			if(arena.getPlayers().length<2){
				game.stopArena(arena);
			}
			
			
		}else if(i>=(gameTime+countdown)){
			game.stopArena(arena);
		}
		i++;
	}

}
