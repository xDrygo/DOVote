package org.eldrygo.DOVote.Utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.eldrygo.DOVote.DOVote;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public String voteItemMaterial;
    public String voteItemName;
    public List<String> voteItemLore;
    public int voteItemCustomModelData;
    public boolean voteItemEnchant;
    private final DOVote plugin;

    public ItemUtils(DOVote plugin) {
        this.plugin = plugin;
    }

    public ItemStack getVoteItem() {
        ItemStack item = new ItemStack(Material.valueOf(voteItemMaterial));
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            String formattedName = ChatUtils.formatColor(voteItemName);

            List<String> formattedLore = new ArrayList<>();
            for (String line : voteItemLore) {
                formattedLore.add(ChatUtils.formatColor(line));
            }

            if (voteItemEnchant) {
                Enchantment unbreaking = Enchantment.getByKey(NamespacedKey.minecraft("unbreaking"));
                if (unbreaking != null) {
                    meta.addEnchant(unbreaking, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                } else {
                    plugin.getLogger().warning("Enchantment 'unbreaking' not found. Skipping enchant.");
                }
            }

            meta.setDisplayName(formattedName);
            meta.setLore(formattedLore);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.setCustomModelData(voteItemCustomModelData);
            item.setItemMeta(meta);
        }

        return item;
    }

    public void setVoteItemMaterial(String material) { voteItemMaterial = material; }
    public void setVoteItemName(String name) { voteItemName = name; }
    public void setVoteItemLore(List<String> lore) { voteItemLore = lore; }
    public void setVoteItemCustomModelData(int model) { voteItemCustomModelData = model; }
    public void setVoteItemEnchant(boolean state) { voteItemEnchant = state; }
    public String getVoteItemMaterial() { return voteItemMaterial; }
    public int getVoteItemCustomModelData() { return voteItemCustomModelData; }
}
