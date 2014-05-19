package me.Aubli.BowWarfare.Listeners;

import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Game.BowArena;
import me.Aubli.BowWarfare.Game.BowPlayer;
import me.Aubli.BowWarfare.Game.GameManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener{

	private GameManager gm = GameManager.getManager();
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event){
		
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Arrow){
			Player damagedPlayer = (Player)event.getEntity();
			Arrow a = (Arrow)event.getDamager();
			if(a.getShooter() instanceof Player){
				Player damager = (Player)a.getShooter();				
				if(gm.isInRunningGame(damagedPlayer) && gm.isInRunningGame(damager)){
					event.setCancelled(true);
					if(damager!=damagedPlayer){
						if(gm.getArena(damagedPlayer).equals(gm.getArena(damager))){
							BowArena arena = gm.getArena(damagedPlayer);
							BowPlayer killer = arena.getPlayer(damager);
							BowPlayer victim = arena.getPlayer(damagedPlayer);
							
							killer.addKill(victim);
							killer.sendMessage(BowWarfare.getPrefix() + ChatColor.DARK_GREEN + "You killed " + ChatColor.GOLD + victim.getName() + ChatColor.DARK_RED + "!!");
							killer.sendMessage(BowWarfare.getPrefix() + ChatColor.DARK_GREEN + "You have now " + ChatColor.DARK_PURPLE + killer.getKills() + ChatColor.DARK_GREEN + " Kills!!");
							
							victim.teleport(arena.getNewRandomStartLoc());
							victim.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "You were killed by " + ChatColor.GOLD + killer.getName() + ChatColor.DARK_RED + "!!");						
							return;
						}
					}
				}
			}
		}
		
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			Player damagedPlayer = (Player)event.getEntity();
			Player damager = (Player)event.getDamager();
			
			if(gm.isInGame(damagedPlayer) && gm.isInGame(damager)){
				event.setCancelled(true);
				return;
			}
		}
	}
}
