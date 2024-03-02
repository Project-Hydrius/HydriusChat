package net.hydrius.hydriuschat.paper.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        // Remove all viewers
        event.viewers().clear();
    }

}
