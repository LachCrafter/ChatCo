package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

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

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);

        if (target == null) {
            Component playerNotFound = mm.deserialize(config.getString("messages.playerNotFound"));
            stack.getExecutor().sendMessage(playerNotFound);
            return;
        }

        if (stack.getExecutor().getUniqueId().equals(target.getUniqueId())) {
            Component ignoreThemselves = mm.deserialize(config.getString("messages.ignoreSelf"));
            stack.getExecutor().sendMessage(ignoreThemselves);
            return;
        }

        UUID playerUUID = stack.getExecutor().getUniqueId();
        UUID targetUUID = target.getUniqueId();

        if (ignoreManager.isIgnoring(playerUUID, targetUUID)) {
            ignoreManager.unignorePlayer(playerUUID, targetUUID);
            String unignoringMessage = config.getString("messages.unignoring");
            unignoringMessage = unignoringMessage.replace("%player%", target.getName());
            Component unignored = mm.deserialize(unignoringMessage);
            stack.getExecutor().sendMessage(unignored);
        } else {
            ignoreManager.ignorePlayer(playerUUID, targetUUID);
            String ignoringMessage = config.getString("messages.ignoring");
            ignoringMessage = ignoringMessage.replace("%player%", target.getName());
            Component ignored = mm.deserialize(ignoringMessage);
            stack.getExecutor().sendMessage(ignored);
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, String @NotNull [] args) {
        if (args.length == 0) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(string -> string.startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}