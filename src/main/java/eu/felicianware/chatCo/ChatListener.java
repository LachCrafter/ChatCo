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
            Component greenMessage = Component.text(message)
                    .color(NamedTextColor.GREEN);
            event.message(greenMessage);
        }
    }
}
