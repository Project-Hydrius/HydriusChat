package net.hydrius.hydriuschat.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.hydrius.hydriuschat.velocity.HydriusChat;
import net.hydrius.hydriuschat.velocity.config.Locale;
import net.hydrius.hydriuschat.velocity.util.containers.ChannelGroup;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ChannelCommand implements SimpleCommand {

    private final HydriusChat plugin;

    public ChannelCommand(HydriusChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        String[] args = invocation.arguments();

        if(args.length < 1) {
            source.sendMessage(Component.text(Locale.PREFIX + Locale.INVALID_USAGE.replace("{usage}", "/chat help")));
            return;
        }

        String action = args[0];
        switch (action) {
            case "swap", "s":
                swap(source, args);
                break;
            case "create", "c":
                create(source, args);
                break;
            case "delete", "d":
                delete(source, args);
                break;
            case "join", "j":
                join(source, args);
                break;
            case "leave", "l":
                leave(source, args);
                break;
            default:
        }

    }

    private void swap(CommandSource source, String[] args) {
        if(checks(source, args, "hydriuschat.channel.swap", "/chat swap <name>")) return;
        String channel = args[1];
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).setCurrentChannel(channel);
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_SWITCH.replace("{channel}", channel)));
        } else {
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", channel)));
        }
    }

    private void join(CommandSource source, String[] args) {
        if(checks(source, args, "hydriuschat.channel.join", "/chat join <name>")) return;
        String channel = args[1];
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).addChannel(channel, true);
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_GAIN_ACCESS.replace("{channel}", channel)));
        } else {
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", channel)));
        }
    }

    private void leave(CommandSource source, String[] args) {
        if(checks(source, args, "hydriuschat.channel.leave", "/chat leave <name>")) return;
        String channel = args[1];
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).removeChannel(channel);
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_LOSE_ACCESS.replace("{channel}", channel)));
        } else {
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", channel)));
        }
    }

    private void create(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.create", "/chat create <name> (pin)")) return;
        String channel = args[1];
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_ALREADY_EXISTS.replace("{channel}", channel)));
        } else {
            String pin = args.length > 2 ? args[2] : null;
            plugin.getChatManager().getChannelGroups().put(channel, new ChannelGroup(channel, channel, null, null, "&7", true, false, ((Player) source).getUniqueId(), pin));
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_CREATE.replace("{channel}", channel)));
        }
    }

    private void delete(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.delete", "/chat delete <name>")) return;
        String channel = args[1];
        if(plugin.getChatManager().getChannelGroups().containsKey(channel) && plugin.getChatManager().getChannelGroups().get(channel).getOwner().equals(((Player) source).getUniqueId())) {
            plugin.getChatManager().getChannelGroups().remove(channel);
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_DELETE.replace("{channel}", channel)));
        } else {
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", channel)));
        }
    }

    private boolean checks(CommandSource source, String[] args, String permission, String usage) {
        if(!(source instanceof Player)) {
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.NO_CONSOLE));
            return false;
        }
        if(!source.hasPermission(permission)) {
            source.sendMessage(parseWithColorCodes(Locale.PREFIX + Locale.NO_PERMISSION));
            return false;
        }
        if(args.length < 2) {
            source.sendMessage(parseWithColorCodes("[Chat] Invalid usage! /chat help"));
            return false;
        }
        return true;
    }

    private Component parseWithColorCodes(String messageFormat) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(messageFormat);
    }

}
