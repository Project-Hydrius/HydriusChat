package net.hydrius.hydriuschat.velocity.api.event.channel;

import com.velocitypowered.api.event.ResultedEvent;
import net.hydrius.hydriuschat.velocity.util.containers.ChannelGroup;

import java.util.Objects;

public abstract class ChannelEvent implements ResultedEvent<ResultedEvent.GenericResult> {

    private final ChannelGroup channelGroup;
    private ResultedEvent.GenericResult result = ResultedEvent.GenericResult.allowed();

    public ChannelEvent(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = Objects.requireNonNull(result);
    }

}
