package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        // Retrieve the target player.
        String targetName = args[0];
        Player target = player.getServer().getPlayerExact(targetName);

        // Check if the target player is online.
        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("Player not found or not online.", NamedTextColor.DARK_RED));
            return true;
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
