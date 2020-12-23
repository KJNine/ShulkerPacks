package me.darkolythe.shulkerpacks;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class GriefPreventionListener implements Listener {

	private ShulkerPacks main;
	
    public GriefPreventionListener(ShulkerPacks plugin) {
        this.main = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onShulkerPack(ShulkerPackOpenEvent event) {
    	Location loc = event.getPlayer().getLocation();
    	
    	if(main.openonlyinclaim) {
    		Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, false, false, 
    				GriefPrevention.instance.dataStore.getPlayerData(event.getPlayer().getUniqueId()).lastClaim);
    		if(claim != null && 
    				(claim.allowBuild(event.getPlayer(), event.getShulkerBox().getType()) != null
    					|| claim.allowContainers(event.getPlayer()) != null)) {
    			event.setCancelled(false);
    		}
    	}
    			
    }

}
