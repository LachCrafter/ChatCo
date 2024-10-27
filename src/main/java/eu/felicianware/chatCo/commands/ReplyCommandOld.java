package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import eu.felicianware.chatCo.managers.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Handles the /r /reply command.
 *
 * @author lachcrafter
 */
public class ReplyCommandOld implements CommandExecutor {

    private final FileConfiguration config;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private final MessageManager messageManager = MessageManager.getInstance();

    public ReplyCommandOld(FileConfiguration config) {
        this.config = config;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.DARK_RED));
            return true;
        }

        if (args.length < 1 ) {
            String usage = config.getString("usages.reply");
            usage = usage
                    .replace("%label%", label);
            Component usageText = mm.deserialize(usage);
            player.sendMessage(usageText);
            return true;
        }

        UUID senderUUID = player.getUniqueId();

        UUID targetUUID = messageManager.getLastMessaged(senderUUID);
        if (targetUUID == null) {

            Component noneReply = mm.deserialize(config.getString("messages.noneReply"));

            player.sendMessage(noneReply);
            return true;
        }

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null || !target.isOnline()) {
            Component notFound = mm.deserialize(config.getString("messages.playerNotFound"));
            player.sendMessage(notFound);
            return true;
        }

        String message = String.join(" ", args);

        if (ignoreManager.isIgnoring(targetUUID, senderUUID)) {
            return true;
        }

        String senderMessageRaw = config.getString("messages.whisperSender");
        senderMessageRaw = senderMessageRaw
                .replace("%player%", sender.getName())
                .replace("%message%", message);
        Component senderMessage = mm.deserialize(senderMessageRaw);

        player.sendMessage(senderMessage);

        String targetMessageRaw = config.getString("messages.whisperTarget");
        targetMessageRaw = targetMessageRaw
                .replace("%player%", sender.getName())
                .replace("%message%", message);
        Component targetMessage = mm.deserialize(targetMessageRaw);

        target.sendMessage(targetMessage);

        messageManager.setLastMessaged(senderUUID, targetUUID);
        messageManager.setLastMessaged(targetUUID, senderUUID);

        return true;
    }

}
