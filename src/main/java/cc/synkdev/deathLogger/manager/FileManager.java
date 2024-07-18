package cc.synkdev.deathLogger.manager;

import cc.synkdev.deathLogger.DeathLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.*;

public class FileManager {
    private DeathLogger core = DeathLogger.getInstance();
    public File file() {
        return new File(core.getDataFolder(), "playerdata.txt");
    }
    public void create() {
        if (!file().exists()) {
            try {
                file().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void insert(Death d) {
        core.deaths.add(d);
        List<Death> deathMap = core.deaths;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file()))) {
            for (Death de : deathMap) {
                writer.write(de.getId() + "µ" + de.getPlayer().getUniqueId() + "µ" + posToString(de.getLoc()) + "µ" + de.getMsg() + "µ" + serializeInventory(de.getInv()) + "µ" + de.getUnix());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setDeathMap() {
        List<Death> deathMap = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("µ");
                Death d;
                try {
                    d = new Death(Integer.parseInt(parts[0]), Bukkit.getOfflinePlayer(UUID.fromString(parts[1])), stringToPos(parts[2]), parts[3], deserializeInventory(parts[4]), Long.parseLong(parts[5]));
                    deathMap.add(d);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        core.deaths.addAll(deathMap);
    }
    private String posToString(Location pos) {
        return pos.getWorld().getName() + "," + Math.round(pos.getX()) + "," + Math.round(pos.getY()) + "," + Math.round(pos.getZ());
    }
    private Location stringToPos(String s) {
        String[] ss = s.split(",");
        return new Location(Bukkit.getWorld(ss[0]), Double.parseDouble(ss[1]), Double.parseDouble(ss[1]), Double.parseDouble(ss[1]));
    }

    private String serializeInventory(ItemStack[] inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.length);

            for(ItemStack item : inventory) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException var8) {
            var8.printStackTrace();
            return null;
        }
    }


    public ItemStack[] deserializeInventory(String data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

        int length = dataInput.readInt();
        ItemStack[] items = new ItemStack[length];

        for (int i = 0; i < length; i++) {
            items[i] = (ItemStack) dataInput.readObject();
        }

        dataInput.close();
        return items;
    }
}
