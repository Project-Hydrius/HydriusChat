package net.hydrius.hydriuschat.velocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.hydrius.hydriuschat.velocity.HydriusChat;
import net.hydrius.hydriuschat.velocity.config.Locale;
import net.hydrius.hydriuschat.velocity.util.StringUtils;
import net.hydrius.hydriuschat.velocity.util.containers.ChatPlayer;
import net.hydrius.hydriuschat.velocity.util.containers.ServerPair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.william278.papiproxybridge.api.PlaceholderAPI;

import java.time.Duration;
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
        String serverNameSplit = serverName.substring(0, serverName.lastIndexOf(plugin.getChatManager().getPairSplitter()));
        String serverIP = currentServer.getServerInfo().getAddress().getHostName();
        int serverPort = currentServer.getServerInfo().getAddress().getPort();

        Player player = event.getPlayer();
        ChatPlayer chatPlayer = plugin.getChatManager().getChatPlayer(player.getUniqueId());

        if(!plugin.getChatManager().getChannelGroups().containsKey(chatPlayer.getCurrentChannel())) {
            player.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_DOES_NOT_EXIST_MESSAGE_SENT));
            event.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        // Build the message format and the message sent.
        String messageFormat = chatPlayer.getFormatGroup().getFormats().containsKey(chatPlayer.getCurrentChannel()) ?
                chatPlayer.getFormatGroup().getFormats().get(chatPlayer.getCurrentChannel()).getFormat() : chatPlayer.getFormatGroup().getFormats().get("default").getFormat();
        String messageSent = event.getMessage();

        PlaceholderAPI placeholderAPI = PlaceholderAPI.createInstance();
        messageFormat = messageFormat.replace("{server_name}", StringUtils.capitalize(serverNameSplit));
        messageFormat = messageFormat.replace("{channel_name}", StringUtils.capitalize(plugin.getChatManager().getChannelGroups().get(chatPlayer.getCurrentChannel()).getName()));
        messageFormat = messageFormat.replace("{channel_color}", plugin.getChatManager().getChannelGroups().get(chatPlayer.getCurrentChannel()).getColor());
        messageFormat = messageFormat.replace("{message}", messageSent);

        // If the server exists in a pair, send the message to the other servers in the pair.
        if(plugin.getChatManager().getGlobalPairs().containsKey(serverNameSplit) && plugin.getChatManager().getChannelGroups().get(chatPlayer.getCurrentChannel()).isCrossServer()) {
            ServerPair serverPair = plugin.getChatManager().getGlobalPairs().get(serverNameSplit);
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

        } else
        // If the server has no pair and is cross-server, send the message to all subscribers.
        if(plugin.getChatManager().getChannelGroups().get(chatPlayer.getCurrentChannel()).isCrossServer() && !plugin.getChatManager().getGlobalPairs().containsKey(serverNameSplit)) {
            String finalMessageFormat = messageFormat;
            plugin.getChatManager().getChannelGroups().get(chatPlayer.getCurrentChannel()).getSubscribers().forEach(uuid -> {
                Player p = plugin.getProxyServer().getPlayer(uuid).orElse(null);
                if(p != null) {
                    if(plugin.getChatManager().getChatPlayers().get(p.getUniqueId()).getChannelList().containsKey(chatPlayer.getCurrentChannel()) && plugin.getChatManager().getChatPlayers().get(p.getUniqueId()).getChannelList().get(chatPlayer.getCurrentChannel())) {
                        placeholderAPI.formatPlaceholders(finalMessageFormat, player.getUniqueId()).thenAccept(formattedMessage -> p.sendMessage(parseWithColorCodes(formattedMessage)));
                    }
                }
            });
        // If the server is not cross-server, send the message to all players on the server.
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
            boolean autoJoin = !value.requiresPermission() || player.hasPermission(value.getPermission());
            chatPlayer.addChannel(value.getId(), autoJoin);
            plugin.getChatManager().getChannelGroups().get(value.getId()).getSubscribers().add(player.getUniqueId());
        });

        // Send autojoin message
        if(!chatPlayer.getChannelList().isEmpty()) {
            plugin.getProxyServer().getScheduler().buildTask(plugin, () -> {
                player.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_JOIN_AUTOMATIC.replace("{channels}",
                        chatPlayer.getChannelList().keySet().stream().map(s -> plugin.getChatManager().getChannelGroups().get(s).getName()).reduce((s1, s2) -> s1 + ", " + s2).orElse(""))));
            }).delay(Duration.ofSeconds(plugin.getConfig().getNode("settings").getNode("autojoin-message-delay").getInt())).schedule();
        }

       // Set current channel
       chatPlayer.setCurrentChannel(plugin.getChatManager().getChannelGroups().get(plugin.getConfig().getNode("settings").getNode("default-channel").getString()).getId());
       plugin.getChatManager().addChatPlayer(chatPlayer);
    }
}


