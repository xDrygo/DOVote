package org.eldrygo.DOVote.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DOVoteTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("giveitem");
            completions.add("top");
            completions.add("reset");
        }

        if (args.length == 2) {
            completions.add("*");
            for (Player player : sender.getServer().getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        return completions;
    }
}
