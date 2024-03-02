package net.hydrius.hydriuschat.velocity.config;

import net.hydrius.hydriuschat.velocity.HydriusChat;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.stream.Collectors;

public class Locale {

    private final HydriusChat plugin;

    public static String PREFIX;
    public static String CHANNEL_JOIN;
    public static String CHANNEL_LEAVE;
    public static String CHANNEL_ADD;
    public static String CHANNEL_REMOVE;
    public static String CHANNEL_SWITCH;
    public static String CHANNEL_CREATE;
    public static String CHANNEL_DELETE;
    public static String CHANNEL_GAIN_ACCESS;
    public static String CHANNEL_LOST_ACCESS;
    public static String CHANNEL_NOT_FOUND;
    public static String CHANNEL_ALREADY_EXISTS;
    public static String CHANNEL_NO_PERMISSION;
    public static String CHANNEL_SET_PASSWORD;
    public static String CHANNEL_REMOVED_PASSWORD;
    public static String CHANNEL_PASSWORD_INCORRECT;
    public static String CHANNEL_PASSWORD_REQUIRED;
    public static String CHANNEL_DELETE_DENIED;
    public static String CHANNEL_DELETE_DENIED_SERVER_OWNED;
    public static String CHANNEL_INFO;
    public static String CHANNEL_LIST;
    public static String CHANNEL_LIST_ITEM;
    public static String CHANNEL_LIST_PERSONAL;
    public static String CHANNEL_LIST_PERSONAL_ITEM;
    public static String STATUS_JOINED;
    public static String STATUS_NOT_JOINED;
    public static String CHANNEL_LIST_EMPTY;
    public static String CHANNEL_DOES_NOT_EXIST_MESSAGE_SENT;
    public static String CHANNEL_JOIN_AUTOMATIC;
    public static String NO_PERMISSION;
    public static String NO_CONSOLE;
    public static String INVALID_USAGE;

    public Locale(HydriusChat plugin) {
        this.plugin = plugin;
    }

    public void load() {
        // Plugin
        PREFIX = plugin.getLocale().getNode("prefix").getString();

        // Channel
        CHANNEL_JOIN = plugin.getLocale().getNode("channel-join").getString();
        CHANNEL_LEAVE = plugin.getLocale().getNode("channel-leave").getString();
        CHANNEL_ADD = plugin.getLocale().getNode("channel-add").getString();
        CHANNEL_REMOVE = plugin.getLocale().getNode("channel-remove").getString();
        CHANNEL_SWITCH = plugin.getLocale().getNode("channel-switch").getString();
        CHANNEL_CREATE = plugin.getLocale().getNode("channel-create").getString();
        CHANNEL_DELETE = plugin.getLocale().getNode("channel-delete").getString();
        CHANNEL_GAIN_ACCESS = plugin.getLocale().getNode("channel-gain-access").getString();
        CHANNEL_LOST_ACCESS = plugin.getLocale().getNode("channel-lost-access").getString();
        CHANNEL_NOT_FOUND = plugin.getLocale().getNode("channel-not-found").getString();
        CHANNEL_ALREADY_EXISTS = plugin.getLocale().getNode("channel-already-exists").getString();
        CHANNEL_NO_PERMISSION = plugin.getLocale().getNode("channel-no-permission").getString();
        CHANNEL_SET_PASSWORD = plugin.getLocale().getNode("channel-set-password").getString();
        CHANNEL_REMOVED_PASSWORD = plugin.getLocale().getNode("channel-removed-password").getString();
        CHANNEL_PASSWORD_INCORRECT = plugin.getLocale().getNode("channel-password-incorrect").getString();
        CHANNEL_PASSWORD_REQUIRED = plugin.getLocale().getNode("channel-password-required").getString();
        CHANNEL_DELETE_DENIED = plugin.getLocale().getNode("channel-delete-denied").getString();
        CHANNEL_DELETE_DENIED_SERVER_OWNED = plugin.getLocale().getNode("channel-delete-denied-server-owned").getString();
        CHANNEL_INFO = plugin.getLocale().getNode("channel-info").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.joining("\n"));
        CHANNEL_LIST = plugin.getLocale().getNode("channel-list").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.joining("\n"));
        CHANNEL_LIST_ITEM = plugin.getLocale().getNode("channel-list-item").getString();
        CHANNEL_LIST_PERSONAL = plugin.getLocale().getNode("channel-list-personal").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.joining("\n"));
        CHANNEL_LIST_PERSONAL_ITEM = plugin.getLocale().getNode("channel-list-personal-item").getString();
        STATUS_JOINED = plugin.getLocale().getNode("status-joined").getString();
        STATUS_NOT_JOINED = plugin.getLocale().getNode("status-not-joined").getString();
        CHANNEL_LIST_EMPTY = plugin.getLocale().getNode("channel-list-empty").getString();
        CHANNEL_DOES_NOT_EXIST_MESSAGE_SENT = plugin.getLocale().getNode("channel-does-not-exist-message-sent").getString();
        CHANNEL_JOIN_AUTOMATIC = plugin.getLocale().getNode("channel-join-automatic").getString();

        // Misc
        NO_PERMISSION = plugin.getLocale().getNode("no-permission").getString();
        NO_CONSOLE = plugin.getLocale().getNode("no-console").getString();
        INVALID_USAGE = plugin.getLocale().getNode("invalid-usage").getString();
    }

}
