package net.hydrius.hydriuschat.velocity.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.hydrius.hydriuschat.velocity.HydriusChat;
import net.hydrius.hydriuschat.velocity.config.Locale;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PluginMessageListener {

    private final HydriusChat plugin;

    public PluginMessageListener(HydriusChat plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    private void onPluginMessage(PluginMessageEvent event) {
        if (!(event.getSource() instanceof ServerConnection connection)) return;
        if(!event.getIdentifier().getId().equals("hydriuschat:main")) return;

        Player player = connection.getPlayer();

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();
        String channel = in.readUTF();

        System.out.println(plugin.getChatManager().getChannelGroups().containsKey(channel));
        if(!plugin.getChatManager().getChannelGroups().containsKey(channel)) return;

        switch (subChannel) {
            case "AddChannel":
                plugin.getChatManager().getChatPlayer(player.getUniqueId()).addChannel(channel, false);
                player.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_GAIN_ACCESS.replace("{channel}", channel)));
                break;
            case "RemoveChannel":
                plugin.getChatManager().getChatPlayer(player.getUniqueId()).removeChannel(channel);
                player.sendMessage(Component.text(Locale.PREFIX + Locale.CHANNEL_LOST_ACCESS.replace("{channel}", channel)));
                break;
        }
    }

    private Component parseWithColorCodes(String messageFormat) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(messageFormat);
    }

}
