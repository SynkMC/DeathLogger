package cc.synkdev.deathLogger.manager;

import cc.synkdev.deathLogger.DeathLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Lang {
    public Lang() {
        init();
    }

    public FileConfiguration config;
    DeathLogger core = DeathLogger.getInstance();
    public File file = new File(core.getDataFolder(), "lang.yml");

    public void init() {
        if (!core.getDataFolder().exists()) {
            core.getDataFolder().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);

        config.addDefault("error", "An error has occurred. Please check the console for errors");
        config.addDefault("noRecentDeath", "The player has no recent death");
        config.addDefault("missingName", "Please specify a player's name");
        config.addDefault("server", "Server");
        config.addDefault("location", "Location");
        config.addDefault("queryError", "Error while retrieving data");
        config.addDefault("noPermission", "You don't have the permission to use this command");
        config.addDefault("playerOnly", "You must be a player to use this command");
        config.addDefault("usage", "Usage:");
        config.addDefault("wrongFormat", "Wrong number format");
        config.addDefault("invFromDeath", "Inventory from death");
        config.addDefault("wasOpened", "was opened");
        config.addDefault("doesntExist", "doesn't exist");
        config.addDefault("notFound", "No death found with id");
        config.addDefault("reloaded", "The configuration file was successfully reloaded");
        config.addDefault("deathMsg", "Death message");
        config.addDefault("latestDeathsOf", "Latest deaths of");
        config.addDefault("clickToTeleport", "Click to teleport to the death location");
        config.addDefault("teleport", "Teleport");
        config.addDefault("clickToOpen", "Click to open the inventory");
        config.addDefault("open", "Open inventory");
        config.addDefault("updateAvailable", "An update is available");
        config.addDefault("downloadHere", "Download it here");
        config.addDefault("upToDate", "The plugin is up to date!");
        config.addDefault("latestDeath", "Latest death of");
        config.addDefault("getCmdUsage", "Usage: /getdeath <id>. Get the ID from /lastdeaths <player>");
        config.addDefault("giveInv", "Set the player's inventory to what it was before death");
        config.addDefault("give", "Give");
        config.addDefault("offline", "This player is offline!");
        config.addDefault("gaveInv", "gave your inventory back from before you died!");
        config.addDefault("success", "Success!");

        config.options().copyDefaults(true);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String translate(String s) {
        return removeEnds(config.getString(s));
    }

    public String removeEnds(String s) {
        return s.split("\"")[0];
    }
}
