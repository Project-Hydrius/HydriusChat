package net.hydrius.hydriuschat.velocity.api.event.channel;

import com.velocitypowered.api.proxy.Player;
import net.hydrius.hydriuschat.velocity.util.containers.ChannelGroup;

public class ChannelDeleteEvent extends ChannelEvent {

    private final Player deleter;

    public ChannelDeleteEvent(ChannelGroup channelGroup, Player deleter) {
        super(channelGroup);
        this.deleter = deleter;
    }

    public Player getDeleter() {
        return deleter;
    }

}
