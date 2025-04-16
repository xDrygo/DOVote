package org.eldrygo.DOVote.Utils;

import org.eldrygo.DOVote.DOVote;
import org.eldrygo.DOVote.Handlers.DOVoteCommand;
import org.eldrygo.DOVote.Handlers.DOVoteTabCompleter;
import org.eldrygo.DOVote.Inventory.Listeners.InventoryListener;
import org.eldrygo.DOVote.Inventory.Managers.InventoryManager;
import org.eldrygo.DOVote.Listeners.ItemListener;
import org.eldrygo.DOVote.Managers.ConfigManager;
import org.eldrygo.DOVote.Managers.ItemManager;
import org.eldrygo.DOVote.Managers.VoteManager;

public class LoadUtils {
    private final DOVote plugin;
    private final PlayerUtils playerUtils;
    private final ItemManager itemManager;
    private final ConfigManager configManager;
    private final ChatUtils chatUtils;
    private final SettingsUtils settingsUtils;
    private final InventoryManager inventoryManager;
    private final ItemUtils itemUtils;
    private final VoteManager voteManager;

    public LoadUtils(DOVote plugin, PlayerUtils playerUtils, ItemManager itemManager, ConfigManager configManager, ChatUtils chatUtils, SettingsUtils settingsUtils, InventoryManager inventoryManager, ItemUtils itemUtils, VoteManager voteManager) {
        this.plugin = plugin;
        this.playerUtils = playerUtils;
        this.itemManager = itemManager;
        this.configManager = configManager;
        this.chatUtils = chatUtils;
        this.settingsUtils = settingsUtils;
        this.inventoryManager = inventoryManager;
        this.itemUtils = itemUtils;
        this.voteManager = voteManager;
    }

    public void loadFeatures() {
        loadFiles();
        loadSettings();
        loadCommands();
        loadListeners();
    }

    private void loadSettings() {
        settingsUtils.loadVoteItemSettings();
        settingsUtils.loadAdminList();
        settingsUtils.loadVoteTeam();
        settingsUtils.loadGiveAllBypassSettings();
        settingsUtils.loadVoteInventorySettings();
    }

    private void loadCommands() {
        if (plugin.getCommand("dovote") == null) {
            plugin.getLogger().severe("❌ Error: DOVote command is no registered in plugin.yml");
        } else {
            plugin.getLogger().info("✅ Plugin command /dovote successfully registered.");
            plugin.getCommand("dovote").setExecutor(new DOVoteCommand(plugin, chatUtils, configManager, itemManager, playerUtils, voteManager));
            plugin.getCommand("dovote").setTabCompleter(new DOVoteTabCompleter());
        }
    }

    private void loadListeners() {
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(inventoryManager), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ItemListener(itemUtils, inventoryManager), plugin);
    }

    private void loadFiles() {
        configManager.loadConfig();
        configManager.reloadMessages();
        configManager.setPrefix(configManager.getMessageConfig().getString("prefix"));
    }
}