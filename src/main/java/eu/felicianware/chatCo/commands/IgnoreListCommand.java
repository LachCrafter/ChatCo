package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author lachcrafter
 *
 * Handles the /ignorelist command to display the ignored players.
 */
public class IgnoreListCommand implements CommandExecutor {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // Ensure the sender is a player.
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        // Retrieve the list of ignored player names.
        List<String> ignoredNames = ignoreManager.getIgnoredPlayerNames(player.getUniqueId());

        if (ignoredNames.isEmpty()) {
            player.sendMessage(Component.text("You are not ignoring any players.", NamedTextColor.GOLD));
        } else {
            StringBuilder listBuilder = new StringBuilder();
            for (String name : ignoredNames) {
                listBuilder.append(name).append(", ");
            }
            // Remove the trailing comma and space.
            String list = listBuilder.substring(0, listBuilder.length() - 2);

            player.sendMessage(Component.text("Players you are ignoring:", NamedTextColor.GOLD));
            player.sendMessage(Component.text(list, NamedTextColor.RED));
        }

        return true;
    }
}
