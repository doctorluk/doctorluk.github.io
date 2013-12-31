package io.github.doctorluk.firstplugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class firstplugin extends JavaPlugin implements Listener {

	public Map<Player, Chest> chest1 = new HashMap<Player, Chest>(); // Player <-> 1. Chest
	public Map<Player, Chest> chest2 = new HashMap<Player, Chest>(); // Player <-> 2. Chest
	public Map<Player, Boolean> pluginActive = new HashMap<Player, Boolean>();  // Is player currently using /swapchests?
	
	@Override
    public void onEnable(){
		// Puts all already online players into pluginActive with value = false
		for (Player player : this.getServer().getOnlinePlayers()) {
			pluginActive.put(player, false);
		}
		// Register Events in this class
		getServer().getPluginManager().registerEvents(this, this);
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
    
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
    	pluginActive.put(event.getPlayer(), false);
    }
    
    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
	    if(player != null){
	    	pluginActive.remove(player);
	    	
	    	if(chest1.containsKey(player))
	    		chest1.remove(player);
	    	
	    	if(chest2.containsKey(player))
	    		chest2.remove(player);
    	}
    }
    
    @EventHandler
    public void onChestHit(PlayerInteractEvent event){
    	if( event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST ) {
    		Chest chest = (Chest)event.getClickedBlock().getState();
    		Player player = event.getPlayer();
    		
    		if(!chest1.containsKey(player) && pluginActive.get(player)){ // First chest selected
    			chest1.put(player, chest);
    			player.sendMessage(ChatColor.BLUE + "First chest selected!");
    			return;
    		}
    		
    		if(!chest2.containsKey(player) && pluginActive.get(player) && chest1.get(player).getLocation() != chest.getLocation()){ // Second chest selected
    			chest2.put(player, chest);
    			player.sendMessage(ChatColor.BLUE + "Second chest selected!");
    		}
    		
    		if(pluginActive.get(player)){
	    		if(chest1.get(player) != null && chest2.get(player) != null){
	    			Chest chestOne = chest1.get(player);
	    			Chest chestTwo = chest2.get(player);

	    			if( (chestOne.getBlock().getType() != Material.CHEST) ){
	    				player.sendMessage(ChatColor.RED + "The first chest was broken during the process!");
	    				
	    				pluginActive.put(player, false);
	    				
	    				if(chest1.containsKey(player))
	        				chest1.remove(player);
	        			
	        			if(chest2.containsKey(player))
	        				chest2.remove(player);
	        			
	        			return;
	    			}
	    			
	    			if(chestOne.getInventory().getSize() != chestTwo.getInventory().getSize()){
	    				player.sendMessage(ChatColor.RED + "Both chests have to be the same size!");
	    				
	    				pluginActive.put(player, false);
	    				
	    				if(chest1.containsKey(player))
	        				chest1.remove(player);
	        			
	        			if(chest2.containsKey(player))
	        				chest2.remove(player);
	        			
	        			return;
	    			}
	    			
	    			ItemStack[] one = chestOne.getInventory().getContents();
	    			ItemStack[] two = chestTwo.getInventory().getContents();
	    			
	    			chestOne.getInventory().setContents(two);
	    			chestTwo.getInventory().setContents(one);
	    			
	    			if(chest1.containsKey(player))
	    				chest1.remove(player);
	    			
	    			if(chest2.containsKey(player))
	    				chest2.remove(player);
	    			
	    			pluginActive.put(player, false);
	    			player.sendMessage(ChatColor.GREEN + "Chest content swapped!");
	    			
	    		}
	    		else
	    			player.sendMessage(ChatColor.RED + "(swapchests) Something went wrong!?");
    		}
		}
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
    	
    	/*
         * Example command usage:
         * swapchests -> Start/Stop process of swapping contents of two chests
         */
    	
    	if(cmd.getName().equalsIgnoreCase("swapchests")){
    		if (sender instanceof Player) {
				Player player = (Player)sender;
				
				if(pluginActive.get(player)){ // If player is already in there, remove him
					pluginActive.put(player, false);
					
					if(chest1.containsKey(player))
			    		chest1.remove(player);
			    	
			    	if(chest2.containsKey(player))
			    		chest2.remove(player);
			    	
					player.sendMessage(ChatColor.RED + "swapchests stopped!");
					return true;
				}
				else{
					pluginActive.put(player, true);
					player.sendMessage(ChatColor.BLUE + "swapchests is now active!");
					player.sendMessage(ChatColor.ITALIC + "Left click both to-swap chests!");
					return true;
				}    			
    		}
    		return false;
    	}
    	return false; 
    }
    
}
