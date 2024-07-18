package cc.synkdev.deathLogger.listener;

import cc.synkdev.deathLogger.DeathLogger;
import cc.synkdev.deathLogger.manager.Death;
import cc.synkdev.deathLogger.manager.FileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathListener implements Listener {
    private final DeathLogger core = DeathLogger.getInstance();
    private final FileManager fm = new FileManager();
    @EventHandler
    public void death(PlayerDeathEvent event) {
        Player p = event.getEntity();
        String message = event.getDeathMessage();
        ItemStack[] inv = p.getInventory().getContents();
        Death d = new Death(core.deaths.size(), p, p.getLocation(), message, inv, System.currentTimeMillis());
        fm.insert(d);
    }
}
