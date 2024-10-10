package eu.felicianware.chatCo;

/**
 * @author lachcrafter
 *
 * This handles greenchat (messages that start with >)
 * And this is a part of the ignoring system.
 */

import eu.felicianware.chatCo.managers.IgnoreManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;
import java.util.UUID;

public class ChatListener implements Listener {

    private final IgnoreManager ignoreManager = IgnoreManager.getInstance();

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        if (message.startsWith(">")) {
            Component greenMessage = Component.text(message)
                    .color(NamedTextColor.GREEN);
            event.message(greenMessage);
        }

        Player sender = event.getPlayer();
        UUID senderUUID = sender.getUniqueId();

        Set<Audience> viewers = event.viewers();

        viewers.removeIf(audience -> {
            if (audience instanceof Player recipient) {
                UUID recipientUUID = recipient.getUniqueId();
                return ignoreManager.isIgnoring(recipientUUID, senderUUID);
            }
            return false;
        });
    }
}
