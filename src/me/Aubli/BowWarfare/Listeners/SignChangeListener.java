package me.Aubli.BowWarfare.Listeners;

import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Game.BowArena;
import me.Aubli.BowWarfare.Game.GameManager;
import me.Aubli.BowWarfare.Sign.SignManager;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent event){
		
		if(event.getLine(0).equalsIgnoreCase("[bw]")){
			if(!event.getLine(1).isEmpty()){
				if(event.getPlayer().hasPermission("bw.admin")){
					try{
						int arenaID = Integer.parseInt(event.getLine(1));
						BowArena arena = GameManager.getManager().getArena(arenaID);
						if(arena!=null){
							SignManager.getManager().createSign(arena, event.getBlock().getLocation());
							
							event.setLine(0, ChatColor.DARK_BLUE.toString() + "Bow Warfare");
							event.setLine(1, ChatColor.LIGHT_PURPLE + "[JOIN]");
							event.setLine(2, ChatColor.GREEN + "" + arena.getPlayers().length + " / " + BowWarfare.getMaxPlayers());
							event.setLine(3, ChatColor.DARK_RED + "Arena " + arena.getID());
								
							event.getPlayer().sendMessage(ChatColor.GREEN + "Sign Created!");
							return;
						}else{
							event.getPlayer().sendMessage(ChatColor.RED + "This arena is not available!");
							event.setCancelled(true);
							return;
						}
					}catch(NumberFormatException e){
						event.getPlayer().sendMessage(ChatColor.RED + "Only numbers are acceptable!");
						event.setCancelled(true);
						return;
					}
				}else{
					event.getPlayer().sendMessage(ChatColor.DARK_RED + "You do not have permissions for that!");
					event.setCancelled(true);
					return;
				}	
			}else{
				event.getPlayer().sendMessage(ChatColor.RED + "Line 2 has to be 'join'\nLine 3 has to be the arena id!");
				event.setCancelled(true);
				return;
			}
		}		
	}
}
