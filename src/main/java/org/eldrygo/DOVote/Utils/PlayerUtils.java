package org.eldrygo.DOVote.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {
    private boolean giveAllBypassEnabled;
    private List<String> giveAllBypassList;

    public List<Player> getGiveVoteItemPlayers() {
        List<Player> filteredPlayers = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (giveAllBypassEnabled) {
                if (!giveAllBypassList.contains(String.valueOf(player))) {
                    filteredPlayers.add(player);
                }
            } else {
                filteredPlayers.add(player);
            }
        }
        return filteredPlayers;
    }

    public List<Player> getToVotePlayers(String teamName) {
        List<Player> playersInTeam = new ArrayList<>();

        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
            if (team == null) return playersInTeam;

            for (String entry : team.getEntries()) {
                Player player = Bukkit.getPlayer(entry);
                if (player != null && player.isOnline()) {
                    playersInTeam.add(player);
                }
        }
        return playersInTeam;
    }

    public void removeExactItem(Player player, ItemStack itemToRemove) {
        Inventory inventory = player.getInventory();

        // Recorrer todo el inventario
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            // Verificar si el ítem es nulo o no es el mismo tipo que el ítem que queremos eliminar
            if (item != null && item.isSimilar(itemToRemove)) {
                // Verificar si el ítem tiene el mismo CustomModelData
                if (item.hasItemMeta()) {
                    ItemMeta itemMeta = item.getItemMeta();
                    ItemMeta itemToRemoveMeta = itemToRemove.getItemMeta();

                    if (itemMeta != null && itemToRemoveMeta != null &&
                            itemMeta.hasCustomModelData() && itemToRemoveMeta.hasCustomModelData() &&
                            itemMeta.getCustomModelData() == itemToRemoveMeta.getCustomModelData()) {
                        // Eliminar el ítem del inventario
                        inventory.clear(i);
                        break;  // Salir del bucle después de eliminar el primer ítem encontrado
                    }
                }
            }
        }
    }
    public void setGiveAllBypassList(List<String> giveAllBypassList) { this.giveAllBypassList = giveAllBypassList; }
    public void setGiveAllBypassEnabled(boolean giveAllBypassEnabled) { this.giveAllBypassEnabled = giveAllBypassEnabled; }
}
