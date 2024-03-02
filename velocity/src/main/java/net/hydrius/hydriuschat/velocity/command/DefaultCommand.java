package net.hydrius.hydriuschat.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.hydrius.hydriuschat.velocity.HydriusChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class DefaultCommand implements SimpleCommand {

    private final HydriusChat plugin;

    public DefaultCommand(HydriusChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        String[] args = invocation.arguments();

        if(args.length > 0) {
            source.sendMessage(parseWithColorCodes("&bHydriusChat &lVelocity &7v1.0.0 &8- &7Created by flrp"));

            source.sendMessage(parseWithColorCodes("&f/chat help &8- &7Display this help message."));
            source.sendMessage(parseWithColorCodes("&f/channel join <name> &8- &7Join a channel."));
            source.sendMessage(parseWithColorCodes("&f/channel leave <name> &8- &7Leave a channel."));
            source.sendMessage(parseWithColorCodes("&f/channel add <name> (pin) &8- &7Add a channel."));
            source.sendMessage(parseWithColorCodes("&f/channel remove <name> &8- &7Remove a channel."));
            source.sendMessage(parseWithColorCodes("&f/channel swap <name> &8- &7Swap to a different channel."));
            source.sendMessage(parseWithColorCodes("&f/channel create <name> (pin) &8- &7Create a new channel."));
            source.sendMessage(parseWithColorCodes("&f/channel delete <name> &8- &7Delete a channel."));
            source.sendMessage(parseWithColorCodes("&f/channel list (all/mine) &8- &7List all available channels."));

        } else {
            source.sendMessage(parseWithColorCodes("&bHydriusChat &lVelocity &7v1.0.0 &8- &7Created by flrp"));
            source.sendMessage(parseWithColorCodes("&7Type &b/chat help &7for a list of commands."));
        }
    }

    private Component parseWithColorCodes(String messageFormat) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(messageFormat);
    }

}
