package cc.synkdev.deathLogger.command;

import cc.synkdev.deathLogger.DeathLogger;
import cc.synkdev.deathLogger.manager.Death;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Get implements CommandExecutor {
    private final DeathLogger core = DeathLogger.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(core.getPrefix() + ChatColor.RED + core.getPlayerOnly());
            return true;
        } else {
            Player player = (Player) sender;
            if (!player.hasPermission("deathlogger.get")) {
                player.sendMessage(core.getPrefix() + ChatColor.RED + core.getNoPerm());
                return true;
            } else {
                boolean canTakeItems = player.hasPermission("deathlogger.get.take");
                if (args.length < 1) {
                    player.sendMessage(core.getPrefix() + ChatColor.RED + core.getUsage() + " /getdeath <id>");
                    return true;
                } else {
                    int deathId;
                    try {
                        deathId = Integer.parseInt(args[0]);
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(core.getPrefix() + ChatColor.RED + core.getWrongFormat());
                        return true;
                    }
                    if (core.deaths.size() >= deathId - 1) {
                        Death d = core.deaths.get(deathId);
                        String deathMsg = d.getMsg();
                        if (d != null) {
                            Gui gui = Gui.gui()
                                    .disableItemDrop()
                                    .disableItemPlace()
                                    .disableItemSwap()
                                    .title(Component.text(core.getInvFromDeath() + " #" + deathId))
                                    .rows(6)
                                    .create();
                            if (!canTakeItems) {
                                gui.disableAllInteractions();
                            }
                            if (d.getInv().length > 0) {
                                for (ItemStack itemStack : d.getInv()) {
                                    if (itemStack != null) {
                                        GuiItem gi = ItemBuilder.from(itemStack).asGuiItem();
                                        gui.addItem(gi);
                                    }
                                }
                                GuiItem msgItem = ItemBuilder.from(Material.PAPER)
                                        .name(Component.text(ChatColor.YELLOW + core.getDeathMsg()))
                                        .lore(Component.text(ChatColor.GRAY + deathMsg))
                                        .asGuiItem(inventoryClickEvent -> {
                                            inventoryClickEvent.setCancelled(true);
                                        });
                                gui.addItem(msgItem);
                                gui.open(player);
                                player.sendMessage(core.getPrefix() + ChatColor.GREEN + core.getInvFromDeath() + " #" + deathId + " " + core.getWasOpened());
                            } else {
                                player.sendMessage(core.getPrefix() + ChatColor.RED + core.getInvFromDeath() + " #" + deathId + " " + core.getDontExist());
                            }
                        }

                    }
                }
            }
        }
        return true;
    }
}
