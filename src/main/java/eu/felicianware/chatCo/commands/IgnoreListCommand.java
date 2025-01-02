package eu.felicianware.chatCo.commands;

import eu.felicianware.chatCo.managers.IgnoreManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IgnoreListCommand implements BasicCommand {
    private final FileConfiguration config;
    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private final MiniMessage mm = MiniMessage.miniMessage();

    public IgnoreListCommand(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public void execute(CommandSourceStack stack, String @NotNull [] args) {

        List<String> ignoredNames = ignoreManager.getIgnoredPlayerNames(stack.getExecutor().getUniqueId());

        if (ignoredNames.isEmpty()) {
            Component ignoreListEmpty = mm.deserialize(config.getString("messages.ignoreListEmpty"));
            stack.getSender().sendMessage(ignoreListEmpty);
        } else {
            StringBuilder listBuilder = new StringBuilder();
            for (String name : ignoredNames) {
                listBuilder.append(name).append(", ");
            }

            String list = listBuilder.substring(0, listBuilder.length() - 2);

            String ignoreList = config.getString("messages.ignoreList");
            ignoreList = ignoreList
                    .replace("%list%", list);
            Component ignoring = mm.deserialize(ignoreList);

            stack.getSender().sendMessage(ignoring);
        }

    }
}
