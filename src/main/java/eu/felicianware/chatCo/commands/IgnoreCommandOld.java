package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author lachcrafter
 *
 * Handles the /ignore command to ignore or unignore players.
 */
public class IgnoreCommandOld implements CommandExecutor {

    private final FileConfiguration config;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();

    public IgnoreCommandOld(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // Ensure the sender is a player.
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        // Check if the correct number of arguments are provided.
        if (args.length != 1) {
            Component usage = mm.deserialize(config.getString("usages.ignore"));
            player.sendMessage(usage);
            return true;
        }

        String targetName = args[0];

        // Get the OfflinePlayer object.
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);
        if (target == null) {
            // Try to get the player by UUID or fetch them from the Mojang API.
            target = Bukkit.getOfflinePlayer(targetName);
            if (!target.hasPlayedBefore()) {
                Component playerNotFound = mm.deserialize(config.getString("messages.playerNotFound"));
                player.sendMessage(playerNotFound);
                return true;
            }
        }

        // Prevent players from ignoring themselves.
        if (player.getUniqueId().equals(target.getUniqueId())) {
            Component ignoreThemselves = mm.deserialize(config.getString("messages.ignoreSelf"));
            player.sendMessage(ignoreThemselves);
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        // Toggle ignoring status.
        if (ignoreManager.isIgnoring(playerUUID, targetUUID)) {
            // Unignore the player.
            ignoreManager.unignorePlayer(playerUUID, targetUUID);
            String unignoringMessage = config.getString("messages.unignoring");
            unignoringMessage = unignoringMessage
                    .replace("%player%", target.getName());
            Component unignored = mm.deserialize(unignoringMessage);

            player.sendMessage(unignored);

        } else {
            // Ignore the player.
            ignoreManager.ignorePlayer(playerUUID, targetUUID);
            String ignoringMessage = config.getString("messages.ignoring");
            ignoringMessage = ignoringMessage
                    .replace("%player%", target.getName());
            Component ignored = mm.deserialize(ignoringMessage);
            player.sendMessage(ignored);
        }

        return true;
    }
}
