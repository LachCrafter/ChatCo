package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IgnoreCommand implements BasicCommand {
    private final FileConfiguration config;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();

    public IgnoreCommand(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, String[] args) {
        if (args.length != 1) {
            Component usage = mm.deserialize(config.getString("usages.ignore"));
            stack.getSender().sendMessage(usage);
            return;
        }

        String targetName = args[0];

        // Get the OfflinePlayer object.
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);
        if (target == null) {
            // Try to get the player by UUID or fetch them from the Mojang API.
            target = Bukkit.getOfflinePlayer(targetName);
            if (!target.hasPlayedBefore()) {
                Component playerNotFound = mm.deserialize(config.getString("messages.playerNotFound"));
                stack.getExecutor().sendMessage(playerNotFound);
            }
        }

        // Prevent players from ignoring themselves.
        if (stack.getExecutor().getUniqueId().equals(target.getUniqueId())) {
            Component ignoreThemselves = mm.deserialize(config.getString("messages.ignoreSelf"));
            stack.getExecutor().sendMessage(ignoreThemselves);
        }

        UUID playerUUID = stack.getExecutor().getUniqueId();
        UUID targetUUID = target.getUniqueId();

        // Toggle ignoring status.
        if (ignoreManager.isIgnoring(playerUUID, targetUUID)) {
            // Unignore the player.
            ignoreManager.unignorePlayer(playerUUID, targetUUID);
            String unignoringMessage = config.getString("messages.unignoring");
            unignoringMessage = unignoringMessage
                    .replace("%player%", target.getName());
            Component unignored = mm.deserialize(unignoringMessage);

            stack.getExecutor().sendMessage(unignored);

        } else {
            // Ignore the player.
            ignoreManager.ignorePlayer(playerUUID, targetUUID);
            String ignoringMessage = config.getString("messages.ignoring");
            ignoringMessage = ignoringMessage
                    .replace("%player%", target.getName());
            Component ignored = mm.deserialize(ignoringMessage);
            stack.getExecutor().sendMessage(ignored);
        }

    }
}
