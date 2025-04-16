package org.eldrygo.DOVote.Managers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.eldrygo.DOVote.DOVote;
import org.eldrygo.DOVote.Utils.ChatUtils;
import org.eldrygo.DOVote.Utils.SettingsUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class VoteManager {
    private final DOVote plugin;
    private final ItemManager itemManager;

    // Estructuras para almacenar los votos
    private final Map<String, Set<String>> votesReceived = new HashMap<>();
    private final Map<String, Set<String>> votesSent = new HashMap<>();

    private final File voteFile;

    public VoteManager(DOVote plugin, ItemManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.voteFile = new File(plugin.getDataFolder(), "votes.json");

        loadVotes();
    }

    // Registrar un voto
    public boolean registerVote(Player from, Player to) {
        String fromName = from.getName();
        String toName = to.getName();

        votesSent.putIfAbsent(fromName, new HashSet<>());
        votesReceived.putIfAbsent(toName, new HashSet<>());

        votesSent.get(fromName).add(toName);
        votesReceived.get(toName).add(fromName);

        itemManager.giveVoteItem(from);

        if (plugin.getConfig().getBoolean("settings.debug_messages", true)) {
            plugin.getLogger().info("✅ " + fromName + " voted for " + toName);
        }

        // Guardar los votos en el archivo JSON después de cada registro
        saveVotes();

        return true;
    }

    // Resetear todos los votos
    public void resetAllVotes() {
        votesReceived.clear();
        votesSent.clear();
        saveVotes();  // Guardar cambios después de resetear

        plugin.getLogger().info("All votes have been reset.");
    }

    // Obtener los jugadores más votados
    public List<Map.Entry<String, Integer>> getTopVotedPlayers() {
        return votesReceived.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());
    }

    // Cargar los votos desde el archivo JSON
    public void loadVotes() {
        if (!voteFile.exists()) {
            return; // Si el archivo no existe, no hacemos nada
        }

        try (Reader reader = new FileReader(voteFile)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // Cargar los votos recibidos
            for (String playerName : jsonObject.keySet()) {
                Set<String> receivedVotes = new HashSet<>(Arrays.asList(jsonObject.getAsJsonArray(playerName).toString().split(",")));
                votesReceived.put(playerName, receivedVotes);
            }

            plugin.getLogger().info("Votes loaded from file.");

        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load votes from file.");
            e.printStackTrace();
        }
    }

    // Guardar los votos en el archivo JSON
    public void saveVotes() {
        try (Writer writer = new FileWriter(voteFile)) {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();

            // Guardar los votos recibidos
            for (Map.Entry<String, Set<String>> entry : votesReceived.entrySet()) {
                jsonObject.add(entry.getKey(), gson.toJsonTree(entry.getValue()));
            }

            gson.toJson(jsonObject, writer);

            plugin.getLogger().info("Votes saved to file.");
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save votes to file.");
            e.printStackTrace();
        }
    }
}
