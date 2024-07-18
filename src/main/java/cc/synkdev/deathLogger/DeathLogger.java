package cc.synkdev.deathLogger;

import cc.synkdev.deathLogger.command.Get;
import cc.synkdev.deathLogger.command.Last;
import cc.synkdev.deathLogger.command.Reload;
import cc.synkdev.deathLogger.listener.DeathListener;
import cc.synkdev.deathLogger.manager.Death;
import cc.synkdev.deathLogger.manager.FileManager;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class DeathLogger extends JavaPlugin {
    @Getter private static DeathLogger instance;
    @Getter private final String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6DeathLogger&8] » &r");
    @Getter private FileManager fm;
    public List<Death> deaths = new ArrayList<>();
    private String noRecentDeath;
    private String latestDeathsOf;
    private String missingName;
    private String locationWord;
    private String queryError;
    private String noPerm;
    private String playerOnly;
    private String usage;
    private String wrongFormat;
    private String invFromDeath;
    private String wasOpened;
    private String dontExist;
    private String notFound;
    private String error;
    private String reloaded;
    private String deathMsg;
    private String tp;
    private String clickToTp;

    public void loadConfig() {
        this.reloadConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        noRecentDeath = this.getConfig().getString("noRecentDeath");
        latestDeathsOf = this.getConfig().getString("latestDeathsOf");
        missingName = this.getConfig().getString("missingName");
        locationWord = this.getConfig().getString("location");
        queryError = this.getConfig().getString("queryError");
        noPerm = this.getConfig().getString("noPermission");
        playerOnly = this.getConfig().getString("playerOnly");
        usage = this.getConfig().getString("usage");
        wrongFormat = this.getConfig().getString("wrongFormat");
        invFromDeath = this.getConfig().getString("invFromDeath");
        wasOpened = this.getConfig().getString("wasOpened");
        dontExist = this.getConfig().getString("doesntExist");
        notFound = this.getConfig().getString("notFound");
        error = this.getConfig().getString("error");
        reloaded = this.getConfig().getString("reloaded");
        deathMsg = this.getConfig().getString("deathMsg");
        tp = this.getConfig().getString("teleport");
        clickToTp = this.getConfig().getString("clickToTeleport");
    }


    public void onEnable() {
        instance = this;
        fm = new FileManager();
        this.loadConfig();
        fm.create();
        fm.setDeathMap();
        this.getCommand("getdeath").setExecutor(new Get());
        this.getCommand("lastdeaths").setExecutor(new Last());
        this.getCommand("dlreload").setExecutor(new Reload());
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        int plId = 22687;
        Metrics metrics = new Metrics(this, plId);
    }


    public void onDisable() {

    }
}