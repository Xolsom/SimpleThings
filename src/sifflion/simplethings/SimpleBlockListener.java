package sifflion.simplethings;

import static com.sk89q.worldguard.bukkit.BukkitUtil.toVector;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;

import com.sk89q.worldedit.Vector;

import sifflion.simplethings.SimpleThings;

public class SimpleBlockListener extends BlockListener {
    private final SimpleThings plugin; 

    public SimpleBlockListener(final SimpleThings plugin) {
        this.plugin = plugin;
    }
    
    public void registerEvents() {

        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvent(Event.Type.BLOCK_PLACE, this, Priority.High, plugin);
        pm.registerEvent(Event.Type.BLOCK_BREAK, this, Priority.High, plugin);

    }
    
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.isCancelled()) {
            return;
        }

        Block blockPlaced = event.getBlock();
        Player player = event.getPlayer();
        Vector vector = toVector(blockPlaced.getLocation());
        World world = blockPlaced.getWorld();
        
        //Portal placement permissions check
        if (blockPlaced.getTypeId() == 90){
        	    if (Permission.getPermissionsState() == false){
        	    	player.sendMessage(ChatColor.DARK_RED + "The required plugin is not enabled.");
        	    	event.setCancelled(true);
        	    	return;
        	    }
        	    if (Permission.IsAllowed(player, "darktide.portal.place") == false) {       
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.DARK_RED + "You are not allowed to place this.");
                    return;
        }
    }
        //Ice placement outside regions check
        if (blockPlaced.getTypeId() == 79) {
        	
    	    if (Permission.getPermissionsState() == false || WorldGuard.getWorldGuardState() == false){
    	    	player.sendMessage(ChatColor.DARK_RED + "The required plugin is not enabled.");
    	    	event.setCancelled(true);
    	    	return;
    	    }
        	
        	if (Permission.IsAllowed(player, "darktide.ice.place") == false){
                if (WorldGuard.getRegionList(world, vector) == null) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You cannot use that outside of acceptable WorldGuard regions");
                        return;
                }
            }
        }
        
        //Sand Gravel placement check
        if (blockPlaced.getTypeId() == 12 || blockPlaced.getTypeId() == 13 ){
        	
    	    if (WorldGuard.getWorldGuardState() == false){
    	    	player.sendMessage(ChatColor.DARK_RED + "The required plugin is not enabled.");
    	    	event.setCancelled(true);
    	    	return;
    	    }
        	
            	for (int i = blockPlaced.getY()-1; i >= 1; i = i - 1 ){       	
             		int xx = blockPlaced.getX();
            		int yy = i;
            		int zz = blockPlaced.getZ();
            	   	Location location = new Location(world,xx,yy,zz);      		
             		if (world.getBlockTypeIdAt(xx, yy, zz) != 0){
            			return;
            		}
            		if (!WorldGuard.IsMemberOnLocation(player, location)){
            			player.sendMessage(ChatColor.RED + "This block is going to fall into a region.");
            			event.setCancelled(true);
            			return;       			
            		}
            	}
         	}        	
        }
    
    public void onBlockBreak(BlockBreakEvent event){
    	
    	Block blockBreaked = event.getBlock();
    	World world = blockBreaked.getWorld();
    	Player player = event.getPlayer();
    	
    //Sand/Gravel check when destroying a block	
    	
    if (world.getBlockTypeIdAt(blockBreaked.getX(), blockBreaked.getY()+1, blockBreaked.getZ()) == 12 || world.getBlockTypeIdAt(blockBreaked.getX(), blockBreaked.getY()+1, blockBreaked.getZ()) == 13){  
    	
	    if (WorldGuard.getWorldGuardState() == false){
	    	player.sendMessage(ChatColor.DARK_RED + "The required plugin is not enabled.");
	    	event.setCancelled(true);
	    	return;
	    }
    	
        	for (int i = blockBreaked.getY()-1; i >= 1; i = i - 1 ){     	 	
         		int xx = blockBreaked.getX();
        		int yy = i;
        		int zz = blockBreaked.getZ();
        		Location location = new Location(world,xx,yy,zz);    		
        		if (world.getBlockTypeIdAt(xx, yy, zz) != 0){
        			return;
        		}
        		if (!WorldGuard.IsMemberOnLocation(player, location)){
        			player.sendMessage(ChatColor.RED + "A block on this one is going to fall into a region.");
        			event.setCancelled(true);
         			return;       			
        		}
        	}   	
        }   	  		
    }
}

