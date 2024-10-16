package eu.felicianware.chatCo.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the last messaged players
 */
public class MessageManager {

    private static MessageManager instance;
    private final Map<UUID, UUID> lastMessaged;

    private MessageManager() {
        this.lastMessaged = new HashMap<>();
    }

    public static MessageManager getInstance() {
        if (instance == null) {
            instance = new MessageManager();
        }
        return instance;
    }

    /**
     * Sets the last messaged player for a given player.
     *
     * @param playerUUID The UUID of the player.
     * @param targetUUID The UUID of the last messaged player.
     */
    public void setLastMessaged(UUID playerUUID, UUID targetUUID) {
        lastMessaged.put(playerUUID, targetUUID);
    }

    /**
     * Gets the last messaged player for a given player.
     *
     * @param playerUUID The UUID of the player.
     * @return The UUID of the last messaged player, or null if none.
     */
    public UUID getLastMessaged(UUID playerUUID) {
        return lastMessaged.get(playerUUID);
    }

    /**
     * Clears the last messaged player for a given player.
     *
     * @param playerUUID The UUID of the player.
     */
    public void clearLastMessaged(UUID playerUUID) {
        lastMessaged.remove(playerUUID);
    }
}
