package net.hydrius.hydriuschat.velocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.hydrius.hydriuschat.velocity.HydriusChat;
import net.hydrius.hydriuschat.velocity.util.containers.ChatPlayer;
import net.hydrius.hydriuschat.velocity.util.containers.FormatGroup;
import net.hydrius.hydriuschat.velocity.util.containers.ServerPair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.william278.papiproxybridge.api.PlaceholderAPI;

import java.util.List;
import java.util.Map;


public class PlayerListener {

    private final HydriusChat plugin;

    public PlayerListener(HydriusChat plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();
        managePlayer(player);
    }

    @Subscribe(order = PostOrder.LATE)
    public void onPlayerChat(PlayerChatEvent event) {
        if(!event.getResult().isAllowed()) return;

        // First, grab all the info we need from the player and server.
        ServerConnection currentServer = event.getPlayer().getCurrentServer().get();
        String serverName = currentServer.getServerInfo().getName();
        String serverIP = currentServer.getServerInfo().getAddress().getHostName();
        int serverPort = currentServer.getServerInfo().getAddress().getPort();

        Player player = event.getPlayer();
        ChatPlayer chatPlayer = plugin.getChatManager().getChatPlayer(player.getUniqueId());

        // Build the message format and the message sent.
        String messageFormat = chatPlayer.getFormatGroup().getFormats().containsKey(chatPlayer.getCurrentChannel()) ?
                chatPlayer.getFormatGroup().getFormats().get(chatPlayer.getCurrentChannel()).getFormat() : chatPlayer.getFormatGroup().getFormats().get("default").getFormat();
        String messageSent = event.getMessage();

        PlaceholderAPI placeholderAPI = PlaceholderAPI.createInstance();
        messageFormat = messageFormat.replace("{server_name}", serverName.substring(0, 1).toUpperCase() + serverName.substring(1));
        messageFormat = messageFormat.replace("{channel_name}", plugin.getChatManager().getChannelGroups().get(chatPlayer.getCurrentChannel()).getName());
        messageFormat = messageFormat.replace("{channel_color}", plugin.getChatManager().getChannelGroups().get(chatPlayer.getCurrentChannel()).getColor());
        messageFormat = messageFormat.replace("{message}", messageSent);

        // If the server exists in a pair, send the message to the other servers in the pair.
        // If not, send the message to the current server.
        if(plugin.getChatManager().getGlobalPairs().containsKey(serverName) && plugin.getChatManager().getChannelGroups().get(chatPlayer.getCurrentChannel()).isCrossServer()) {
            ServerPair serverPair = plugin.getChatManager().getGlobalPairs().get(serverName);
            if(serverPair.getServer(serverIP, serverPort) == null) return;

            for (Map.Entry<String, List<Integer>> entry : serverPair.getServers().entrySet()) {
                String address = entry.getKey();
                List<Integer> ports = entry.getValue();
                for (int port : ports) {
                    RegisteredServer server = plugin.getProxyServer().getAllServers().stream()
                            .filter(s -> s.getServerInfo().getAddress().getHostName().equals(address) && s.getServerInfo().getAddress().getPort() == port)
                            .findFirst().orElse(null);
                    if(server != null) {
                        String finalMessageFormat = messageFormat;
                        server.getPlayersConnected().forEach(p -> {
                            if(plugin.getChatManager().getChatPlayer(p.getUniqueId()).getChannelList().containsKey(chatPlayer.getCurrentChannel()) && plugin.getChatManager().getChatPlayer(p.getUniqueId()).getChannelList().get(chatPlayer.getCurrentChannel())) {
                                placeholderAPI.formatPlaceholders(finalMessageFormat, player.getUniqueId()).thenAccept(formattedMessage -> p.sendMessage(parseWithColorCodes(formattedMessage)));
                            }
                        });
                    }
                }
            }

        } else {
            placeholderAPI.formatPlaceholders(messageFormat, player.getUniqueId()).thenAccept(formattedMessage -> currentServer.getServer().sendMessage(parseWithColorCodes(formattedMessage)));
        }

        event.setResult(PlayerChatEvent.ChatResult.denied());
    }

    private Component parseWithColorCodes(String messageFormat) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(messageFormat);
    }

    private void managePlayer(Player player) {
        ChatPlayer chatPlayer = new ChatPlayer(player.getUniqueId());

        // Set format group
        chatPlayer.setFormatGroup(plugin.getChatManager().getChatGroupByUUID(player.getUniqueId()));

        // Set channels
        plugin.getChatManager().getChannelGroups().forEach((key, value) -> {
            chatPlayer.addChannel(value.getId(), !value.requiresPermission() || player.hasPermission(value.getPermission()));
            System.out.println(player.getUsername() + "  Channel: " + value.getId() + " Has Permission: " + player.hasPermission(value.getPermission()) + " Requires Permission: " + value.requiresPermission() + " Permission: " + value.getPermission());
        });

        // Set current channel
        chatPlayer.setCurrentChannel(plugin.getChatManager().getChannelGroups().get(plugin.getConfig().getNode("settings").getNode("default-channel").getString()).getId());

        plugin.getChatManager().addChatPlayer(chatPlayer);
    }
}


