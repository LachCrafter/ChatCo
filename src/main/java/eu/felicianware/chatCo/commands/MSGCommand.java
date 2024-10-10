package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

/**
 *
 * @author lachcrafter
 *
 * The /w, /pm, /msg... private messaging system will be handled here.
 */
public class MSGCommand implements CommandExecutor {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Ensure the sender is a player.
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        // Check if the correct number of arguments are provided.
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: /" + label + " <player> <message>", NamedTextColor.RED));
            return true;
        }

        // Retrieve the target player.
        String targetName = args[0];
        Player target = player.getServer().getPlayerExact(targetName);

        // Check if the target player is online.
        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("Player not found or not online.", NamedTextColor.RED));
            return true;
        }

        // Construct the message from the arguments.
        String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
        String message = String.join(" ", messageArgs);

        UUID senderUUID = player.getUniqueId();
        UUID recipientUUID = target.getUniqueId();

        // Send the formatted message to the sender.
        TextComponent senderMessage = Component.text()
                .append(Component.text("You whisper to ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(target.getName(), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(": ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
                .build();

        player.sendMessage(senderMessage);

        // Check if the recipient has ignored the sender.
        if (ignoreManager.isIgnoring(recipientUUID, senderUUID)) {
            // Do not send the message to the recipient.
            return true;
        }

        // Send the formatted message to the recipient.
        TextComponent targetMessage = Component.text()
                .append(Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" whispers: ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
                .build();

        target.sendMessage(targetMessage);

        return true;
    }
}