package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import eu.felicianware.chatCo.managers.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Handles the /r /reply command.
 *
 * @author lachcrafter
 */
public class ReplyCommand implements CommandExecutor {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private final MessageManager messageManager = MessageManager.getInstance();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.DARK_RED));
            return true;
        }

        if (args.length < 1 ) {
            player.sendMessage(Component.text("Usage: /" + label + " <message>", NamedTextColor.DARK_RED));
            return true;
        }

        UUID senderUUID = player.getUniqueId();

        UUID targetUUID = messageManager.getLastMessaged(senderUUID);
        if (targetUUID == null) {
            player.sendMessage(Component.text("You have no one to reply to", NamedTextColor.DARK_RED));
            return true;
        }

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null || !player.isOnline()) {
            player.sendMessage(Component.text("Player not found or not online.", NamedTextColor.DARK_RED));
            return true;
        }

        String message = String.join(" ", args);

        if (ignoreManager.isIgnoring(targetUUID, senderUUID)) {
            player.sendMessage(Component.text("You cannot message this player.", NamedTextColor.DARK_RED));
            return true;
        }

        TextComponent senderMessage = Component.text()
                .append(Component.text("You whisper to ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(target.getName(), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(": ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
                .build();

        player.sendMessage(senderMessage);

        TextComponent targetMessage = Component.text()
                .append(Component.text(player.getName(), NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(" whispers: ", NamedTextColor.LIGHT_PURPLE))
                .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
                .build();

        target.sendMessage(targetMessage);

        messageManager.setLastMessaged(senderUUID, targetUUID);
        messageManager.setLastMessaged(targetUUID, senderUUID);

        return true;
    }

}
