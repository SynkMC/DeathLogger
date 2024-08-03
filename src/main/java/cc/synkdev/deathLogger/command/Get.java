package cc.synkdev.deathLogger.command;

import cc.synkdev.deathLogger.DeathLogger;
import cc.synkdev.deathLogger.manager.Death;
import cc.synkdev.deathLogger.manager.Lang;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Get implements CommandExecutor {
    private final DeathLogger core = DeathLogger.getInstance();
    Lang lang = new Lang();
    Last last = new Last();

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(core.getPrefix() + ChatColor.RED + lang.translate("playerOnly"));
            return true;
        } else {
            Player player = (Player) sender;
            if (!player.hasPermission("deathlogger.get")) {
                player.sendMessage(core.getPrefix() + ChatColor.RED + lang.translate("noPermission"));
                return true;
            } else {
                boolean canTakeItems = player.hasPermission("deathlogger.get.take");
                if (args.length < 1) {
                    player.sendMessage(core.getPrefix() + ChatColor.RED + lang.translate("getCmdUsage"));
                    return true;
                } else {
                    OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
                    if (op.hasPlayedBefore() || op.isOnline()) {
                        Gui gui = Gui.gui()
                                .disableItemDrop()
                                .disableItemPlace()
                                .disableItemSwap()
                                .title(Component.text(lang.translate("latestDeath") + " " + op.getName()))
                                .rows(6)
                                .create();
                        if (!canTakeItems) {
                            gui.disableAllInteractions();
                        }

                        Death d = null;
                        if (!last.getPlayerDeathsDesc(op).isEmpty()) {
                            d = last.getPlayerDeathsDesc(op).get(0);
                        }
                        if (d != null) {
                            if (d.getInv().length > 0) {
                                for (ItemStack itemStack : d.getInv()) {
                                    if (itemStack != null) {
                                        GuiItem gi = ItemBuilder.from(itemStack).asGuiItem();
                                        gui.addItem(gi);
                                    }
                                }
                                GuiItem msgItem = ItemBuilder.from(Material.PAPER)
                                        .name(Component.text(ChatColor.YELLOW + lang.translate("deathMsg")))
                                        .lore(Component.text(ChatColor.GRAY + d.getMsg()))
                                        .asGuiItem(inventoryClickEvent -> {
                                            inventoryClickEvent.setCancelled(true);
                                        });
                                gui.addItem(msgItem);
                                gui.open(player);
                                player.sendMessage(core.getPrefix() + ChatColor.GREEN + lang.translate("invFromDeath") + " #" + d.getId() + " " + lang.translate("wasOpened"));
                                return true;
                            }
                        }
                    }
                        int deathId;
                        try {
                            deathId = Integer.parseInt(args[0]);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(core.getPrefix() + ChatColor.RED + lang.translate("wrongFormat"));
                            return true;
                        }
                        if (core.deaths.size() >= deathId - 1) {
                            Death dd = core.deaths.get(deathId);
                            String deathMsg = dd.getMsg();
                            Gui ggui = Gui.gui()
                                    .disableItemDrop()
                                    .disableItemPlace()
                                    .disableItemSwap()
                                    .title(Component.text(lang.translate("invFromDeath") + " #" + deathId))
                                    .rows(6)
                                    .create();
                            if (!canTakeItems) {
                                ggui.disableAllInteractions();
                            }
                            if (dd.getInv().length > 0) {
                                for (ItemStack itemStack : dd.getInv()) {
                                    if (itemStack != null) {
                                        GuiItem gi = ItemBuilder.from(itemStack).asGuiItem();
                                        ggui.addItem(gi);
                                    }
                                }
                                GuiItem msgItem = ItemBuilder.from(Material.PAPER)
                                        .name(Component.text(ChatColor.YELLOW + lang.translate("deathMsg")))
                                        .lore(Component.text(ChatColor.GRAY + deathMsg))
                                        .asGuiItem(inventoryClickEvent -> {
                                            inventoryClickEvent.setCancelled(true);
                                        });
                                ggui.addItem(msgItem);
                                ggui.open(player);
                                player.sendMessage(core.getPrefix() + ChatColor.GREEN + lang.translate("invFromDeath") + " #" + deathId + " " + lang.translate("wasOpened"));
                            } else {
                                player.sendMessage(core.getPrefix() + ChatColor.RED + lang.translate("invFromDeath") + " #" + deathId + " " + lang.translate("doesntExist"));
                            }
                        }
                }
            }
            return true;
        }
    }
}
