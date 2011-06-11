package sifflion.simplethings;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;
import com.sk89q.worldedit.Vector;

import sifflion.simplethings.SimpleBlockListener;

public class SimpleThings extends JavaPlugin {
	
    private final SimpleBlockListener blockListener = new SimpleBlockListener(this);
    private final SimpleEntityListener entityListener = new SimpleEntityListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    protected static LWC lwc = null;
    protected static final Logger log = Logger.getLogger("Minecraft");
    protected static PluginDescriptionFile pdfFile = null;

    public void onDisable() {

	    String strEnable = pdfFile.getName() + pdfFile.getVersion() + " disabled.";
	    log.info(strEnable);
    }

    public void onEnable() {

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PAINTING_PLACE, entityListener, Priority.High, this);
        pm.registerEvent(Event.Type.PAINTING_BREAK, entityListener, Priority.High, this);
        if(pdfFile == null){
            pdfFile = this.getDescription();
            }
        log.info( "[" + pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " is enabled!" );
        setupPlugins();
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
    	Player player = null;
    	
    	if(sender instanceof Player)
    		player = (Player)sender;  
    	
    	    //Home replacement
    		if(commandLabel.equalsIgnoreCase("home")){			 
    			player.sendMessage(ChatColor.DARK_RED + "Home has been disabled. Check out the new warps in the mage tower!");
    		    return true;
            }
    		
    		//LWC reset outside WorldGuard regions. Thanks Xolsom.
    		if(commandLabel.equalsIgnoreCase("lwcreset")  && Permission.IsAllowed(player, "darktide.admin.resetlwc")){ 
    			if(WorldGuard.getWorldGuardState() == false || lwc == null){
    				player.sendMessage(ChatColor.DARK_RED + "The required plugin is not enabled.");
    				return true;
    			}
    			
    			for(Protection protection : lwc.getPhysicalDatabase().loadProtections()) {
    				Vector protectionPoint = new Vector(protection.getX(), protection.getY(), protection.getZ());
    			    if(WorldGuard.getRegionList(player.getWorld(), protectionPoint) == null) {
    			        lwc.getPhysicalDatabase().unregisterProtection(protection.getId());
    			        protection.removeCache();
    			    }
    			}

    			player.sendMessage(ChatColor.DARK_RED + "Every LWC outside of valid WorldGuard regions has been removed.");    			
    			return true;
    		}
    		else
    		{
    			player.sendMessage(ChatColor.RED + "You do not have access to that command.");
    		}
    		
    		//Killme replacement
    		if(commandLabel.equalsIgnoreCase("killme")){			 
    			player.sendMessage(ChatColor.DARK_RED + "Killme has been disabled");
    		    return true;
            }
      return true; 
      }
    
    public void setupPlugins() {
    	Server server = getServer();
    	Plugin Permissions = server.getPluginManager().getPlugin("Permissions");
        Plugin Worldguard = server.getPluginManager().getPlugin("WorldGuard");
        LWCPlugin LWC = (LWCPlugin) getServer().getPluginManager().getPlugin("LWC");
        WorldGuard.setupWorldGuard(Worldguard, server);
        Permission.setupPermissions(Permissions, server);
        
        if (lwc == null){
            if (LWC != null) {
            	lwc = ((LWCPlugin) LWC).getLWC();
              log.info("[" + pdfFile.getName() + "]" + " LWC detected!");
            }
            else
            {
          	log.info("[" + pdfFile.getName() + "]" + " LWC not detected! WorldGuard checks defaulted to False.");  
            }
        }
      }
            
}