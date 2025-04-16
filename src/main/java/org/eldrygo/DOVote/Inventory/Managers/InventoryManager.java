package org.eldrygo.DOVote.Inventory.Managers;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.eldrygo.DOVote.DOVote;
import org.eldrygo.DOVote.Inventory.Models.InventoryPlayer;
import org.eldrygo.DOVote.Inventory.Models.InventorySection;
import org.eldrygo.DOVote.Managers.VoteManager;
import org.eldrygo.DOVote.Utils.ChatUtils;
import org.eldrygo.DOVote.Utils.InventoryUtils;
import org.eldrygo.DOVote.Utils.ItemUtils;
import org.eldrygo.DOVote.Utils.PlayerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventoryManager {
    private final DOVote plugin;
    private final ArrayList<InventoryPlayer> players;
    private String voteTeamString;
    private final PlayerUtils playerUtils;
    private final InventoryUtils inventoryUtils;
    private final VoteManager voteManager;
    private final ChatUtils chatUtils;
    private final ItemUtils itemUtils;

    public InventoryManager(DOVote plugin, PlayerUtils playerUtils, InventoryUtils inventoryUtils, VoteManager voteManager, ChatUtils chatUtils, ItemUtils itemUtils) {
        this.plugin = plugin;
        this.playerUtils = playerUtils;
        this.inventoryUtils = inventoryUtils;
        this.voteManager = voteManager;
        this.chatUtils = chatUtils;
        this.itemUtils = itemUtils;
        this.players = new ArrayList<>();
    }

    public void removePlayer(Player player) { players.removeIf(inventoryPlayer -> inventoryPlayer.getPlayer().equals(player)); }
    public void setVoteTeamString(String voteTeamString) { this.voteTeamString = voteTeamString; }

    public InventoryPlayer getInventoryPlayer(Player player) {
        for(InventoryPlayer inventoryPlayer : players) {
            if (inventoryPlayer.getPlayer().equals(player)) {
                return inventoryPlayer;
            }
        }
        return null;
    }

    public void openVoteInventory(InventoryPlayer inventoryPlayer) {
        inventoryPlayer.setSection(InventorySection.MENU_VOTE);
        Player player = inventoryPlayer.getPlayer();
        Inventory inventory = createVoteInventory(inventoryPlayer);
        player.openInventory(inventory);
        if (!players.contains(inventoryPlayer)) players.add(inventoryPlayer);
    }

    private Inventory createVoteInventory(InventoryPlayer inventoryPlayer) {
        Player player = inventoryPlayer.getPlayer();
        int page = inventoryPlayer.getVotePage();
        String title = ChatUtils.formatColor(plugin.getConfig().getString("inventory.title")).replace("%page%", String.valueOf(page));
        Inventory inv = Bukkit.createInventory(null, 54, title);

        if (voteTeamString == null) return inv;

        List<Player> playersInTeam = playerUtils.getToVotePlayers(voteTeamString).stream()
                .filter(p -> !p.getName().equals(player.getName()))
                .toList();

        int totalPages = (int) Math.ceil(playersInTeam.size() / 45.0);

        int start = (page - 1) * 45;
        int end = Math.min(start + 45, playersInTeam.size());

        for (int i = start; i < end; i++) {
            Player target = playersInTeam.get(i);
            inv.setItem(i - start, inventoryUtils.getHead(target.getName())); // Usa tu getter aquí
        }

        if (page > 1) {
            inv.setItem(45, inventoryUtils.getBackwardsItem());
        } else {
            inv.setItem(45, inventoryUtils.getFillItem());
        }
        inv.setItem(46, inventoryUtils.getFillItem());
        inv.setItem(47, inventoryUtils.getFillItem());
        inv.setItem(48, inventoryUtils.getFillItem());
        inv.setItem(49, inventoryUtils.getFillItem());
        inv.setItem(50, inventoryUtils.getFillItem());
        inv.setItem(51, inventoryUtils.getFillItem());
        inv.setItem(52, inventoryUtils.getFillItem());
        if (page < totalPages) {
            inv.setItem(53, inventoryUtils.getForwardsItem());
        } else {
            inv.setItem(53, inventoryUtils.getFillItem());
        }

        return inv;
    }

    public void inventoryClick(InventoryPlayer inventoryPlayer, InventoryClickEvent event) {
        Player player = inventoryPlayer.getPlayer();
        InventorySection section = inventoryPlayer.getSection();
        int slot = event.getSlot();

        if (section.equals(InventorySection.MENU_VOTE)) {
            event.setCancelled(true);

            // Página anterior
            if (slot == 45) {
                inventoryPlayer.setVotePage(inventoryPlayer.getVotePage() - 1);
                openVoteInventory(inventoryPlayer);
                return;
            }
            // Página siguiente
            if (slot == 53) {
                inventoryPlayer.setVotePage(inventoryPlayer.getVotePage() + 1);
                openVoteInventory(inventoryPlayer);
                return;
            }
            // Clic en una cabeza
            if (slot >= 0 && slot < 45) {
                ItemStack item = event.getCurrentItem();
                if (item == null || !item.hasItemMeta()) return;

                ItemMeta meta = item.getItemMeta();
                NamespacedKey key = new NamespacedKey(plugin, "nickname");
                String targetName = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

                if (targetName != null) {
                    player.sendMessage(chatUtils.getMessage("vote.success", null).replace("%target%", targetName));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 2.0f, 1.75f);
                    voteManager.registerVote(player, Objects.requireNonNull(Bukkit.getPlayerExact(targetName)));
                    player.closeInventory();
                    playerUtils.removeExactItem(player, itemUtils.getVoteItem());
                }
            }
        }
    }
}
