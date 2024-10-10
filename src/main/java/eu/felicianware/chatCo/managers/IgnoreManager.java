package eu.felicianware.chatCo.managers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

/**
 * @author lachcrafter
 *
 * Manages the ignore lists for players.
 *
 */
public class IgnoreManager {

    private static IgnoreManager instance;
    private final Map<UUID, Set<UUID>> ignoreLists;

    private IgnoreManager() {
        this.ignoreLists = new HashMap<>();
    }

    public static IgnoreManager getInstance() {
        if (instance == null) {
            instance = new IgnoreManager();
        }
        return instance;
    }

    /**
     * Adds a player to another player's ignore list.
     *
     * @param playerUUID The UUID of the player ignoring someone.
     * @param targetUUID The UUID of the player being ignored.
     */
    public void ignorePlayer(UUID playerUUID, UUID targetUUID) {
        ignoreLists.computeIfAbsent(playerUUID, k -> new HashSet<>()).add(targetUUID);
    }

    /**
     * Removes a player from another player's ignore list.
     *
     * @param playerUUID The UUID of the player unignoring someone.
     * @param targetUUID The UUID of the player being unignored.
     */
    public void unignorePlayer(UUID playerUUID, UUID targetUUID) {
        Set<UUID> ignored = ignoreLists.get(playerUUID);
        if (ignored != null) {
            ignored.remove(targetUUID);
            if (ignored.isEmpty()) {
                ignoreLists.remove(playerUUID);
            }
        }
    }

    /**
     * Checks if a player is ignoring another player.
     *
     * @param playerUUID The UUID of the player.
     * @param targetUUID The UUID of the potential ignored player.
     * @return True if the player is ignoring the target, false otherwise.
     */
    public boolean isIgnoring(UUID playerUUID, UUID targetUUID) {
        Set<UUID> ignored = ignoreLists.get(playerUUID);
        return ignored != null && ignored.contains(targetUUID);
    }

    /**
     * Retrieves the set of UUIDs of players that the specified player is ignoring.
     *
     * @param playerUUID The UUID of the player.
     * @return A Set of UUIDs of ignored players, or an empty Set if none.
     */
    public Set<UUID> getIgnoredPlayers(UUID playerUUID) {
        return ignoreLists.getOrDefault(playerUUID, Collections.emptySet());
    }

    /**
     * Retrieves the list of player names that the specified player is ignoring.
     *
     * @param playerUUID The UUID of the player.
     * @return A List of player names.
     */
    public List<String> getIgnoredPlayerNames(UUID playerUUID) {
        Set<UUID> ignoredUUIDs = getIgnoredPlayers(playerUUID);
        List<String> names = new ArrayList<>();
        for (UUID uuid : ignoredUUIDs) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer.getName() != null) {
                names.add(offlinePlayer.getName());
            } else {
                names.add(uuid.toString());
            }
        }
        return names;
    }
}
