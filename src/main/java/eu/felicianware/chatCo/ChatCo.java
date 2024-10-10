package eu.felicianware.chatCo;

import eu.felicianware.chatCo.commands.IgnoreCommand;
import eu.felicianware.chatCo.commands.IgnoreListCommand;
import eu.felicianware.chatCo.commands.MSGCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatCo extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the commands
        getCommand("msg").setExecutor(new MSGCommand());
        getCommand("ignore").setExecutor(new IgnoreCommand());
        getCommand("ignorelist").setExecutor(new IgnoreListCommand());

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
