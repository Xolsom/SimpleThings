package sifflion.simplethings;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;
import sifflion.simplethings.simplethings;

public class SimpleBlockListener extends BlockListener {
    private final simplethings plugin; 

    public SimpleBlockListener(final simplethings plugin) {
        this.plugin = plugin;
    }
    
    public void registerEvents() {

        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvent(Event.Type.BLOCK_PLACE, this, Priority.High, plugin);

    }
    
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.isCancelled()) {
            return;
        }

        Block blockPlaced = event.getBlock();
        Player player = event.getPlayer();
        
        //Portal placement permissions check
        if (blockPlaced.getTypeId() == 90 && simplethings.IsAllowed(player, "darktide.portal.place") == false) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED + "You are not allowed to place this.");
                return;
        }
}
}

