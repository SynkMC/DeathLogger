package cc.synkdev.deathLogger.manager;

import cc.synkdev.deathLogger.DeathLogger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateManager implements Listener {
    Lang lang = new Lang();
    DeathLogger core = DeathLogger.getInstance();
    public void checkVer() {
        try {
            URL url = new URL("https://synkdev.cc/ver/deathlogger/");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals(core.getVersion())) {
                    core.log(core.getPrefix() + ChatColor.GREEN + lang.translate("upToDate"));
                } else {
                    core.log(core.getPrefix() + ChatColor.GREEN + lang.translate("updateAvailable") + ": v" + inputLine);
                    core.log(core.getPrefix() + ChatColor.GREEN + lang.translate("downloadHere") + ": https://modrinth.com/plugin/deathlogger");
                    core.setUpdate(true);
                    core.setNewVersion(inputLine);
                }
                break;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @EventHandler
    public void login (PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if (p.hasPermission("deathlogger.last")) {
            if (core.getUpdate()) {
                p.sendMessage(core.getPrefix() + ChatColor.GREEN + lang.translate("updateAvailable") + ": v" + core.getNewVersion());
                p.sendMessage(core.getPrefix() + ChatColor.GREEN + lang.translate("downloadHere") + ": https://modrinth.com/plugin/deathlogger");
            }
        }
    }
}
