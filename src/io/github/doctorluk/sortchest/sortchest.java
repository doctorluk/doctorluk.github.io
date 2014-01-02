package io.github.doctorluk.sortchest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class sortchest extends JavaPlugin implements Listener {

	public Map<Player, Boolean> sortchestActive = new HashMap<Player, Boolean>();  // Is player currently using /swapchests?
	
	private void sortInventory(Chest chest, Player player){
		List<ItemStack> list = new ArrayList<ItemStack>();
		ItemStack[] inventory = chest.getInventory().getContents();
		ItemStack inv;
		int ii = 0;
		for(int i = 0; i < inventory.length; i++){
			inv = inventory[i];
			if(inv == null)
				continue;
			list.add(inv);
			ii++;
			getLogger().info("There is " + inv.getAmount() + " of " + inv.getType() + " in slot " + i);
			player.sendMessage("There is " + inv.getAmount() + " of " + inv.getType() + " in slot " + i);
		}
	}
	
	@Override
	public void onEnable() {
		for (Player player : this.getServer().getOnlinePlayers()) {
			sortchestActive.put(player, false);
		}

		// Register Events in this class
		getServer().getPluginManager().registerEvents(this, this);
	}
 
    @Override
    public void onDisable() {
    }
    
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
    	sortchestActive.put(event.getPlayer(), false);
    }
    
    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
    	Player player = event.getPlayer();
	    if(player != null){
	    	sortchestActive.remove(player);
    	}
    }
    
    @EventHandler
    public void onChestHit(PlayerInteractEvent event){
    	if( event.getAction() == Action.LEFT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST ) {
    		Chest chest = (Chest)event.getClickedBlock().getState();
    		Player player = event.getPlayer();
    		sortInventory(chest, player);
		}
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){    	
    	/*
         * Example command usage:
         * swapchests -> Start/Stop process of swapping contents of two chests
         */
    	if(cmd.getName().equalsIgnoreCase("sortchest")){
    		/*
    		if (sender instanceof Player) {
				Player player = (Player)sender;
				
				if(sortchestActive.get(player)){ // If player is already in there, remove him
					sortchestActive.put(player, false);

			    	
					player.sendMessage(ChatColor.RED + "sortchest stopped!");
					return true;
				}
				else{
					sortchestActive.put(player, true);
					player.sendMessage(ChatColor.BLUE + "swapchests is now active!");
					player.sendMessage(ChatColor.ITALIC + "Left click chests that should be sorted!\nDeactivate with /sortchest");
					return true;
				}    			
    		}
    		return false;
    		*/
    	}
    	return false; 
    }
    
}
