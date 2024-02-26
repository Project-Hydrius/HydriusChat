package net.hydrius.hydriuschat.velocity.config;

import net.hydrius.hydriuschat.velocity.HydriusChat;

public class Locale {

    private final HydriusChat plugin;

    public static String PREFIX;
    public static String CHANNEL_JOIN;
    public static String CHANNEL_LEAVE;
    public static String CHANNEL_SWITCH;
    public static String CHANNEL_CREATE;
    public static String CHANNEL_DELETE;
    public static String CHANNEL_GAIN_ACCESS;
    public static String CHANNEL_LOSE_ACCESS;
    public static String CHANNEL_NOT_FOUND;
    public static String CHANNEL_ALREADY_EXISTS;
    public static String CHANNEL_NO_PERMISSION;
    public static String CHANNEL_SET_PASSWORD;
    public static String CHANNEL_REMOVED_PASSWORD;
    public static String CHANNEL_PASSWORD_INCORRECT;
    public static String CHANNEL_PASSWORD_REQUIRED;
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
        CHANNEL_SWITCH = plugin.getLocale().getNode("channel-switch").getString();
        CHANNEL_CREATE = plugin.getLocale().getNode("channel-create").getString();
        CHANNEL_DELETE = plugin.getLocale().getNode("channel-delete").getString();
        CHANNEL_GAIN_ACCESS = plugin.getLocale().getNode("channel-gain-access").getString();
        CHANNEL_LOSE_ACCESS = plugin.getLocale().getNode("channel-lose-access").getString();
        CHANNEL_NOT_FOUND = plugin.getLocale().getNode("channel-not-found").getString();
        CHANNEL_ALREADY_EXISTS = plugin.getLocale().getNode("channel-already-exists").getString();
        CHANNEL_NO_PERMISSION = plugin.getLocale().getNode("channel-no-permission").getString();
        CHANNEL_SET_PASSWORD = plugin.getLocale().getNode("channel-set-password").getString();
        CHANNEL_REMOVED_PASSWORD = plugin.getLocale().getNode("channel-removed-password").getString();
        CHANNEL_PASSWORD_INCORRECT = plugin.getLocale().getNode("channel-password-incorrect").getString();
        CHANNEL_PASSWORD_REQUIRED = plugin.getLocale().getNode("channel-password-required").getString();

        // Misc
        NO_PERMISSION = plugin.getLocale().getNode("no-permission").getString();
        NO_CONSOLE = plugin.getLocale().getNode("no-console").getString();
        INVALID_USAGE = plugin.getLocale().getNode("invalid-usage").getString();
    }

}
