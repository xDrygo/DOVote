package org.eldrygo.DOVote.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.eldrygo.DOVote.DOVote;
import org.eldrygo.DOVote.Managers.ConfigManager;
import org.eldrygo.DOVote.Managers.ItemManager;
import org.eldrygo.DOVote.Managers.VoteManager;
import org.eldrygo.DOVote.Utils.ChatUtils;
import org.eldrygo.DOVote.Utils.PlayerUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class DOVoteCommand implements CommandExecutor {
    private final DOVote plugin;
    private final ChatUtils chatUtils;
    private final ConfigManager configManager;
    private final ItemManager itemManager;
    private final PlayerUtils playerUtils;
    private final VoteManager voteManager;

    public DOVoteCommand(DOVote plugin, ChatUtils chatUtils, ConfigManager configManager, ItemManager itemManager, PlayerUtils playerUtils, VoteManager voteManager) {
        this.plugin = plugin;
        this.chatUtils = chatUtils;
        this.configManager = configManager;
        this.itemManager = itemManager;
        this.playerUtils = playerUtils;
        this.voteManager = voteManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(chatUtils.getMessage("error.no_action", null));
            return true;
        }

        String action = args[0];

        switch (action) {
            case "reload" -> {
                if (!sender.hasPermission("dovote.reload") && !sender.isOp()) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", (Player) sender));
                } else {
                    handleReload(sender);
                }
            }
            case "top" -> {
                if (!sender.hasPermission("dovote.top") && !sender.isOp()) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", (Player) sender));
                } else {
                    handleTopVotes(sender, args);
                }
            }
            case "reset" -> {
                if (!sender.hasPermission("dovote.reset") && !sender.isOp()) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", (Player) sender));
                } else {
                    handleResetVotes(sender);
                }
            }
            case "giveitem" -> { return handleGiveItem(sender, args); }
            default -> sender.sendMessage(chatUtils.getMessage("error.invalid_action", null));
        }
        return false;
    }
    private void handleResetVotes(CommandSender sender) {
        voteManager.resetAllVotes();
        plugin.getLogger().warning("⚠️ Se han reseteado los votos.");
        sender.sendMessage(chatUtils.getMessage("command.reset.success", null));
    }

    private void handleTopVotes(CommandSender sender, String[] args) {
        int page = 1;
        int perPage = 10;

        // Manejo de argumentos para la página
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                sender.sendMessage(chatUtils.getMessage("commands.topvotes.invalid_page", null));
            }
        }

        // Obtener la lista de jugadores más votados
        List<Map.Entry<String, Integer>> topVoted = voteManager.getTopVotedPlayers();

        // Verificar si hay jugadores votados
        if (topVoted.isEmpty()) {
            sender.sendMessage(chatUtils.getMessage("command.topvotes.no_votes", null));
        }

        // Calcular las páginas totales
        int totalPages = (int) Math.ceil(topVoted.size() / (double) perPage);
        if (page > totalPages) page = totalPages;

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, topVoted.size());

        // Enviar los resultados
        sender.sendMessage(chatUtils.getMessage("command.topvotes.header", null)
                .replace("%page%", String.valueOf(page))
                .replace("%total_pages%", String.valueOf(totalPages)));

        // Listar los jugadores más votados
        for (int i = start; i < end; i++) {
            Map.Entry<String, Integer> entry = topVoted.get(i);
            sender.sendMessage(chatUtils.getMessage("command.topvotes.player", null)
                    .replace("%position%", String.valueOf(i + 1))
                    .replace("%player_p%", entry.getKey())
                    .replace("%votes%", String.valueOf(entry.getValue())));
        }
    }

    private boolean handleGiveItem(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(chatUtils.getMessage("command.giveitem.self.only_player", null));
                return true;
            }
            if (!sender.hasPermission("dovote.giveitem.self") && !sender.isOp()) {
                sender.sendMessage(chatUtils.getMessage("error.no_permission", (Player) sender));
                return true;
            }
            itemManager.giveVoteItem(player);
            player.sendMessage(chatUtils.getMessage("command.giveitem.self.sender", player));
            return true;
        } else {
            if (args[1].equals("*")) {
                if (!sender.hasPermission("dovote.giveitem.all") && !sender.isOp()) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", (Player) sender));
                    return true;
                }
                int affected = 0;
                for (Player target : playerUtils.getGiveVoteItemPlayers()) {
                    itemManager.giveVoteItem(target);
                    if (!sender.equals(target)) {
                        target.sendMessage(chatUtils.getMessage("command.giveitem.other.target", null).replace("%target%", target.getName()));
                    }
                    affected++;
                }

                sender.sendMessage(chatUtils.getMessage("command.giveitem.other.all", null).replace("%players%", String.valueOf(affected)));
                return true;
            } else {
                if (!sender.hasPermission("dovote.giveitem.others") && !sender.isOp()) {
                    sender.sendMessage(chatUtils.getMessage("error.no_permission", (Player) sender));
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(chatUtils.getMessage("error.player_not_found", null).replace("%target%", args[1]));
                    return true;
                }

                itemManager.giveVoteItem(target);
                if (!sender.equals(target)) {
                    target.sendMessage(chatUtils.getMessage("command.giveitem.other.target", null));
                }
                sender.sendMessage(chatUtils.getMessage("command.giveitem.other.sender", null).replace("%target%", args[1]));
            }
        }
        return false;
    }

    public void handleReload(CommandSender sender) {
        configManager.loadConfig();
        configManager.reloadMessages();
        configManager.setPrefix(configManager.getMessageConfig().getString("prefix"));
        sender.sendMessage(chatUtils.getMessage("command.reload.success", (Player) sender));
    }
}
