package me.Aubli.BowWarfare;

import java.util.HashMap;
import java.util.Map;

import me.Aubli.BowWarfare.Game.BowArena;
import me.Aubli.BowWarfare.Game.GameManager;

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
	 *  /bw help
	 *  /bw list
	 *
	 * 	/bw arena pos1
	 *  /bw arena pos2
	 *   
	 *  /bw remove [arenaID]
	 *  
	 *  /bw start [ID]
	 *  
	 *  /bw stop [ID]
	 *  /bw stop
	 */
	
	private HashMap<Integer, Location> pos = new HashMap<Integer, Location>();
	private GameManager gm = GameManager.getManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//Console commands
		if(!(sender instanceof Player)){
			if(cmd.getName().equalsIgnoreCase("bw")){
				if(args.length==1){
					if(args[0].equalsIgnoreCase("stop")){
						gm.restartArenas();
						sender.sendMessage("[BW] All games are stoped!");
						return true;
					}
					if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
						gm.reload();
						sender.sendMessage("[BW] Config reloaded!");
						return true;
					}
				}
				if(args.length==2){
					if(args[0].equalsIgnoreCase("stop")){
						BowArena a = gm.getArena(Integer.parseInt(args[1]));
						if(a!=null){
							gm.restartArena(a);
							sender.sendMessage("[BW] Arena " + args[1] + " stoped!");
							return true;
						}else{
							sender.sendMessage("[BW] Arena " + args[1] + " is not available!");
							return true;
						}
					}
				}			
			}
			return true;
		}		
		
		
		//Player commands		
		Player playerSender = (Player)sender;
		
		if(cmd.getName().equalsIgnoreCase("bw")){
			
			if(args.length==1){
				if(args[0].equalsIgnoreCase("help")){
					printCommands(playerSender);
					return true;
				}
				if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
					if(playerSender.hasPermission("bw.reload")){
						gm.reload();
						playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.GREEN + "Config reloaded!");
						return true;
					}else{
						commandDenied(playerSender);
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("list")){
					if(playerSender.hasPermission("bw.list")){
						String version = BowWarfare.getInstance().getDescription().getVersion();
						String name = BowWarfare.getInstance().getDescription().getName();						
						
						playerSender.sendMessage(ChatColor.DARK_GREEN + "\n|----------- " + ChatColor.GRAY + name + " v" + version + ChatColor.DARK_GREEN + " -----------");
						
						Map<BowArena, Integer> arenas = new HashMap<BowArena, Integer>();						
						for(BowArena a : gm.getArenas()){
							arenas.put(a, a.getID());
						}						
						arenas = BowWarfare.getGameAPI().sortMap(arenas);	
						
						for(BowArena a : arenas.keySet()){
							playerSender.sendMessage(ChatColor.DARK_GREEN + "| Arena-ID:" + ChatColor.DARK_GRAY + a.getID() + ChatColor.DARK_GREEN + " Status:" + ChatColor.DARK_GRAY + a.getStatus().toString() + ChatColor.DARK_GREEN + " Player:"  + ChatColor.DARK_GRAY + a.getPlayers().length);
						}
						
						return true;
					}else{
						commandDenied(playerSender);
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("leave")){
					if(playerSender.hasPermission("bw.play")){
						boolean success = gm.removePlayer(playerSender);
						
						if(success){
							playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.GREEN + "You have left the game!");
							return true;
						}else{
							playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "You are not in a Game!");
							return true;
						}						
					}else{
						commandDenied(playerSender);
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("stop")){
					if(playerSender.hasPermission("bw.stop.all")){
						gm.restartArenas();
						playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.DARK_GRAY + "[BW] All Arenas were stoped!");
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
						BowArena a = gm.getArena(Integer.parseInt(args[1]));						
						if(a!=null){
							gm.restartArena(a);
							playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.DARK_GRAY + "[BW] Arena " + a.getID() + " halted!");
							return true;
						}else{
							playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "Arena is not available!");
							return true;
						}
					}else{
						commandDenied(playerSender);
						return true;
					}
				}				
				if(args[0].equalsIgnoreCase("start")){
					if(playerSender.hasPermission("bw.start")){
						BowArena a = gm.getArena(Integer.parseInt(args[1]));						
						if(a!=null){
							gm.startArena(a);
							return true;
						}else{
							playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "Arena is not available!");
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
							playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Position 1 saved!");
						}
						if(args[1].equalsIgnoreCase("pos2")){
							pos.put(2, playerSender.getLocation().clone());
							playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Position 2 saved!");
						}
						
						if(pos.containsKey(1) && pos.containsKey(2)){
							BowArena arena = gm.createArena(pos.get(1), pos.get(2));
							pos.clear();
							
							if(arena!=null){
								playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.GREEN + "" + ChatColor.BOLD + "Arena " + arena.getID() + " created!");
								return true;
							}else{
								playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "Job failed!");
								return true;
							}							
						}
						return true;
					}
					if(args[0].equalsIgnoreCase("remove")){
						BowArena a = gm.getArena(Integer.parseInt(args[1]));
						if(a!=null){
							boolean success = gm.removeArena(a);
							if(success){
								playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.GREEN + "Arena removed!");
								return true;
							}else{
								playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "Job failed!");
								return true;
							}	
						}else{
							playerSender.sendMessage(BowWarfare.getPrefix() + ChatColor.RED + "Arena is not available!");
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
			
			player.sendMessage(ChatColor.DARK_GREEN + "|----------- " + ChatColor.GRAY + name + " v" + version + ChatColor.DARK_GREEN + " -----------");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw help");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw reload");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw list");
			player.sendMessage(ChatColor.DARK_GRAY + "|-------------------------------------");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw leave");
			player.sendMessage(ChatColor.DARK_GRAY + "|-------------------------------------");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw stop");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw stop [Arena-ID]");
			player.sendMessage(ChatColor.DARK_GRAY + "|-------------------------------------");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw arena [pos1|pos2]");
			player.sendMessage(ChatColor.DARK_GREEN + "| /bw remove [Arena-ID]");
		}else{
			commandDenied(player);
		}
	}
	
	public static void commandDenied(Player player){
		player.sendMessage(ChatColor.DARK_RED + "You do not have permissions for that!");
	}

}
