package me.darkolythe.shulkerpacks;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;

public class WorldGuardListener implements Listener {

	private ShulkerPacks main;
	
	private RegionContainer regionContainer;
	
    public WorldGuardListener(ShulkerPacks plugin) {
        this.main = plugin;
        
        regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onShulkerPack(ShulkerPackOpenEvent event) {
    	Location loc = event.getPlayer().getLocation();
    	
    	if(main.openonlyinclaim) {
    		ApplicableRegionSet arg = regionContainer.get(BukkitAdapter.adapt(loc.getWorld()))
            		.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
    		LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(event.getPlayer());
    		if(!arg.testState(lp, Flags.BUILD, Flags.CHEST_ACCESS)) {
    			event.setCancelled(false);
    		}
    	}
    			
    }
	
}
