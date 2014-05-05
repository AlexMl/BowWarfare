package me.Aubli.BowWarfare;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BowExecuter implements CommandExecutor {

	/*
	 *	Commands: 
	 * 	/bw arena pos1
	 *  /bw arena pos2
	 *   
	 * 
	 */
	
	private HashMap<Integer, Location> pos = new HashMap<Integer, Location>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)){			
			if(cmd.getName().equalsIgnoreCase("bw")){
				if(args.length==1){
					if(args[0].equalsIgnoreCase("stop")){
						GameManager.getManager().stopArenas();
						sender.sendMessage("All games are stoped!");
						return true;
					}
					if(args[0].equalsIgnoreCase("reload")){
						GameManager.getManager().reload();
						sender.sendMessage("Config reloaded!");
						return true;
					}
				}
				if(args.length==2){
					if(args[0].equalsIgnoreCase("stop")){
						BowArena a = GameManager.getManager().getArena(Integer.parseInt(args[1]));
						if(a!=null){
							a.stop();
							sender.sendMessage("Arena " + args[1] + " stoped!");
							return true;
						}else{
							sender.sendMessage("Arena " + args[1] + " is not available!");
							return true;
						}
					}
				}			
			}
			return true;
		}		
		
		Player playerSender = (Player)sender;
		
		if(cmd.getName().equalsIgnoreCase("bw")){
			
			
			
			if(args.length==2){
				if(playerSender.hasPermission("bw.admin")){
					if(args[0].equalsIgnoreCase("arena")){					
						if(args[1].equalsIgnoreCase("pos1")){
							pos.put(1, playerSender.getLocation().clone());
							playerSender.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Position 1 saved!");
						}
						if(args[1].equalsIgnoreCase("pos2")){
							pos.put(2, playerSender.getLocation().clone());
							playerSender.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Position 2 saved!");
						}
						
						if(pos.containsKey(1) && pos.containsKey(2)){
							boolean success = GameManager.getManager().createArena(pos.get(1), pos.get(2));
							pos.clear();
							
							if(success){
								playerSender.sendMessage(ChatColor.GREEN + "Arena created!");
								return true;
							}else{
								playerSender.sendMessage(ChatColor.RED + "Job failed!");
								return true;
							}							
						}					
					}
					if(args[0].equalsIgnoreCase("remove")){
						BowArena a = GameManager.getManager().getArena(Integer.parseInt(args[1]));
						if(a!=null){
							boolean success = GameManager.getManager().removeArena(a);
							if(success){
								playerSender.sendMessage(ChatColor.GREEN + "Arena removed!");
								return true;
							}else{
								playerSender.sendMessage(ChatColor.RED + "Job failed!");
								return true;
							}	
						}else{
							playerSender.sendMessage(ChatColor.RED + "Arena is not available!");
							return true;
						}
					}
				}else{
					playerSender.sendMessage(ChatColor.DARK_RED + "You do not have permissions for that!");
					return true;
				}				
			}			
		}
		return true;
	}

}
