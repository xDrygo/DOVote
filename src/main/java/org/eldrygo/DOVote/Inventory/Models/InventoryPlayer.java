package org.eldrygo.DOVote.Inventory.Models;

import org.bukkit.entity.Player;

public class InventoryPlayer {
    private Player player;
    private InventorySection section;
    private int votePage = 1;

    public InventoryPlayer(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSection(InventorySection section) {
        this.section = section;
    }

    public Player getPlayer() {
        return player;
    }

    public InventorySection getSection() {
        return section;
    }

    public int getVotePage() { return votePage; }

    public void setVotePage(int votePage) { this.votePage = votePage; }
}
