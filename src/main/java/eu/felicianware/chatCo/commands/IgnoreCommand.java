package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author lachcrafter
 *
 * Handles the /ignore command to ignore or unignore players.
 */
public class IgnoreCommand implements CommandExecutor {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // Ensure the sender is a player.
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        // Check if the correct number of arguments are provided.
        if (args.length != 1) {
            player.sendMessage(Component.text("Usage: /ignore <player>", NamedTextColor.DARK_RED));
            return true;
        }

        String targetName = args[0];

        // Get the OfflinePlayer object.
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);
        if (target == null) {
            // Try to get the player by UUID or fetch them from the Mojang API.
            target = Bukkit.getOfflinePlayer(targetName);
            if (!target.hasPlayedBefore()) {
                player.sendMessage(Component.text("Player not found.", NamedTextColor.DARK_RED));
                return true;
            }
        }

        // Prevent players from ignoring themselves.
        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(Component.text("You cannot ignore yourself.", NamedTextColor.DARK_RED));
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        // Toggle ignoring status.
        if (ignoreManager.isIgnoring(playerUUID, targetUUID)) {
            // Unignore the player.
            ignoreManager.unignorePlayer(playerUUID, targetUUID);
            player.sendMessage(Component.text("You have unignored " + target.getName() + ".", NamedTextColor.GOLD));
        } else {
            // Ignore the player.
            ignoreManager.ignorePlayer(playerUUID, targetUUID);
            player.sendMessage(Component.text("You have ignored " + target.getName() + ".", NamedTextColor.GOLD));
        }

        return true;
    }
}
