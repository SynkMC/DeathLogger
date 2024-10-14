package cc.synkdev.deathLogger.command;

import cc.synkdev.deathLogger.DeathLogger;
import cc.synkdev.deathLogger.manager.Death;
import cc.synkdev.synkLibs.bukkit.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {
    private final DeathLogger core = DeathLogger.getInstance();
    private CommandSender sender;

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        this.sender = sender;
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("reload")) {
                    if (checkPerm("deathlogger.reload")) {
                        core.langMap.clear();
                        core.langMap.putAll(Lang.init(core, new File(core.getDataFolder(), "lang.json")));
                        sender.sendMessage(ChatColor.GREEN + Lang.translate("reloaded", core));
                    }
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("giveinv")) {
                    if (checkPerm("deathlogger.giveinv")) {
                        int id = Integer.parseInt(args[1]);
                        if (core.deaths.size() >= id - 1) {
                            Death d = core.deaths.get(id);
                            if (d.getPlayer().isOnline()) {
                                d.getPlayer().getPlayer().getInventory().setContents(d.getInv());
                                d.getPlayer().getPlayer().sendMessage(core.getPrefix() + ChatColor.GREEN + sender.getName() + " " + Lang.translate("gaveInv", core));
                                sender.sendMessage(core.getPrefix() + ChatColor.GREEN + Lang.translate("success", core));
                            } else {
                                sender.sendMessage(core.getPrefix() + ChatColor.RED + Lang.translate("offline", core));
                            }
                        }
                    }
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
            sender.sendMessage(core.getPrefix()+ChatColor.RED+Lang.translate("noPermission", core));
            return false;
        }
    }
}
