package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.ChatCo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final ChatCo plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ReloadCommand(ChatCo plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("chatco.admin")) {
            String noPermissionMessage = plugin.getConfig().getString("messages.noPermission");
            if (noPermissionMessage != null) {
                Component noPermission = mm.deserialize(noPermissionMessage);
                sender.sendMessage(noPermission);
            }
            return true;
        }

        plugin.reloadConfig();

        String reloadSuccessMessage = plugin.getConfig().getString("messages.reloadSuccess");
        if (reloadSuccessMessage != null) {
            Component successMessage = mm.deserialize(reloadSuccessMessage);
            sender.sendMessage(successMessage);
        }

        return true;
    }
}
