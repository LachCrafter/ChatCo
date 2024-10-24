package eu.felicianware.chatCo;

import eu.felicianware.chatCo.commands.*;
import eu.felicianware.chatCo.managers.IgnoreManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ChatCo extends JavaPlugin {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private File ignoreListFile;

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();
        saveDefaultConfig();

        // Register the commands
        getCommand("msg").setExecutor(new MSGCommand(config));
        getCommand("ignore").setExecutor(new IgnoreCommand(config));
        getCommand("ignorelist").setExecutor(new IgnoreListCommand(config));
        getCommand("r").setExecutor(new ReplyCommand(config));
        getCommand("coreload").setExecutor(new ReloadCommand(this));

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
