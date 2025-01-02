package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.ChatCo;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements BasicCommand {
    private final ChatCo plugin;
    private final FileConfiguration config;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ReloadCommand(ChatCo plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public void execute(CommandSourceStack stack, String @NotNull [] args) {
        if (stack.getExecutor().hasPermission("chatco.admin")) {
            plugin.reloadConfig();
        } else {
            Component noPermissionText = mm.deserialize(config.getString("messages.noPermission"));
            stack.getSender().sendMessage(noPermissionText);
        }
    }
}
