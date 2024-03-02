package net.hydrius.hydriuschat.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.hydrius.hydriuschat.velocity.HydriusChat;
import net.hydrius.hydriuschat.velocity.config.Locale;
import net.hydrius.hydriuschat.velocity.util.StringUtils;
import net.hydrius.hydriuschat.velocity.util.containers.ChannelGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.INVALID_USAGE.replace("{usage}", "/chat help")));
            return;
        }

        String action = args[0];
        switch (action) {
            case "swap", "switch":
                swap(source, args);
                break;
            case "create":
                create(source, args);
                break;
            case "delete":
                delete(source, args);
                break;
            case "add":
                add(source, args);
                break;
            case "remove":
                remove(source, args);
                break;
            case "join":
                join(source, args);
                break;
            case "leave":
                leave(source, args);
                break;
            case "list":
                list(source, args);
                break;
        }

    }

    private void swap(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.swap", "/channel swap <name>")) return;
        String channel = args[1].toLowerCase();
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).setCurrentChannel(channel);
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_SWITCH.replace("{channel}", StringUtils.capitalize(channel))));
        } else {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", StringUtils.capitalize(channel))));
        }
    }

    private void join(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.join", "/channel join <name>")) return;
        String channel = args[1].toLowerCase();
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).addChannel(channel, true);
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_JOIN.replace("{channel}", StringUtils.capitalize(channel))));
        } else {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", StringUtils.capitalize(channel))));
        }
    }

    private void leave(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.leave", "/channel leave <name>")) return;
        String channel = args[1].toLowerCase();
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).getChannelList().put(channel, false);
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_LEAVE.replace("{channel}", StringUtils.capitalize(channel))));
        } else {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", StringUtils.capitalize(channel))));
        }
    }

    private void add(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.add", "/channel add <name>")) return;
        String channel = args[1].toLowerCase();
        String pin = args.length > 2 ? args[2] : null;
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            if(plugin.getChatManager().getChannelGroups().get(channel).hasPin()) {
                if(pin == null) {
                    source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_PASSWORD_REQUIRED));
                    return;
                }
                if(!plugin.getChatManager().getChannelGroups().get(channel).getPin().equals(pin)) {
                    source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_PASSWORD_INCORRECT));
                    return;
                }
            }
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).addChannel(channel, true);
            plugin.getChatManager().getChannelGroups().get(channel).getSubscribers().add(((Player) source).getUniqueId());
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_ADD.replace("{channel}", StringUtils.capitalize(channel))));
        } else {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", StringUtils.capitalize(channel))));
        }
    }

    private void remove(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.remove", "/channel remove <name>")) return;
        String channel = args[1].toLowerCase();
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).removeChannel(channel);
            plugin.getChatManager().getChannelGroups().get(channel).getSubscribers().remove(((Player) source).getUniqueId());
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_REMOVE.replace("{channel}", StringUtils.capitalize(channel))));
        } else {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", StringUtils.capitalize(channel))));
        }
    }

    private void create(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.create", "/channel create <name> (pin)")) return;
        String channel = args[1].toLowerCase();
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_ALREADY_EXISTS.replace("{channel}", StringUtils.capitalize(channel))));
        } else {
            String pin = args.length > 2 ? args[2] : null;
            plugin.getChatManager().getChannelGroups().put(channel, new ChannelGroup(channel, StringUtils.capitalize(channel), null, null, "&7", true, false, ((Player) source).getUniqueId(), pin));
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).addChannel(channel, true);
            plugin.getChatManager().getChannelGroups().get(channel).getSubscribers().add(((Player) source).getUniqueId());
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_CREATE.replace("{channel}", StringUtils.capitalize(channel))));
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_JOIN.replace("{channel}", StringUtils.capitalize(channel))));
        }
    }

    private void list(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.list", "/channel list <all/mine>")) return;
        String option = args.length > 1 ? args[1] : "all";
        if(option.equalsIgnoreCase("all")) {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.CHANNEL_LIST.toString()));
            plugin.getChatManager().getChannelGroups().forEach((key, value) -> {
                source.sendMessage(StringUtils.parseWithColorCodes(Locale.CHANNEL_LIST_ITEM
                        .replace("{channel}", StringUtils.capitalize(value.getName()))
                        .replace("{playersInChannel}", String.valueOf(value.getSubscribers().size()))));
            });
        } else if (option.equalsIgnoreCase("mine")) {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.CHANNEL_LIST_PERSONAL.toString()));
            plugin.getChatManager().getChatPlayer(((Player) source).getUniqueId()).getChannelList().forEach((key, value) -> {
                source.sendMessage(StringUtils.parseWithColorCodes(Locale.CHANNEL_LIST_PERSONAL_ITEM
                        .replace("{channel}", StringUtils.capitalize(key))
                        .replace("{playersInChannel}", String.valueOf(plugin.getChatManager().getChannelGroups().get(key).getSubscribers().size()))
                        .replace("{status}", value ? Locale.STATUS_JOINED : Locale.STATUS_NOT_JOINED))
                );
            });
        }
    }

    private void delete(CommandSource source, String[] args) {
        if(!checks(source, args, "hydriuschat.channel.delete", "/channel delete <name>")) return;
        String channel = args[1].toLowerCase();
        if(plugin.getChatManager().getChannelGroups().containsKey(channel)) {
            if(plugin.getChatManager().getChannelGroups().get(channel).isServerOwned()) {
                source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_DELETE_DENIED_SERVER_OWNED));
                return;
            }
            if (plugin.getChatManager().getChannelGroups().get(channel).getOwner() != ((Player) source).getUniqueId()) {
                source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_DELETE_DENIED));
                return;
            }
            for(UUID uuid : plugin.getChatManager().getChannelGroups().get(channel).getSubscribers()) {
                plugin.getProxyServer().getPlayer(uuid).ifPresent(player -> {
                    plugin.getChatManager().getChatPlayer(player.getUniqueId()).removeChannel(channel);
                    player.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_LOST_ACCESS.replace("{channel}", StringUtils.capitalize(channel))));
                });
            }
            plugin.getChatManager().getChannelGroups().remove(channel);
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_DELETE.replace("{channel}", StringUtils.capitalize(channel))));
        } else {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.CHANNEL_NOT_FOUND.replace("{channel}", StringUtils.capitalize(channel))));
        }
    }

    private boolean checks(CommandSource source, String[] args, String permission, String usage) {
        if(!(source instanceof Player)) {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.NO_CONSOLE));
            return false;
        }
        if(!source.hasPermission(permission)) {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.NO_PERMISSION));
            return false;
        }
        if(args.length < 2) {
            source.sendMessage(StringUtils.parseWithColorCodes(Locale.PREFIX + Locale.INVALID_USAGE.replace("{usage}", usage)));
            return false;
        }
        return true;
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> suggestions = new ArrayList<>();
        suggestions.add("swap");
        suggestions.add("create");
        suggestions.add("delete");
        suggestions.add("add");
        suggestions.add("remove");
        suggestions.add("join");
        suggestions.add("leave");
        suggestions.add("list");
        return CompletableFuture.completedFuture(suggestions);
    }


}
