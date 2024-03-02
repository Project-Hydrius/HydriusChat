package net.hydrius.hydriuschat.velocity.api.event.channel;

import com.velocitypowered.api.proxy.Player;
import net.hydrius.hydriuschat.velocity.util.containers.ChannelGroup;

public class ChannelLeaveEvent extends ChannelEvent {

    private final Player player;

    public ChannelLeaveEvent(ChannelGroup channelGroup, Player player) {
        super(channelGroup);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
