package org.util.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginOutput {

	private final Logger log;
	
	private File logFile;
	
	private boolean debugMode;
	private int loglevel;
	
	public PluginOutput(JavaPlugin plugin, boolean enableDebug, int minLevel) {
		
		log = plugin.getLogger();
		
		this.debugMode = enableDebug;
		
		this.loglevel = minLevel;
		
		this.logFile = new File(plugin.getDataFolder().getAbsolutePath() + "/pluginlog.info");
		
		try {
			this.logFile.createNewFile();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	
	}
	
	
	public boolean isDebugMode() {
		return debugMode;
	}
	
	public void log(String message) {
		log(Level.INFO, message, false);
	}
	
	public void log(Level level, String message, boolean debugMessage) {
		log(level, message, debugMessage, null);		
	}
	
	public void log(Level level, String message, boolean debugMessage, Exception e) {
		
		if(!debugMessage) {
			if(level.intValue()>=Level.INFO.intValue()) {
				log.log(level, message);
				return;
			}
			log.info("[" + level.getName() + "] " + message);
			return;
		}		
		
		if(debugMessage) {
			logData(level, message, e);
			
			if(debugMode && level.intValue()>=this.loglevel) {
				if(level.intValue()>=Level.INFO.intValue()) {
					log.log(level, message);
					return;
				}
				log.info("[" + level.getName() + "] " + message);
				return;
			}
		}
	}
	
	
	public void logData(Level level, String message, Exception exception){
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	    Date currentTime = new Date();	    
	  
		try {
			FileWriter writer = new FileWriter(logFile, true);
			
			if(exception!=null) {
				message += "\n" + exception.toString();
			}
			
			writer.write("[" + formatter.format(currentTime) + "] [" + level.getName() + "] " + message);			
			writer.write(System.getProperty("line.separator"));
			writer.flush();
			writer.close();
			
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
	
}
