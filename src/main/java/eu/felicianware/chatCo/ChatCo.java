package eu.felicianware.chatCo;

import eu.felicianware.chatCo.commands.*;
import eu.felicianware.chatCo.managers.IgnoreManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public final class ChatCo extends JavaPlugin {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private File ignoreListFile;

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();
        saveDefaultConfig();
        LifecycleEventManager<@NotNull Plugin> manager = this.getLifecycleManager();

        // register commands
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("reload", "Reload the plugin configuration", new ReloadCommand(this, config));
            commands.register("ignorelist", "What players you are ignoring", new IgnoreListCommand(config));
            commands.register("ignore", "Ignore a player", new IgnoreCommand(config));
            commands.register(
                    "reply",
                    "Reply to a player",
                    List.of("r"),
                    new ReplyCommand(config)
            );
            commands.register(
                    "whisper",
                    "Whisper to a player",
                    List.of("w", "pm"),
                    new WhisperCommand(config)
            );
        });

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        // Create the data folder if it doesn't exist
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Initialize the ignore list file
        ignoreListFile = new File(getDataFolder(), "ignoreLists.yml");

        // Load ignore lists
        ignoreManager.loadIgnoreLists(ignoreListFile);
    }

    @Override
    public void onDisable() {
        ignoreManager.saveIgnoreLists(ignoreListFile);
    }

}
