package eu.felicianware.chatCo;

/**
 * @author lachcrafter
 *
 * This handles greenchat (messages that start with >)
 */

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        if (message.startsWith(">")) {
            String modifiedMessage = message.substring(1);
            Component greenMessage = Component.text(modifiedMessage)
                    .color(NamedTextColor.GREEN);
            event.message(greenMessage);
        }
    }
}
