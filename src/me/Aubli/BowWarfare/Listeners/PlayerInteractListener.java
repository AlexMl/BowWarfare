package me.Aubli.BowWarfare.Listeners;

import me.Aubli.BowWarfare.GameManager;

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
				if(sign.getLine(0).equalsIgnoreCase("[bw]")){
					if(eventPlayer.hasPermission("bw.play")){
						if(!GameManager.getManager().isInGame(eventPlayer)){
							if(GameManager.getManager().getArena(Integer.parseInt(sign.getLine(1)))!=null){
								GameManager.getManager().createPlayer(eventPlayer, GameManager.getManager().getArena(Integer.parseInt(sign.getLine(1))));
								return;
							}	
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
			}
		}
	}
	
}
