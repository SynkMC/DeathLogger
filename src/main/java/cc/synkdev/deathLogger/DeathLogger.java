package cc.synkdev.deathLogger;

import cc.synkdev.deathLogger.command.Get;
import cc.synkdev.deathLogger.command.Last;
import cc.synkdev.deathLogger.command.MainCommand;
import cc.synkdev.deathLogger.listener.DeathListener;
import cc.synkdev.deathLogger.manager.Death;
import cc.synkdev.deathLogger.manager.FileManager;
import cc.synkdev.deathLogger.manager.Lang;
import cc.synkdev.deathLogger.manager.UpdateManager;
import lombok.Getter;
import lombok.Setter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class DeathLogger extends JavaPlugin {
    @Getter private static DeathLogger instance;
    @Getter private final String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6DeathLogger&8] » &r");
    @Getter private FileManager fm;
    public List<Death> deaths = new ArrayList<>();
    Lang lm;

    private final String version = "2.3";
    @Setter private String newVersion = null;
    @Getter @Setter Boolean update = false;

    UpdateManager um;

    public void onEnable() {
        instance = this;
        um = new UpdateManager();
        um.checkVer();
        lm = new Lang();
        lm.init();
        lm.load();
        fm = new FileManager();
        fm.create();
        fm.setDeathMap();
        this.getCommand("getdeath").setExecutor(new Get());
        this.getCommand("lastdeaths").setExecutor(new Last());
        this.getCommand("dl").setExecutor(new MainCommand());
        this.getCommand("dl").setTabCompleter(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new UpdateManager(), this);
        int plId = 22687;
        new Metrics(this, plId);
    }


    public void onDisable() {

    }
    public void log(String s) {
        Bukkit.getConsoleSender().sendMessage(s);
    }
}