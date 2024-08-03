package cc.synkdev.deathLogger.command;

import cc.synkdev.deathLogger.DeathLogger;
import cc.synkdev.deathLogger.manager.Death;
import cc.synkdev.deathLogger.manager.Lang;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
    Lang lang = new Lang();
    private final DeathLogger core = DeathLogger.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (p.hasPermission("deathlogger.last")) {
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + lang.translate("missingName"));
            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (getPlayerDeathsDesc(target).isEmpty()) {
                    p.sendMessage(core.getPrefix() + ChatColor.RED + lang.translate("noRecentDeath"));
                } else {
                    p.sendMessage(core.getPrefix() + ChatColor.GOLD + lang.translate("latestDeathsOf") + " " + p.getName());
                    for (int i = 0; i < 10; i++) {
                        if (getPlayerDeathsDesc(target).size() > i) {
                            Death d = getPlayerDeathsDesc(target).get(i);
                            Location loc = d.getLoc();
                            int x = Math.toIntExact(Math.round(loc.getX()));
                            int y = Math.toIntExact(Math.round(loc.getY()));
                            int z = Math.toIntExact(Math.round(loc.getZ()));
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(d.getUnix());
                            TextComponent hover = new TextComponent(lang.translate("clickToOpen"));
                            hover.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                            BaseComponent[] hovrs = {
                                    hover
                            };
                            TextComponent hover1 = new TextComponent(lang.translate("giveInv"));
                            hover1.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                            BaseComponent[] hovrs1 = {
                                    hover1
                            };
                            TextComponent open = new TextComponent("["+lang.translate("open")+"]");
                            open.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hovrs));
                            open.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/getdeath "+d.getId()));
                            open.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                            open.setBold(true);
                            TextComponent give = new TextComponent(" | ["+lang.translate("give")+"]");
                            give.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hovrs1));
                            give.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dl giveinv "+d.getId()));
                            give.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                            give.setBold(true);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
                            LocalDateTime ldt = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            TextComponent def = new TextComponent(core.getPrefix() + ChatColor.GOLD + "#" + d.getId() + " | " + loc.getWorld().getName() + ", " + x + ", " + y + ", " + z + " | " + formatter.format(ldt) + " | ");
                            def.addExtra(open);
                            def.addExtra(give);
                            p.spigot().sendMessage(def);
                        }
                    }
                }
            }
        } else {
            p.sendMessage(core.getPrefix() + ChatColor.RED + lang.translate("noPermission"));
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

    public List<Death> getPlayerDeathsDesc(OfflinePlayer p) {
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
