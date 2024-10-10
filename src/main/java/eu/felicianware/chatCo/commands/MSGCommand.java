package eu.felicianware.chatCo.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 *
 * @author lachcrafter
 *
 * The /w, /pm, /msg... private messaging system will be handled here.
 */
public class MSGCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // Ensure the sender is a player.
        if (!(sender instanceof Player player)) {
            return false;
        }

        // Check if the correct number of arguments are provided.
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: /" + label + " <player> <message>", NamedTextColor.DARK_RED));
            return true;
        }

        // Retrieve the target player.
        String targetName = args[0];
        Player target = player.getServer().getPlayerExact(targetName);

        // Check if the target player is online.
        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("Player is not online.", NamedTextColor.DARK_RED));
            return true;
        }

        // Construct the message from the arguments.
        String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
        String message = String.join(" ", messageArgs);

        // Send the formatted messages
        TextComponent senderMessage = Component.text()
                .append(Component.text("You whisper to ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(target.getName(), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(": "))
                .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
                .build();

        TextComponent targetMessage = Component.text()
                .append(Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" whispers: ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
                .build();

        player.sendMessage(senderMessage);
        target.sendMessage(targetMessage);

        return true;
    }
}
