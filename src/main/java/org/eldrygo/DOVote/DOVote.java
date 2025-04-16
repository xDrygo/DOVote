package org.eldrygo.DOVote;

import org.bukkit.plugin.java.JavaPlugin;
import org.eldrygo.DOVote.Inventory.Managers.InventoryManager;
import org.eldrygo.DOVote.Managers.ConfigManager;
import org.eldrygo.DOVote.Managers.ItemManager;
import org.eldrygo.DOVote.Managers.VoteManager;
import org.eldrygo.DOVote.Utils.*;

public class DOVote extends JavaPlugin {
    public String prefix;
    private LogsUtils logsUtils;
    private VoteManager voteManager;

    @Override
    public void onEnable() {
        PlayerUtils playerUtils = new PlayerUtils();
        InventoryUtils inventoryUtils = new InventoryUtils(this);
        ItemUtils itemUtils = new ItemUtils(this);
        ItemManager itemManager = new ItemManager(this, itemUtils);
        this.voteManager = new VoteManager(this, itemManager);
        ConfigManager configManager = new ConfigManager(this);
        ChatUtils chatUtils = new ChatUtils(this, configManager);
        InventoryManager inventoryManager = new InventoryManager(this, playerUtils, inventoryUtils, voteManager, chatUtils, itemUtils);
        SettingsUtils settingsUtils = new SettingsUtils(this, itemUtils, playerUtils, inventoryManager, inventoryUtils);
        LoadUtils loadUtils = new LoadUtils(this, playerUtils, itemManager, configManager, chatUtils, settingsUtils, inventoryManager, itemUtils, voteManager);
        this.logsUtils = new LogsUtils(this);
        loadUtils.loadFeatures();
        voteManager.loadVotes();
        logsUtils.sendStartupMessage();
    }

    @Override
    public void onDisable() {
        voteManager.saveVotes();
        logsUtils.sendShutdownMessage();
    }
}
