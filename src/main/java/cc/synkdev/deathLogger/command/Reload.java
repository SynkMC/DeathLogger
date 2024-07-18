package cc.synkdev.deathLogger.command;

import cc.synkdev.deathLogger.DeathLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {
    private final DeathLogger core = DeathLogger.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender.hasPermission("deathlogger.reload")) {
            core.loadConfig();
            sender.sendMessage(ChatColor.GREEN + core.getReloaded());
        } else {
            sender.sendMessage(ChatColor.RED + core.getNoPerm());
        }

        return true;
    }
}
