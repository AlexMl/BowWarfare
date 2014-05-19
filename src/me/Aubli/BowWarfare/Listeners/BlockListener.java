package me.Aubli.BowWarfare.Listeners;

import me.Aubli.BowWarfare.BowExecuter;
import me.Aubli.BowWarfare.BowWarfare;
import me.Aubli.BowWarfare.Game.GameManager;
import me.Aubli.BowWarfare.Sign.SignManager;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

	private GameManager gm = GameManager.getManager();
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		Player eventPlayer = event.getPlayer();
		
		if(gm.isInGame(eventPlayer)){
			event.setCancelled(true);
			return;
		}
		
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Player eventPlayer = event.getPlayer();
		
		if(gm.isInGame(eventPlayer)){
			event.setCancelled(true);
			return;
		}
		
		if(event.getBlock().getState() instanceof Sign){
			Sign sign = (Sign)event.getBlock().getState();
			if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("bow warfare")){
				if(eventPlayer.hasPermission("bw.admin")){
					if(SignManager.getManager().getSign(event.getBlock().getLocation())!=null){
						SignManager.getManager().removeSign(SignManager.getManager().getSign(event.getBlock().getLocation()));
						eventPlayer.sendMessage(BowWarfare.getPrefix() + ChatColor.GREEN + "Sign removed!");
						return;
					}
				}else{
					BowExecuter.commandDenied(eventPlayer);
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
