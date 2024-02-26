package net.hydrius.hydriuschat.velocity.api.event.channel;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import net.hydrius.hydriuschat.velocity.util.containers.ChannelGroup;

public class ChannelSwapEvent implements ResultedEvent<ResultedEvent.GenericResult> {

    private final ChannelGroup to;
    private final ChannelGroup from;
    private final Player player;

    private ResultedEvent.GenericResult result = ResultedEvent.GenericResult.allowed();

    public ChannelSwapEvent(ChannelGroup to, ChannelGroup from, Player player) {
        this.to = to;
        this.from = from;
        this.player = player;
    }

    public ChannelGroup toChannel() {
        return to;
    }

    public ChannelGroup fromChannel() {
        return from;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    @Override
    public void setResult(GenericResult genericResult) {
        this.result = genericResult;
    }
}
