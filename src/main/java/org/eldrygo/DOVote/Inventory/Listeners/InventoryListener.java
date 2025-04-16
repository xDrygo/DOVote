package org.eldrygo.DOVote.Inventory.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.eldrygo.DOVote.Inventory.Managers.InventoryManager;
import org.eldrygo.DOVote.Inventory.Models.InventoryPlayer;

import java.util.Objects;

public class InventoryListener implements Listener {
    private final InventoryManager inventoryManager;

    public InventoryListener(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        InventoryPlayer inventoryPlayer = inventoryManager.getInventoryPlayer(player);
        if (inventoryPlayer != null) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && Objects.equals(event.getClickedInventory(), player.getOpenInventory().getTopInventory())) {
                inventoryManager.inventoryClick(inventoryPlayer, event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        inventoryManager.removePlayer(player);
    }
}
