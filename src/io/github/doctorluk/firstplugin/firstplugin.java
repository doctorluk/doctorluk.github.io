package io.github.doctorluk.firstplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class firstplugin extends JavaPlugin {
	
	@Override
    public void onEnable(){
		getLogger().info("onEnable has been invoked!");
    }
 
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("killradius")){
    		return true;
    	}
    	return false; 
    }
    
}
