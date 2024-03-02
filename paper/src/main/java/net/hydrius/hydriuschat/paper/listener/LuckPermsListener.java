package net.hydrius.hydriuschat.paper.listener;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.hydrius.hydriuschat.paper.HydriusChat;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.event.node.NodeRemoveEvent;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.UUID;

public class LuckPermsListener implements Listener {

    private final HydriusChat plugin;

    public LuckPermsListener(HydriusChat plugin) {
        this.plugin = plugin;
        EventBus eventBus = plugin.getLuckPerms().getEventBus();

        eventBus.subscribe(plugin, NodeAddEvent.class, this::permissionAddEvent);
        eventBus.subscribe(plugin, NodeRemoveEvent.class, this::permissionRemoveEvent);
    }

    private void permissionAddEvent(NodeAddEvent event) {
        Node node = event.getNode();
        if(!node.getKey().startsWith("hydriuschat.channels.")) return;

        String channel = node.getKey().split("\\.")[2];
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("AddChannel");
        out.writeUTF(channel);

        Player player = Bukkit.getPlayer(UUID.fromString(event.getTarget().getIdentifier().getName()));
        if (player == null) return;
        player.sendPluginMessage(plugin, "hydriuschat:main", out.toByteArray());
    }

    private void permissionRemoveEvent(NodeRemoveEvent event) {
        Node node = event.getNode();
        if(!node.getKey().startsWith("hydriuschat.channels.")) return;

        String channel = node.getKey().split("\\.")[2];
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("RemoveChannel");
        out.writeUTF(channel);

        Player player = Bukkit.getPlayer(UUID.fromString(event.getTarget().getIdentifier().getName()));
        if (player == null) return;
        player.sendPluginMessage(plugin, "hydriuschat:main", out.toByteArray());
    }

}
