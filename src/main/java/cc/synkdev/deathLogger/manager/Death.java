package cc.synkdev.deathLogger.manager;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
public class Death {
    private int id;
    private OfflinePlayer player;
    private Location loc;
    private String msg;
    private ItemStack[] inv;
    private long unix;

    public Death(int id, OfflinePlayer player, Location loc, String msg, ItemStack[] inv, long unix) {
        this.id = id;
        this.player = player;
        this.loc = loc;
        this.msg = msg;
        this.inv = inv;
        this.unix = unix;
    }
}
