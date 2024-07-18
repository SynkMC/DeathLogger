package cc.synkdev.deathLogger.command;

import cc.synkdev.deathLogger.DeathLogger;
import cc.synkdev.deathLogger.manager.Death;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Last implements CommandExecutor {
    private final DeathLogger core = DeathLogger.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (p.hasPermission("deathlogger.last")) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + core.getMissingName());
            } else {
                if (getPlayerDeathsDesc(p).isEmpty()) {
                    p.sendMessage(core.getPrefix() + ChatColor.RED + core.getNoRecentDeath());
                } else {
                    p.sendMessage(core.getPrefix() + ChatColor.GOLD + core.getLatestDeathsOf() + " " + p.getName());
                    for (int i = 0; i < 10; i++) {
                        if (getPlayerDeathsDesc(p).size() > i) {
                            Death d = getPlayerDeathsDesc(p).get(i);
                            Location loc = d.getLoc();
                            int x = Math.toIntExact(Math.round(loc.getX()));
                            int y = Math.toIntExact(Math.round(loc.getY()));
                            int z = Math.toIntExact(Math.round(loc.getZ()));
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(d.getUnix());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
                            LocalDateTime ldt = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            p.sendMessage(core.getPrefix() + ChatColor.GOLD + "#" + d.getId() + " | " + loc.getWorld().getName() + ", " + x + ", " + y + ", " + z + " | " + formatter.format(ldt));
                        }
                    }
                }
            }
        } else {
            p.sendMessage(core.getPrefix() + ChatColor.RED + core.getNoPerm());
        }

        return true;
    }

    public List<Death> getPlayerDeaths(Player p) {
        List<Death> list = new ArrayList<>();
        for (Death d : core.deaths) {
            if (d.getPlayer().getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) list.add(d);
        }
        return list;
    }

    public List<Death> getPlayerDeathsDesc(Player p) {
        List<Death> list = new ArrayList<>();
        if (!core.deaths.isEmpty()) {
            for (int i = core.deaths.size()-1; i >= 0; i--) {
                Death d = core.deaths.get(i);
                if (d.getPlayer().getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) list.add(d);
            }
        }
        return list;
    }
}
