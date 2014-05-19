package me.Aubli.BowWarfare.Listeners;

import me.Aubli.BowWarfare.Game.BowArena;
import me.Aubli.BowWarfare.Game.BowPlayer;
import me.Aubli.BowWarfare.Game.GameManager;

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
				if(damager!=damagedPlayer){
					if(gm.isInRunningGame(damagedPlayer) && gm.isInRunningGame(damager)){
						event.setCancelled(true);
						
						if(gm.getArena(damagedPlayer).equals(gm.getArena(damager))){
							BowArena arena = gm.getArena(damagedPlayer);
							BowPlayer killer = arena.getPlayer(damager);
							BowPlayer victim = arena.getPlayer(damagedPlayer);
							
							killer.addKill(victim);
							killer.sendMessage("You killed " + victim.getName());
							killer.sendMessage("You have now " + killer.getKills() + " Kills!");
							
							victim.teleport(victim.getOriginStartLocation());
							victim.sendMessage("You were killed by " + killer.getName());						
							return;
						}
					}
				}
			}
		}
	}
}
