package sifflion.simplethings;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

public class SimpleEntityListener extends EntityListener {
	
	    private final SimpleThings plugin;

	    public SimpleEntityListener(SimpleThings instance) {
	        this.plugin = instance;
	    }


		public void registerEvents() {

	        PluginManager pm = plugin.getServer().getPluginManager();

	        pm.registerEvent(Event.Type.PAINTING_PLACE, this, Priority.High, plugin);
	        pm.registerEvent(Event.Type.PAINTING_BREAK, this, Priority.High, plugin);
	    }
		
		//Painting placement region check
	    public void onPaintingPlace(PaintingPlaceEvent event){
	    	
	        Block blockPlaced = event.getBlock();
	        Player player = event.getPlayer();	
	    	
	        if (event.isCancelled()) {
	            return;
	        }	
	        
    	    if (WorldGuard.getWorldGuardState() == false){
    	    	player.sendMessage(ChatColor.DARK_RED + "The required plugin is not enabled.");
    	    	event.setCancelled(true);
    	    	return;
    	    }
	        	        
	        if (WorldGuard.IsMember(player, blockPlaced) == false ){
	        	event.setCancelled(true);
	            player.sendMessage(ChatColor.DARK_RED + "You don't have permission for this area.");
	            return;       	
	        }

	   }
	    
	   //Painting break region check
	    public void onPaintingBreak(PaintingBreakEvent event){
	    	
	    	if (event.isCancelled()){
	    		return;
	    	}	       	
	    	if(event instanceof PaintingBreakByEntityEvent){	    		    		
	    	   Location block = event.getPainting().getLocation();
	           Entity player = ((PaintingBreakByEntityEvent) event).getRemover();	           	           
	        	        if(player instanceof Player){
	        	        	
	        	    	    if (WorldGuard.getWorldGuardState() == false){
	        	    	    	((Player) player).sendMessage(ChatColor.DARK_RED + "The required plugin is not enabled.");
	        	    	    	event.setCancelled(true);
	        	    	    	return;
	        	    	    }
	        	        	
	        		        if (WorldGuard.IsMemberOnLocation(((Player)player), block) == false ){
                	        	event.setCancelled(true);
                	            ((Player)player).sendMessage(ChatColor.DARK_RED + "You don't have permission for this area.");
                  	            return;       	
	                        }
	                    }   		    	
     	    }	    
        }	    
}

