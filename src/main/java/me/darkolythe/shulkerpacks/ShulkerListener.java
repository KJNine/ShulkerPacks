package me.darkolythe.shulkerpacks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ShulkerListener implements Listener {

    public ShulkerPacks main;
    public ShulkerListener(ShulkerPacks plugin) {
        this.main = plugin; //set it equal to an instance of main
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                @Override
                public void run() {
                    saveShulker(player, event.getView().getTitle());
                }
            }, 1);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null && (main.openshulkers.containsKey(player.getUniqueId()) && event.getCurrentItem().equals(main.openshulkers.get(player.getUniqueId())))) {
                event.setCancelled(true);
                return;
            }
            if (event.getClickedInventory() != null && (event.getClickedInventory().getType() == InventoryType.CHEST && !main.canopeninchests)) {
                return;
            }
            InventoryType type = event.getInventory().getType();
            if (type == InventoryType.WORKBENCH || type == InventoryType.ANVIL || type == InventoryType.BEACON || type == InventoryType.MERCHANT || type == InventoryType.ENCHANTING) {
                return;
            }
            if (type == InventoryType.CRAFTING && event.getRawSlot() >= 1 && event.getRawSlot() <= 4) {
                return;
            }

            for (String str: main.blacklist) {
                if (ChatColor.translateAlternateColorCodes('&', str).equals(player.getOpenInventory().getTitle())) {
                    return;
                }
            }
            if (!main.shiftclicktoopen || event.isShiftClick()) {
                if (event.getClickedInventory() != null && event.isRightClick() && openInventoryIfShulker(event.getCurrentItem(), player)) {
                    event.setCancelled(true);
                    return;
                }
            }
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                @Override
                public void run() {
                    saveShulker(player, event.getView().getTitle());
                }
            }, 1);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (saveShulker(player, player.getOpenInventory().getTitle())) {
                player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 1, 1);
            }
            main.openshulkers.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onClickAir(PlayerInteractEvent event) {
        if ((!main.shiftclicktoopen || event.getPlayer().isSneaking())) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                ItemStack item = event.getItem();
                openInventoryIfShulker(item, event.getPlayer());
            }
        }
    }

    public boolean saveShulker(Player player, String title) {
        if (main.openshulkers.containsKey(player.getUniqueId())) {
            if (title.equals(main.defaultname) || (main.openshulkers.get(player.getUniqueId()).hasItemMeta() &&
                                    main.openshulkers.get(player.getUniqueId()).getItemMeta().hasDisplayName() &&
                                    (main.openshulkers.get(player.getUniqueId()).getItemMeta().getDisplayName().equals(title)))) {
                ItemStack item = main.openshulkers.get(player.getUniqueId());
                if (item != null) {
                    BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                    shulker.getInventory().setContents(main.openinventories.get(player.getUniqueId()).getContents());
                    meta.setBlockState(shulker);
                    item.setItemMeta(meta);
                    main.openshulkers.put(player.getUniqueId(), item);
                    player.updateInventory();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean openInventoryIfShulker(ItemStack item, Player player) {
        if (player.hasPermission("shulkerpacks.use")) {
            if (item != null) {
                if (item.getItemMeta() instanceof BlockStateMeta) {
                    BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                    if (meta.getBlockState() instanceof ShulkerBox) {
                        ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                        Inventory inv;
                        if (meta.hasDisplayName()) {
                            inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, meta.getDisplayName());
                        } else {
                            inv = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, main.defaultname);
                        }
                        inv.setContents(shulker.getInventory().getContents());
                        player.openInventory(inv);
                        player.playSound(player.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1, 1);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                            @Override
                            public void run() {
                                main.openshulkers.put(player.getUniqueId(), item);
                                main.openinventories.put(player.getUniqueId(), player.getOpenInventory().getTopInventory());
                            }
                        }, 1);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}