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
	 *
	 *  /bw reload
	 *
	 * 	/bw arena pos1
	 *  /bw arena pos2
	 *   
	 *  /bw remove [arenaID]
	 *  
	 *  
	 *  
	 *  
	 *  /bw stop [ID]
	 *  /bw stop
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
			
			if(args.length==1){
				if(args[0].equalsIgnoreCase("help")){
					printCommands(playerSender);
					return true;
				}
				if(args[0].equalsIgnoreCase("reload")){
					if(playerSender.hasPermission("bw.reload")){
						GameManager.getManager().reload();
						playerSender.sendMessage(ChatColor.GREEN + "Config reloaded!");
						return true;
					}else{
						commandDenied(playerSender);
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("stop")){
					if(playerSender.hasPermission("bw.stop.all")){
						GameManager.getManager().stopArenas();
						playerSender.sendMessage(ChatColor.DARK_GRAY + "[BW] All Arenas were stoped!");
						return true;
					}else{
						commandDenied(playerSender);
						return true;
					}
				}
				
				printCommands(playerSender);
				return true;
			}
			
			
			if(args.length==2){
				
				if(args[0].equalsIgnoreCase("stop")){
					if(playerSender.hasPermission("bw.stop")){
						BowArena a = GameManager.getManager().getArena(Integer.parseInt(args[1]));						
						if(a!=null){
							GameManager.getManager().stopArena(a);
							playerSender.sendMessage(ChatColor.DARK_GRAY + "[BW] Arena " + a.getID() + " halted!");
							return true;
						}else{
							playerSender.sendMessage(ChatColor.RED + "Arena is not available!");
							return true;
						}
					}else{
						commandDenied(playerSender);
						return true;
					}
				}
				if(playerSender.hasPermission("bw.admin")){
					if(args[0].equalsIgnoreCase("arena")){	
						if(!args[1].equalsIgnoreCase("pos1") && !args[1].equalsIgnoreCase("pos2")){
							printCommands(playerSender);
							return true;
						}
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
						return true;
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
					printCommands(playerSender);
					return true;
				}else{
					commandDenied(playerSender);
					return true;
				}				
			}	
			printCommands(playerSender);
			return true;
		}
		return true;
	}
	
	
	private void printCommands(Player player){
		if(player.hasPermission("bw.help")){
			String version = BowWarfare.getInstance().getDescription().getVersion();
			String name = BowWarfare.getInstance().getDescription().getName();
			
			player.sendMessage(ChatColor.DARK_GREEN + "|–––––––––– " + ChatColor.DARK_GRAY + name + " v" + version + ChatColor.DARK_GREEN + " ––––––––––");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw reload");
			
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw stop");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw stop [Arena-ID]");
			
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw arena [pos1|pos2]");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw remove [Arena-ID]");
			
			
		}else{
			commandDenied(player);
		}
	}
	
	private void commandDenied(Player player){
		player.sendMessage(ChatColor.DARK_RED + "You do not have permissions for that!");
	}

}
