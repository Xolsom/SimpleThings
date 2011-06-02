package sifflion.simplethings;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import sifflion.simplethings.SimpleBlockListener;

public class simplethings extends JavaPlugin {
	
    private final SimpleBlockListener blockListener = new SimpleBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    private static PermissionHandler Permissions = null;
    protected static final Logger log = Logger.getLogger("Minecraft");
    private PluginDescriptionFile pdfFile = null;

    public void onDisable() {

	    String strEnable = pdfFile.getName() + pdfFile.getVersion() + " disabled.";
	    log.info(strEnable);
    }

    public void onEnable() {

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        if(pdfFile == null){
            pdfFile = this.getDescription();
            }
        log.info( "[" + pdfFile.getName() + "]" + " version " + pdfFile.getVersion() + " is enabled!" );
        setupPermissions();
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
      return true; 
      }
    
    public void setupPermissions() {
        Plugin this_plugin = getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null){
          if (this_plugin != null) {
            getServer().getPluginManager().enablePlugin(this_plugin);
            Permissions = ((Permissions)this_plugin).getHandler();
            log.info("[" + pdfFile.getName() + "]" + " Permissions detected!");
          }
          else
          {
        	log.info("[" + pdfFile.getName() + "]" + " Permissions not detected! Permissions checks defaulted to False.");  
          }
      }
    }
    
    public static boolean IsAllowed(Player player, String string){
    	
    	if(Permissions != null){   		
    	    if(Permissions.has(player, string)){
    		    return true;
    	    }
    	}
    	return false;   	
    }

      
  
}
