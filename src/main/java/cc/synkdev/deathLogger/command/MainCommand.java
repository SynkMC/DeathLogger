package cc.synkdev.deathLogger.command;

import cc.synkdev.deathLogger.DeathLogger;
import cc.synkdev.deathLogger.manager.Death;
import cc.synkdev.deathLogger.manager.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {
    private final DeathLogger core = DeathLogger.getInstance();
    Lang lang = new Lang();
    private CommandSender sender;

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        this.sender = sender;
        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "reload":
                        if (checkPerm("deathlogger.reload")) {
                            core.getLm().load();
                            core.getFm().setDeathMap();
                            sender.sendMessage(ChatColor.GREEN + lang.translate("reloaded"));
                        }
                        break;
                }
                break;
            case 2:
                switch (args[0]) {
                    case "giveinv":
                        if (checkPerm("deathlogger.giveinv")) {
                            int id = Integer.parseInt(args[1]);
                            if (core.deaths.size()>=id-1) {
                                Death d = core.deaths.get(id);
                                if (d.getPlayer().isOnline()) {
                                    d.getPlayer().getPlayer().getInventory().setContents(d.getInv());
                                    d.getPlayer().getPlayer().sendMessage(core.getPrefix()+ChatColor.GREEN+sender.getName()+" "+lang.translate("gaveInv"));
                                    sender.sendMessage(core.getPrefix()+ChatColor.GREEN+lang.translate("success"));
                                } else {
                                    sender.sendMessage(core.getPrefix()+ChatColor.RED+lang.translate("offline"));
                                }
                            }
                        }
                        break;
                }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("deathlogger.reload")) list.add("reload");
            if (sender.hasPermission("deathlogger.giveinv")) list.add("giveinv");
        }
        return list;
    }

    boolean checkPerm(String s) {
        if (sender.hasPermission(s)) {
            return true;
        } else {
            sender.sendMessage(core.getPrefix()+ChatColor.RED+lang.translate("noPermission"));
            return false;
        }
    }
}
