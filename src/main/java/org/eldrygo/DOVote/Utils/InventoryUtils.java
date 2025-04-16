package org.eldrygo.DOVote.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.eldrygo.DOVote.DOVote;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {
    private final DOVote plugin;
    private String headName;
    private List<String> headLore;
    private int headModel;
    private String forwardsID;
    private String forwardsName;
    private List<String> forwardsLore;
    private int forwardsModel;
    private String backwadsID;
    private String backwardsName;
    private List<String> backwardsLore;
    private int backwardsModel;
    private String fillID;
    private String fillName;
    private List<String> fillLore;
    private int fillModel;
    private boolean fillEnchant;
    private boolean headEnchant;
    private boolean forwardsEnchant;
    private boolean backwardsEnchant;

    public InventoryUtils(DOVote plugin) {
        this.plugin = plugin;
    }

    private void applyEnchantIfNeeded(ItemMeta meta, boolean shouldEnchant) {
        if (shouldEnchant) {
            Enchantment unbreaking = Enchantment.getByKey(NamespacedKey.minecraft("unbreaking"));
            if (unbreaking != null) {
                meta.addEnchant(unbreaking, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                plugin.getLogger().warning("Enchantment 'unbreaking' not found. Skipping enchant.");
            }
        }
    }

    public ItemStack getHead(String playerName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
            meta.setCustomModelData(headModel);
            List<String> formattedLore = new ArrayList<>();
            for (String line : headLore) {
                formattedLore.add(ChatUtils.formatColor(line).replace("%player%", playerName));
            }
            applyEnchantIfNeeded(meta, headEnchant);
            meta.setDisplayName(ChatUtils.formatColor(headName).replace("%player%", playerName));
            meta.setLore(formattedLore);
            NamespacedKey key = new NamespacedKey(plugin, "nickname");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, playerName);
        }
        head.setItemMeta(meta);
        return head;
    }

    public ItemStack getForwardsItem() {
        ItemStack item = new ItemStack(Material.valueOf(forwardsID));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtils.formatColor(forwardsName));
            meta.setCustomModelData(forwardsModel);
            List<String> formattedLore = new ArrayList<>();
            for (String line : forwardsLore) {
                formattedLore.add(ChatUtils.formatColor(line));
            }
            applyEnchantIfNeeded(meta, forwardsEnchant);
            meta.setLore(formattedLore);
        }
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getBackwardsItem() {
        ItemStack item = new ItemStack(Material.valueOf(backwadsID));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtils.formatColor(backwardsName));
            meta.setCustomModelData(backwardsModel);
            List<String> formattedLore = new ArrayList<>();
            for (String line : backwardsLore) {
                formattedLore.add(ChatUtils.formatColor(line));
            }
            applyEnchantIfNeeded(meta, backwardsEnchant);
            meta.setLore(formattedLore);
        }
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getFillItem() {
        ItemStack item = new ItemStack(Material.valueOf(fillID));
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtils.formatColor(fillName));
            meta.setCustomModelData(fillModel);
            List<String> formattedLore = new ArrayList<>();
            for (String line : fillLore) {
                formattedLore.add(ChatUtils.formatColor(line));
            }
            applyEnchantIfNeeded(meta, fillEnchant);
            meta.setLore(formattedLore);
        }
        item.setItemMeta(meta);

        return item;
    }

    // Setters...
    public void setHeadName(String headName) { this.headName = headName; }
    public void setHeadLore(List<String> headLore) { this.headLore = headLore; }
    public void setHeadModel(int headModel) { this.headModel = headModel; }
    public void setHeadEnchant(boolean headEnchant) { this.headEnchant = headEnchant; }
    public void setForwardsID(String forwardsID) { this.forwardsID = forwardsID; }
    public void setForwardsName(String forwardsName) { this.forwardsName = forwardsName; }
    public void setForwardsLore(List<String> forwardsLore) { this.forwardsLore = forwardsLore; }
    public void setForwardsModel(int forwardsModel) { this.forwardsModel = forwardsModel; }
    public void setForwardsEnchant(boolean forwardsEnchant) { this.forwardsEnchant = forwardsEnchant; }
    public void setBackwadsID(String backwadsID) { this.backwadsID = backwadsID; }
    public void setBackwardsName(String backwardsName) { this.backwardsName = backwardsName; }
    public void setBackwardsLore(List<String> backwardsLore) { this.backwardsLore = backwardsLore;}
    public void setBackwardsModel(int backwardsModel) { this.backwardsModel = backwardsModel; }
    public void setBackwardsEnchant(boolean backwardsEnchant) { this.backwardsEnchant = backwardsEnchant; }
    public void setFillID(String fillID) { this.fillID = fillID; }
    public void setFillName(String fillName) { this.fillName = fillName; }
    public void setFillLore(List<String> fillLore) { this.fillLore = fillLore; }
    public void setFillModel(int fillModel) { this.fillModel = fillModel; }
    public void setFillEnchant(boolean fillEnchant) { this.fillEnchant = fillEnchant; }
}
