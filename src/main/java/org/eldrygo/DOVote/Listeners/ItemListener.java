package org.eldrygo.DOVote.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.eldrygo.DOVote.Inventory.Managers.InventoryManager;
import org.eldrygo.DOVote.Inventory.Models.InventoryPlayer;
import org.eldrygo.DOVote.Utils.ItemUtils;

public class ItemListener implements Listener {
    private final InventoryManager inventoryManager;
    private final Material voteItemMaterial;
    private final int voteItemModel;

    public ItemListener(ItemUtils itemUtils, InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        this.voteItemMaterial = Material.valueOf(itemUtils.getVoteItemMaterial());
        this.voteItemModel = itemUtils.getVoteItemCustomModelData();
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == voteItemMaterial &&
                item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
                item.getItemMeta().getCustomModelData() == voteItemModel) {
            inventoryManager.openVoteInventory(new InventoryPlayer(player));
        }
    }
}
