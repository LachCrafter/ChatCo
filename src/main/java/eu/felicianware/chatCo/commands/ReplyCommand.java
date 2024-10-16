package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import eu.felicianware.chatCo.managers.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Handles the /r command to reply to the last messaged player.
 */
public class ReplyCommand implements CommandExecutor {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private final MessageManager messageManager = MessageManager.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // Ensure the sender is a player.
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        // Check if message is provided.
        if (args.length < 1) {
            player.sendMessage(Component.text("Usage: /" + label + " <message>", NamedTextColor.DARK_RED));
            return true;
        }

        UUID senderUUID = player.getUniqueId();

        // Get the last messaged player.
        UUID targetUUID = messageManager.getLastMessaged(senderUUID);
        if (targetUUID == null) {
            player.sendMessage(Component.text("You have no one to reply to.", NamedTextColor.DARK_RED));
            return true;
        }

        // Get the target player.
        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("Player not found or not online.", NamedTextColor.DARK_RED));
            return true;
        }

        // Construct the message from the arguments.
        String message = String.join(" ", args);

        // Check if the recipient has ignored the sender.
        if (ignoreManager.isIgnoring(targetUUID, senderUUID)) {
            player.sendMessage(Component.text("You cannot message this player.", NamedTextColor.DARK_RED));
            return true;
        }

        // Send the formatted message to the sender.
        TextComponent senderMessage = Component.text()
                .append(Component.text("You whisper to ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(target.getName(), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(": ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
                .build();

        player.sendMessage(senderMessage);

        // Send the formatted message to the recipient.
        TextComponent targetMessage = Component.text()
                .append(Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" whispers: ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
                .build();

        target.sendMessage(targetMessage);

        // Update last messaged players.
        messageManager.setLastMessaged(senderUUID, targetUUID);
        messageManager.setLastMessaged(targetUUID, senderUUID);

        return true;
    }
}
