package org.eldrygo.DOVote.Utils;

import org.bukkit.Material;
import org.eldrygo.DOVote.DOVote;
import org.eldrygo.DOVote.Inventory.Managers.InventoryManager;

import java.util.List;

public class SettingsUtils {
    private final DOVote plugin;
    private final ItemUtils itemUtils;
    private final PlayerUtils playerUtils;
    private List<String> adminList;
    private final InventoryManager inventoryManager;
    private final InventoryUtils inventoryUtils;

    public SettingsUtils(DOVote plugin, ItemUtils itemUtils, PlayerUtils playerUtils, InventoryManager inventoryManager, InventoryUtils inventoryUtils) {
        this.plugin = plugin;
        this.itemUtils = itemUtils;
        this.playerUtils = playerUtils;
        this.inventoryManager = inventoryManager;
        this.inventoryUtils = inventoryUtils;
    }

    public void loadVoteItemSettings() {
        String itemID = plugin.getConfig().getString("item.material", "PAPER").toUpperCase();
        String itemName = plugin.getConfig().getString("item.name", "&aVote Item");
        List<String> itemLore = plugin.getConfig().getStringList("item.lore");
        int itemModel = plugin.getConfig().getInt("item.custom_model_data");
        boolean itemEnchant = plugin.getConfig().getBoolean("item.enchant");

        if (!isValidItemID(itemID)) {
            plugin.getLogger().warning("⚠ The item material is not valid. Set material to STRUCTURE_VOID has placeholder.");
            itemID = "STRUCTURE_VOID";
        }

        itemUtils.setVoteItemMaterial(itemID);
        itemUtils.setVoteItemName(itemName);
        itemUtils.setVoteItemLore(itemLore);
        itemUtils.setVoteItemCustomModelData(itemModel);
        itemUtils.setVoteItemEnchant(itemEnchant);
    }

    public void loadAdminList() {
        adminList = plugin.getConfig().getStringList("settings.admin_list");
    }

    public void loadVoteTeam() {
        String voteTeam = plugin.getConfig().getString("settings.team_of_vote");
        inventoryManager.setVoteTeamString(voteTeam);
    }

    public void loadGiveAllBypassSettings() {
        List<String> playerList = adminList;
        boolean enabled = plugin.getConfig().getBoolean("settings.giveall_bypass.enabled", false);

        playerUtils.setGiveAllBypassList(playerList);
        playerUtils.setGiveAllBypassEnabled(enabled);
    }

    public boolean isValidItemID(String itemId) {
        try {
            Material material = Material.matchMaterial(itemId);
            return material != null; // Si es null, la ID no es válida
        } catch (Exception e) {
            return false; // Si ocurre algún error, también se considera inválido
        }
    }

    public void loadVoteInventorySettings() {
        String forwardsID = plugin.getConfig().getString("inventory.items.forwards.material", "SPECTRAL_ARROW").toUpperCase();
        String backwardsID = plugin.getConfig().getString("inventory.items.backwards.material", "SPECTRAL_ARROW").toUpperCase();
        String fillID = plugin.getConfig().getString("inventory.items.fill.material", "BLACK_STAINED_GLASS_PANE").toUpperCase();
        String headName = plugin.getConfig().getString("inventory.items.head.name");;
        String forwardsName = plugin.getConfig().getString("inventory.items.forwards.name");;
        String backwardsName = plugin.getConfig().getString("inventory.items.backwards.name");;
        String fillName = plugin.getConfig().getString("inventory.items.fill.name");;
        List<String> headLore = plugin.getConfig().getStringList("inventory.items.head.lore");
        List<String> forwardsLore = plugin.getConfig().getStringList("inventory.items.forwards.lore");
        List<String> backwardsLore = plugin.getConfig().getStringList("inventory.items.backwards.lore");
        List<String> fillLore = plugin.getConfig().getStringList("inventory.items.fill.lore");
        int headModel = plugin.getConfig().getInt("inventory.items.head.custom_model_data");
        int forwardsModel = plugin.getConfig().getInt("inventory.items.forwards.custom_model_data");
        int backwardsModel = plugin.getConfig().getInt("inventory.items.backwards.custom_model_data");
        int fillModel = plugin.getConfig().getInt("inventory.items.fill.custom_model_data");
        boolean headEnchant = plugin.getConfig().getBoolean("inventory.items.head.enchant");
        boolean forwardsEnchant = plugin.getConfig().getBoolean("inventory.items.forwards.enchant");
        boolean backwardsEnchant = plugin.getConfig().getBoolean("inventory.items.backwards.enchant");
        boolean fillEnchant = plugin.getConfig().getBoolean("inventory.items.fill.enchant");

        if (!isValidItemID(forwardsID)) {
            plugin.getLogger().warning("⚠ The Vote Inventory's Forwards Item material is not valid. Set material to SPECTRAL_ARROW has placeholder.");
            forwardsID = "SPECTRAL_ARROW";
        }
        if (!isValidItemID(backwardsID)) {
            plugin.getLogger().warning("⚠ The Vote Inventory's Backwards Item material is not valid. Set material to SPECTRAL_ARROW has placeholder.");
            backwardsID = "SPECTRAL_ARROW";
        }
        if (!isValidItemID(fillID)) {
            plugin.getLogger().warning("⚠ The Vote Inventory's Fill Item material is not valid. Set material to BLACK_STAINED_GLASS_PANE has placeholder.");
            fillID = "BLACK_STAINED_GLASS_PANE";
        }

        inventoryUtils.setHeadName(headName);
        inventoryUtils.setHeadLore(headLore);
        inventoryUtils.setHeadModel(headModel);
        inventoryUtils.setHeadEnchant(headEnchant);
        inventoryUtils.setForwardsID(forwardsID);
        inventoryUtils.setForwardsName(forwardsName);
        inventoryUtils.setForwardsLore(forwardsLore);
        inventoryUtils.setForwardsModel(forwardsModel);
        inventoryUtils.setForwardsEnchant(forwardsEnchant);
        inventoryUtils.setBackwadsID(backwardsID);
        inventoryUtils.setBackwardsName(backwardsName);
        inventoryUtils.setBackwardsLore(backwardsLore);
        inventoryUtils.setBackwardsModel(backwardsModel);
        inventoryUtils.setBackwardsEnchant(backwardsEnchant);
        inventoryUtils.setFillID(fillID);
        inventoryUtils.setFillName(fillName);
        inventoryUtils.setFillLore(fillLore);
        inventoryUtils.setFillModel(fillModel);
        inventoryUtils.setFillEnchant(fillEnchant);
    }
}
