package org.eldrygo.DOVote.Managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.eldrygo.DOVote.DOVote;
import org.eldrygo.DOVote.Utils.ItemUtils;

public class ItemManager {
    private final DOVote plugin;
    private final ItemUtils itemUtils;
    private final boolean debug;

    public ItemManager(DOVote plugin, ItemUtils itemUtils) {
        this.plugin = plugin;
        this.itemUtils = itemUtils;
        this.debug = plugin.getConfig().getBoolean("settings.debug_messages", false);
    }

    public void giveVoteItem(Player player) {
        ItemStack item = itemUtils.getVoteItem();

        if (item != null) {
            player.getInventory().addItem(item);
            if (debug) {
                plugin.getLogger().info("✅ Player " + player + " received the Vote Item. (You can disable this message making debug_message false on config.yml)");
            }
        }
    }
}
