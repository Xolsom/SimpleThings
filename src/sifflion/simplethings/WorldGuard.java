package sifflion.simplethings;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.GlobalRegionManager;

public class WorldGuard extends SimpleThings {
	
	private static GlobalRegionManager Globalregionmanager = null;
	private static boolean Worldguard = false;
	
	//Setups everything need by WorldGuard	
	public static void setupWorldGuard(Plugin plugin, Server server){
		
        if (Globalregionmanager == null){
            if (plugin != null) {
            	server.getPluginManager().enablePlugin(plugin);
          	  Globalregionmanager = ((WorldGuardPlugin)plugin).getGlobalRegionManager();
              log.info("[" + pdfFile.getName() + "]" + " WorldGuard detected!");
              Worldguard = true;
              
            }
            else
            {
          	log.info("[" + pdfFile.getName() + "]" + " WorldGuard not detected! Some functions may not work!.");
          	Worldguard = false;
            }
        }	
		
	}
	
	//Checks if a player is member of a region by block location
    public static boolean IsMember(Player player, Block block){   	
    	    if(Globalregionmanager.canBuild(player, block)){
    		    return true;
    	    }   
    	return false;
    }
    //Checks if a player is a member of a region by location
    public static boolean IsMemberOnLocation(Player player, Location loc){     	
        	if(Globalregionmanager.canBuild(player, loc)){
        		return true;
        	}     
        return false;
    }
    //Gets the list of regions in a point inside a world
    public static List<String> getRegionList(World world, Vector vector){
    	List<String> regionlist = null;
	    	RegionManager mgr = Globalregionmanager.get(world);
	    	regionlist = mgr.getApplicableRegionsIDs(vector);
	        	if (regionlist.isEmpty() == true){
	     		regionlist = null;
	        
        	}
    	return regionlist;   
    }
    
    public static boolean getWorldGuardState(){
    	return Worldguard;
    }
}
