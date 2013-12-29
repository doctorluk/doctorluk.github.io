package io.github.doctorluk.firstplugin;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class firstplugin extends JavaPlugin {
	
	@Override
    public void onEnable(){
    }
 
    @Override
    public void onDisable() {
    }
    
    /*
     * Example command usage:
     * killradius [radius] -> Kills all animals within iRadius of the player's position
     * killradius chicken [radius] -> Kills all chickens within iRadius of the player's position 
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("killradius")){
    		if (sender instanceof Player) {
    			
    			if(args.length == 0) // No arguments specified
    				return false;
    			
    			if(args.length == 1 && args[0].matches("\\d+") ) // Arguments are radius only
    				return false;
    			
    			if(args.length == 2 && args[1].matches("\\d+") ){ // Arguments contain animal name + radius
    				Player player = (Player)sender;
    				double radius = Integer.getInteger(args[1])/2;
    				List <Entity> entities = player.getNearbyEntities(radius, radius, radius);
    				int count = 0;
    				for(Entity ent:entities){
    					if(ent instanceof Animals)
    						count++;
    				}
    				getLogger().info(count+"");
    				return false;
    			}
    			
    			if(args.length > 2)
    				return false;
    			
    		}
    		return true;
    	}
    	return false; 
    }
    
}
