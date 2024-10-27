package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import eu.felicianware.chatCo.managers.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 *
 * @author lachcrafter
 *
 * The /w, /pm, /msg... private messaging system will be handled here.
 */
public class MSGCommandOld implements CommandExecutor {

    private final FileConfiguration config;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private final MessageManager messageManager = MessageManager.getInstance();

    public MSGCommandOld(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Ensure the sender is a player.
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        // Check if the correct number of arguments are provided.
        if (args.length < 2) {
            String usage = config.getString("usages.whisper");
            usage = usage
                    .replace("%label%", label);
            Component usageText = mm.deserialize(usage);
            player.sendMessage(usageText);
            return true;
        }

        // Retrieve the target player.
        String targetName = args[0];
        Player target = player.getServer().getPlayerExact(targetName);

        // Check if the target player is online.
        if (target == null || !target.isOnline()) {

            Component notFound = mm.deserialize(config.getString("messages.playerNotFound"));
            player.sendMessage(notFound);
            return true;
        }

        // Construct the message from the arguments.
        String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
        String message = String.join(" ", messageArgs);

        UUID senderUUID = player.getUniqueId();
        UUID recipientUUID = target.getUniqueId();

        String senderMessageRaw = config.getString("messages.whisperSender");
        senderMessageRaw = senderMessageRaw
                .replace("%player%", sender.getName())
                .replace("%message%", message);
        Component senderMessage = mm.deserialize(senderMessageRaw);

        player.sendMessage(senderMessage);

        // Check if the recipient has ignored the sender.
        if (ignoreManager.isIgnoring(recipientUUID, senderUUID)) {
            // Do not send the message to the recipient.
            return true;
        }

        // Send the formatted message to the recipient.

        String targetMessageRaw = config.getString("messages.whisperTarget");
        targetMessageRaw = targetMessageRaw
                .replace("%player%", sender.getName())
                .replace("%message%", message);
        Component targetMessage = mm.deserialize(targetMessageRaw);

        target.sendMessage(targetMessage);

        // Update last messaged players.
        messageManager.setLastMessaged(senderUUID, recipientUUID);
        messageManager.setLastMessaged(recipientUUID, senderUUID);

        return true;
    }
}