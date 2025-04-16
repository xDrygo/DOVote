package org.eldrygo.DOVote.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.eldrygo.DOVote.DOVote;
import org.eldrygo.DOVote.Managers.ConfigManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {
    private final DOVote plugin;
    private final ConfigManager configManager;

    public ChatUtils(DOVote plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public static String formatColor(String message) {
        message = replaceHexColors(message);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String replaceHexColors(String message) {
        Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String hexColor = matcher.group(1);
            StringBuilder color = new StringBuilder("&x");
            for (char c : hexColor.toCharArray()) {
                color.append("&").append(c);
            }
            matcher.appendReplacement(buffer, color.toString());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
    public String getMessage(String path, Player player) {
        if (configManager == null) {
            throw new IllegalStateException("ConfigManager no está inicializado.");
        }

        String message = configManager.getMessageConfig().isList(path)
                ? String.join("\n", configManager.getMessageConfig().getStringList(path))
                : configManager.getMessageConfig().getString(path);

        if (message == null || message.isEmpty()) {
            plugin.getLogger().warning("[WARNING] Message not found: " + path);
            return ChatUtils.formatColor("&r" + configManager.getPrefix() + " #FF0000&l[ERROR] #FF3535Message not found: " + path);
        }

        // Reemplazar placeholders
        if (player != null) {
            message = message.replace("%player%", player.getName());
        } else {
            message = message.replace("%player%", "Unknown");
        }

        /*
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }
         */

        message = message.replace("%prefix%", configManager.getPrefix());

        return ChatUtils.formatColor(message);
    }
}
