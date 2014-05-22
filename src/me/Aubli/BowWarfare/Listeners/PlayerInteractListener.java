package me.Aubli.BowWarfare.Listeners;

import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Game.GameManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerInteractListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent event){
		Player eventPlayer = event.getPlayer();
		
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(event.getClickedBlock().getState() instanceof Sign){
				Sign sign = (Sign)event.getClickedBlock().getState();				
				if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("Bow Warfare")){
					if(eventPlayer.hasPermission("bw.play")){
						if(ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[join]")){
							if(!GameManager.getManager().isInGame(eventPlayer)){								
								if(GameManager.getManager().getArena(Integer.parseInt(ChatColor.stripColor(sign.getLine(3)).split("Arena ")[1]))!=null){
									GameManager.getManager().createPlayer(eventPlayer, GameManager.getManager().getArena(Integer.parseInt(ChatColor.stripColor(sign.getLine(3)).split("Arena ")[1])));
									return;
								}	
							}else{
								eventPlayer.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "You are already in a game!");
								event.setCancelled(true);
								return;
							}
						}else{							
							eventPlayer.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "You can't join this game!");
							event.setCancelled(true);
							return;
						}
					}
				}				
			}		
		}
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){			
			if(GameManager.getManager().isInGame(eventPlayer)){		
				event.setCancelled(true);
				if(GameManager.getManager().isInRunningGame(eventPlayer)){
					if(eventPlayer.getItemInHand().getType()==Material.BOW){
						Vector v = eventPlayer.getEyeLocation().getDirection().multiply(3);
						eventPlayer.launchProjectile(Arrow.class, v);
					}
				}
				return;
			}
		}
	}
	
}
