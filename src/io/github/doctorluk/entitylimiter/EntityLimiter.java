package io.github.doctorluk.entitylimiter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityLimiter extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
 
    @Override
    public void onDisable() {
    }
    
    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent evt){
    }
}
