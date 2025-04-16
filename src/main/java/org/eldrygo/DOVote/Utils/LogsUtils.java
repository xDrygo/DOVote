package org.eldrygo.DOVote.Utils;

import org.bukkit.Bukkit;
import org.eldrygo.DOVote.DOVote;

public class LogsUtils {
    private final DOVote plugin;
    private final String version;

    public LogsUtils(DOVote plugin) {
        this.plugin = plugin;
        this.version = plugin.getDescription().getVersion();
    }

    public void sendStartupMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&6&lDO&r&lVote #a0ff72plugin enabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#fff18dVersion: #ffffff" + version));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#fff18dDeveloped by: #ffffff" + String.join(", ", plugin.getDescription().getAuthors())));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
    }
    public void sendShutdownMessage() {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("&6&lDO&r&lVote #ff7272plugin disabled!"));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#fff18dVersion: #ffffff" + version));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor("#fff18dDeveloped by: #ffffff" + String.join(", ", plugin.getDescription().getAuthors())));
        Bukkit.getConsoleSender().sendMessage(ChatUtils.formatColor(" "));
    }
}
