package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import eu.felicianware.chatCo.managers.MessageManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class WhisperCommand implements BasicCommand {

    private final FileConfiguration config;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private final MessageManager messageManager = MessageManager.getInstance();

    public WhisperCommand(FileConfiguration config) {
        this.config = config;
    }


    @Override
    public void execute(@NotNull CommandSourceStack stack, String[] args) {
        // Check if the correct number of arguments are provided.
        if (args.length < 2) {
            String usage = config.getString("usages.whisper");
            usage = usage
                    .replace("%label%", "whisper"); // TODO - Find way to find alias/label
            Component usageText = mm.deserialize(usage);
            stack.getExecutor().sendMessage(usageText);
        }

        // Retrieve the target player.
        String targetName = args[0];
        Player target = stack.getExecutor().getServer().getPlayerExact(targetName);

        // Check if the target player is online.
        if (target == null || !target.isOnline()) {

            Component notFound = mm.deserialize(config.getString("messages.playerNotFound"));
            stack.getExecutor().sendMessage(notFound);
        }

        // Construct the message from the arguments.
        String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
        String message = String.join(" ", messageArgs);

        UUID senderUUID = stack.getExecutor().getUniqueId();
        UUID recipientUUID = target.getUniqueId();

        String senderMessageRaw = config.getString("messages.whisperSender");
        senderMessageRaw = senderMessageRaw
                .replace("%player%", stack.getSender().getName())
                .replace("%message%", message);
        Component senderMessage = mm.deserialize(senderMessageRaw);

        stack.getExecutor().sendMessage(senderMessage);

        if (ignoreManager.isIgnoring(recipientUUID, senderUUID)) { // TODO - Check this?
        }

        // Send the formatted message to the recipient.

        String targetMessageRaw = config.getString("messages.whisperTarget");
        targetMessageRaw = targetMessageRaw
                .replace("%player%", stack.getSender().getName())
                .replace("%message%", message);
        Component targetMessage = mm.deserialize(targetMessageRaw);

        target.sendMessage(targetMessage);

        // Update last messaged players.
        messageManager.setLastMessaged(senderUUID, recipientUUID);
        messageManager.setLastMessaged(recipientUUID, senderUUID);

    }
}
