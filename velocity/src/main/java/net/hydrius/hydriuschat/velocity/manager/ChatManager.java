package net.hydrius.hydriuschat.velocity.manager;

import net.hydrius.hydriuschat.velocity.HydriusChat;
import net.hydrius.hydriuschat.velocity.util.component.ChatComponent;
import net.hydrius.hydriuschat.velocity.util.containers.*;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatManager {

    private final HydriusChat plugin;
    private final HashMap<UUID, ChatPlayer> chatPlayers = new HashMap<>();

    private final HashMap<String, ChannelGroup> channelGroups = new HashMap<>();
    private final HashMap<String, FormatGroup> chatGroups = new HashMap<>();
    private final HashMap<String, ServerPair> globalPairs = new HashMap<>();

    public ChatManager(HydriusChat plugin) {
        this.plugin = plugin;
        buildChannels();
        buildGroups();
        buildPairs();
    }

    // ChatPlayer methods
    public HashMap<UUID, ChatPlayer> getChatPlayers() {
        return chatPlayers;
    }

    public ChatPlayer getChatPlayer(UUID uuid) {
        return chatPlayers.get(uuid);
    }

    public boolean hasChatPlayer(UUID uuid) {
        return chatPlayers.containsKey(uuid);
    }

    public void addChatPlayer(UUID uuid) {
        chatPlayers.put(uuid, new ChatPlayer(uuid));
    }

    public void addChatPlayer(ChatPlayer chatPlayer) {
        chatPlayers.put(chatPlayer.getUUID(), chatPlayer);
    }

    public void removeChatPlayer(UUID uuid) {
        chatPlayers.remove(uuid);
    }

    // Channel Group methods
    public HashMap<String, ChannelGroup> getChannelGroups() {
        return channelGroups;
    }

    // Chat Group methods
    public HashMap<String, FormatGroup> getChatGroups() {
        return chatGroups;
    }

    public HashMap<String, ServerPair> getGlobalPairs() {
        return globalPairs;
    }

    public FormatGroup getChatGroupByUUID(UUID uuid) {
        String group = "default";
        int weight = 0;

        for (Node node : plugin.getLuckPerms().getUserManager().getUser(uuid).resolveInheritedNodes(QueryOptions.nonContextual())) {
            if (!node.getKey().startsWith("hydriuschat.formats.")) continue;

            String g = node.getKey().substring(13);
            if (!chatGroups.containsKey(g)) continue;

            int w = chatGroups.get(g).getWeight();
            if (w < weight) continue;

            group = g;
            weight = w;
        }

        return chatGroups.get(group);
    }

    private void buildChannels() {
        plugin.getConfig().getNode("channels").getChildrenMap().forEach((key, value) -> {
            String id = key.toString();
            String name = value.getNode("name").getString();
            String alias = value.getNode("alias").getString();
            String permission = value.getNode("permission").getString();
            String color = value.getNode("color").getString();
            boolean isCrossServer = value.getNode("cross-server").getBoolean();
            boolean requiresPermission = value.getNode("requires-permission").getBoolean();
            channelGroups.put(id, new ChannelGroup(id, name, alias, permission, color, isCrossServer, requiresPermission, null, null));
            System.out.println("Channel: " + id + " " + name + " " + alias + " " + permission + " " + color + " " + isCrossServer + " " + requiresPermission);
        });
    }

    private void buildGroups() {
        plugin.getConfig().getNode("groups").getChildrenMap().forEach((key, value) -> {
            FormatGroup group = new FormatGroup(key.toString(), value.getNode("weight").getInt());
            value.getNode("formats").getChildrenMap().forEach((k, v) -> {
                String formatName = k.toString();
                List<ChatComponent> components = new ArrayList<>();
                v.getNode("components").getChildrenMap().forEach((k2, v2) -> {
                    String componentName = k2.toString();
                    String componentValue = v2.getString();
                    components.add(new ChatComponent(componentName, componentValue));
                });
                group.addFormat(formatName, new ChatFormat(components));
            });
            chatGroups.put(key.toString(), group);
            System.out.println("Group: " + key + " " + group.getWeight() + " " + group.getFormats().size());
        });
    }

    private void buildPairs() {
        plugin.getConfig().getNode("pairs").getChildrenMap().forEach((key, value) -> {
            String name = key.toString().split(" ")[0];
            ServerPair pair = new ServerPair(name);
            value.getChildrenList().forEach(address -> {
                String[] addressSplit = address.toString().substring(address.toString().lastIndexOf('=') + 1).split(":");
                String addressString = addressSplit[0];
                String portString = addressSplit[1].substring(0, addressSplit[1].length() - 2);
                if(addressString.equals("127.0.0.1")) addressString = "localhost";

                System.out.println("Pair: " + name + " " + addressString + " " + portString);

                if(pair.getServers().containsKey(addressString)) {
                    pair.getServers().get(addressString).add(Integer.parseInt(portString));
                } else {
                    pair.getServers().put(addressString, new ArrayList<>(List.of(Integer.parseInt(portString))));
                }
            });
            globalPairs.put(name, pair);
        });
    }

}
