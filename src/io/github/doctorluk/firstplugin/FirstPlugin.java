package io.github.doctorluk.firstplugin;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FirstPlugin extends JavaPlugin implements Listener {

	@Override
    public void onEnable(){

    }
 
    @Override
    public void onDisable() {
    }
    
    private boolean isAnimal(String animal){
    	if(animal.equals( "CHICKEN" ))
    		return true;
    	if(animal.equals( "COW" ))
    		return true;
    	if(animal.equals( "HORSE" ))
    		return true;
    	if(animal.equals( "MUSHROOM_COW" ))
    		return true;
    	if(animal.equals( "OCELOT" ))
    		return true;
    	if(animal.equals( "PIG" ))
    		return true;
    	if(animal.equals( "SHEEP" ))
    		return true;
    	if(animal.equals( "WOLF" ))
    		return true;
    	return false;
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
    			
    			if(args.length == 1 && args[0].matches("\\d+") ){ // Arguments are radius only
    				Player player = (Player)sender;
    				
    				double radius = Double.parseDouble(args[0])/2;
    				List <Entity> entities = player.getNearbyEntities(radius, radius, radius);
    				int count = 0;
    				for(Entity ent:entities){
    					if(!(ent instanceof Animals))
    						continue;
						count++;
						Animals ani = (Animals)ent;
						ani.remove();
    				}
    				getServer().broadcastMessage("Killed " + count + " animals.");
    				return true;
    			}
    			
    			if(args.length == 2 && args[1].matches("\\d+") ){ // Arguments contain animal name + radius
    				Player player = (Player)sender;
    				
    				String animal = args[0].toUpperCase();
    				if(!isAnimal(animal))
    					return false;
    				
    				double radius = Double.parseDouble(args[1])/2;
    				List <Entity> entities = player.getNearbyEntities(radius, radius, radius);
    				int count = 0;
    				for(Entity ent:entities){
    					if( ent.getType() == EntityType.valueOf(animal) ){
    						count++;
    						Animals ani = (Animals)ent;
    						ani.remove();
    					}
    				}
    				getServer().broadcastMessage("Killed " + count + " of type " + args[0]);
    				return true;
    			}
    			
    			return false; // In case no arguments match
    			
    		}
    		else
    			getLogger().info("This command can only be run as a player on the server!");
    		return false;
    	}
    	return false; 
    }
    
}
