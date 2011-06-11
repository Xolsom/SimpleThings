package sifflion.simplethings;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Permission extends SimpleThings {
	
	private static PermissionHandler PermissionsHandler = null;
	private static boolean Permissions = false;
	
	public static void setupPermissions(Plugin plugin, Server server){
		
		
        if (PermissionsHandler == null){
            if (plugin != null){
              server.getPluginManager().enablePlugin(plugin);
              PermissionsHandler = ((Permissions)plugin).getHandler();
              log.info("[" + pdfFile.getName() + "]" + " Permissions detected!");
              Permissions = true;
            }
            else
            {
          	log.info("[" + pdfFile.getName() + "]" + " Permissions not detected! Some functions may not work!.");
          	Permissions = false;
            }
        }
		
	}
	
	//Checks if a player has permissions
    public static boolean IsAllowed(Player player, String string){   	  		
    	    if(PermissionsHandler.has(player, string)){
    		    return true;
    	    }
    	return false;   	
    }
    
    public static boolean getPermissionsState(){
    	return Permissions;
    }

}
