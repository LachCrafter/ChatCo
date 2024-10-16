package eu.felicianware.chatCo;

import eu.felicianware.chatCo.commands.*;
import eu.felicianware.chatCo.managers.IgnoreManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ChatCo extends JavaPlugin {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();
    private File ignoreListFile;

    @Override
    public void onEnable() {
        // Register the commands
        getCommand("msg").setExecutor(new MSGCommand());
        getCommand("ignore").setExecutor(new IgnoreCommand());
        getCommand("ignorelist").setExecutor(new IgnoreListCommand());
        getCommand("r").setExecutor(new ReplyCommand());

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
