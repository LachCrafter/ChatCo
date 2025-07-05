package eu.felicianware.chatCo.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import eu.felicianware.chatCo.managers.IgnoreManager;
import eu.felicianware.chatCo.managers.MessageManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        if (args.length < 2) {
            String usage = config.getString("usages.whisper");
            usage = usage
                    .replace("%label%", "whisper"); // TODO - Find way to find alias/label
            Component usageText = mm.deserialize(usage);
            stack.getExecutor().sendMessage(usageText);
            return;
        }

        String targetName = args[0];
        Player target = stack.getExecutor().getServer().getPlayerExact(targetName);

        if (target == null || !target.isOnline()) {

            Component notFound = mm.deserialize(config.getString("messages.playerNotFound"));
            stack.getExecutor().sendMessage(notFound);
            return;
        }

        String[] messageArgs = Arrays.copyOfRange(args, 1, args.length);
        String message = String.join(" ", messageArgs);

        UUID senderUUID = stack.getExecutor().getUniqueId();
        UUID recipientUUID = target.getUniqueId();

        String senderMessageRaw = config.getString("messages.whisperSender");
        senderMessageRaw = senderMessageRaw
                .replace("%player%", targetName)
                .replace("%message%", message);
        Component senderMessage = mm.deserialize(senderMessageRaw);

        if (ignoreManager.isIgnoring(recipientUUID, senderUUID)) {
            stack.getExecutor().sendMessage(senderMessage);
            return;
        }

        stack.getExecutor().sendMessage(senderMessage);

        String targetMessageRaw = config.getString("messages.whisperTarget");
        targetMessageRaw = targetMessageRaw
                .replace("%player%", stack.getSender().getName())
                .replace("%message%", message);
        Component targetMessage = mm.deserialize(targetMessageRaw);

        target.sendMessage(targetMessage);

        messageManager.setLastMessaged(senderUUID, recipientUUID);
        messageManager.setLastMessaged(recipientUUID, senderUUID);

    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, String @NotNull [] args) {
        if (args.length == 0) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(string -> string.startsWith(args[0]))
                .collect(Collectors.toList());
    }
}
