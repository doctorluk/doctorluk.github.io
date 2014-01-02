package io.github.doctorluk.swapchests;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class swapchests extends JavaPlugin implements Listener {

	public Map<Player, Chest> chest1 = new HashMap<Player, Chest>(); // Player <-> 1. Chest
	public Map<Player, Chest> chest2 = new HashMap<Player, Chest>(); // Player <-> 2. Chest
	public Map<Player, Boolean> swapchestsActive = new HashMap<Player, Boolean>();  // Is player currently using /swapchests?
	
	@Override
	public void onEnable() {
		for (Player player : this.getServer().getOnlinePlayers()) {
			swapchestsActive.put(player, false);
		}

		// Register Events in this class
		getServer().getPluginManager().registerEvents(this, this);
	}
 
    @Override
    public void onDisable() {
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
    	swapchestsActive.put(event.getPlayer(), false);
    }
    
    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
	    if(player != null){
	    	swapchestsActive.remove(player);
	    	
	    	if(chest1.containsKey(player))
	    		chest1.remove(player);
	    	
	    	if(chest2.containsKey(player))
	    		chest2.remove(player);
    	}
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChestHit(PlayerInteractEvent event){
    	if( event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST ) {
    		Chest chest = (Chest)event.getClickedBlock().getState();
    		Player player = event.getPlayer();
    		
    		if(!chest1.containsKey(player) && swapchestsActive.get(player)){ // First chest selected
    			chest1.put(player, chest);
    			player.sendMessage(ChatColor.BLUE + "First chest selected!");
    			return;
    		}
    		
    		if(!chest2.containsKey(player) && swapchestsActive.get(player) && chest1.get(player).getLocation() != chest.getLocation()){ // Second chest selected
    			chest2.put(player, chest);
    			player.sendMessage(ChatColor.BLUE + "Second chest selected!");
    		}
    		
    		if(swapchestsActive.get(player)){
	    		if(chest1.get(player) != null && chest2.get(player) != null){
	    			Chest chestOne = chest1.get(player);
	    			Chest chestTwo = chest2.get(player);

	    			if( (chestOne.getBlock().getType() != Material.CHEST) ){
	    				player.sendMessage(ChatColor.RED + "The first chest was broken during the process!");
	    				
	    				swapchestsActive.put(player, false);
	    				
	    				if(chest1.containsKey(player))
	        				chest1.remove(player);
	        			
	        			if(chest2.containsKey(player))
	        				chest2.remove(player);
	        			
	        			return;
	    			}
	    			
	    			if(chestOne.getInventory().getSize() != chestTwo.getInventory().getSize()){
	    				player.sendMessage(ChatColor.RED + "Both chests have to be the same size!");
	    				
	    				swapchestsActive.put(player, false);
	    				
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
	    			
	    			swapchestsActive.put(player, false);
	    			player.sendMessage(ChatColor.GREEN + "Chest content swapped!");
	    			
	    		}
	    		else
	    			player.sendMessage(ChatColor.RED + "(swapchests) Something went wrong!?");
    		}
		}
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){    	
    	/*
         * Example command usage:
         * swapchests -> Start/Stop process of swapping contents of two chests
         */
    	if(cmd.getName().equalsIgnoreCase("swapchests")){
    		if (sender instanceof Player) {
				Player player = (Player)sender;
				
				if(swapchestsActive.get(player)){ // If player is already in there, remove him
					swapchestsActive.put(player, false);
					
					if(chest1.containsKey(player))
			    		chest1.remove(player);
			    	
			    	if(chest2.containsKey(player))
			    		chest2.remove(player);
			    	
					player.sendMessage(ChatColor.RED + "swapchests stopped!");
					return true;
				}
				else{
					swapchestsActive.put(player, true);
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
