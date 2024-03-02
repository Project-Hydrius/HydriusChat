package net.hydrius.hydriuschat.velocity.api.event.channel;

import com.velocitypowered.api.proxy.Player;
import net.hydrius.hydriuschat.velocity.util.containers.ChannelGroup;

public class ChannelCreateEvent extends ChannelEvent {

    private final Player creator;

    public ChannelCreateEvent(ChannelGroup channelGroup, Player creator) {
        super(channelGroup);
        this.creator = creator;
    }

    public Player getCreator() {
        return creator;
    }

}
