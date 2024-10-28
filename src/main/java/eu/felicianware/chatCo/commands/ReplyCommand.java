package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import eu.felicianware.chatCo.managers.MessageManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ReplyCommand implements BasicCommand {
    private final FileConfiguration config;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private final MessageManager messageManager = MessageManager.getInstance();

    public ReplyCommand(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, String[] args) {
        if (args.length < 1 ) {
            String usage = config.getString("usages.reply");
            usage = usage
                    .replace("%label%", "reply"); // TODO - get alias of command
            Component usageText = mm.deserialize(usage);
            stack.getExecutor().sendMessage(usageText);
            return;
        }

        UUID senderUUID = stack.getExecutor().getUniqueId();

        UUID targetUUID = messageManager.getLastMessaged(senderUUID);
        if (targetUUID == null) {

            Component noneReply = mm.deserialize(config.getString("messages.noneReply"));

            stack.getExecutor().sendMessage(noneReply);
        }

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null || !target.isOnline()) {
            Component notFound = mm.deserialize(config.getString("messages.playerNotFound"));
            stack.getExecutor().sendMessage(notFound);
        }

        String message = String.join(" ", args);

        String senderMessageRaw = config.getString("messages.whisperSender");
        senderMessageRaw = senderMessageRaw
                .replace("%player%", stack.getExecutor().getName())
                .replace("%message%", message);
        Component senderMessage = mm.deserialize(senderMessageRaw);

        stack.getExecutor().sendMessage(senderMessage);

        String targetMessageRaw = config.getString("messages.whisperTarget");
        targetMessageRaw = targetMessageRaw
                .replace("%player%", stack.getExecutor().getName())
                .replace("%message%", message);
        Component targetMessage = mm.deserialize(targetMessageRaw);

        target.sendMessage(targetMessage);

        messageManager.setLastMessaged(senderUUID, targetUUID);
        messageManager.setLastMessaged(targetUUID, senderUUID);
    }
}
