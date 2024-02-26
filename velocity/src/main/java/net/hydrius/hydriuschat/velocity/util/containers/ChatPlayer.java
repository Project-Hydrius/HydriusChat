package net.hydrius.hydriuschat.velocity.util.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatPlayer {

    private final UUID uuid;
    private String currentChannel;
    private FormatGroup formatGroup;
    private HashMap<String, Boolean> channelList = new HashMap<>();
    private List<UUID> ignoredPlayers = new ArrayList<>();
    private UUID lastMessageSender;

    public ChatPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the UUID of the player.
     * @return The UUID of the player.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Get the current channel of the player.
     * @return The current channel of the player.
     */
    public String getCurrentChannel() {
        return currentChannel;
    }

    /**
     * Set the current channel of the player.
     * @param currentChannel The current channel of the player.
     */
    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    /**
     * Get the format group of the player.
     * @return The format group of the player.
     */
    public FormatGroup getFormatGroup() {
        return formatGroup;
    }

    /**
     * Set the format group of the player.
     * @param formatGroup The format group of the player.
     */
    public void setFormatGroup(FormatGroup formatGroup) {
        this.formatGroup = formatGroup;
    }

    /**
     * Get the list of channels the player is in.
     * @return The list of channels the player has access to.
     */
    public HashMap<String, Boolean> getChannelList() {
        return channelList;
    }

    /**
     * Set the list of channels the player is in.
     * @param channelList The list of channels the player has access to.
     */
    public void setChannelList(HashMap<String, Boolean> channelList) {
        this.channelList = channelList;
    }

    /**
     * Add a channel to the list of channels the player is in.
     * @param channel The channel to add to the list of channels the player has access to.
     */
    public void addChannel(String channel) {
        channelList.put(channel, true);
    }

    /**
     * Add a channel to the list of channels the player is in.
     * @param channel The channel to add to the list of channels the player has access to.
     * @param value If the player should be in the channel.
     */
    public void addChannel(String channel, boolean value) {
        channelList.put(channel, value);
    }

    /**
     * Remove a channel from the list of channels the player is in.
     * @param channel The channel to remove from the list of channels the player has access to.
     */
    public void removeChannel(String channel) {
        channelList.remove(channel);
    }

    /**
     * Check if the player is in a channel.
     * @param channel The channel to check if the player is in.
     * @return True if the player is in the channel, false if not.
     */
    public boolean isInChannel(String channel) {
        return channelList.containsKey(channel) && channelList.get(channel);
    }

    /**
     * Check if the player has access to a channel.
     * @param channel The channel to check if the player has access to.
     * @return True if the player has access to the channel, false if not.
     */
    public boolean hasAccessToChannel(String channel) {
        return channelList.containsKey(channel);
    }

    /**
     * Get the UUID of the last message sender.
     * @return The UUID of the last message sender.
     */
    public UUID getLastMessageSender() {
        return lastMessageSender;
    }

    /**
     * Set the UUID of the last message sender.
     * @param lastMessageSender The UUID of the last message sender.
     */
    public void setLastMessageSender(UUID lastMessageSender) {
        this.lastMessageSender = lastMessageSender;
    }

    /**
     * Get the list of players the player is ignoring.
     * @return The list of players the player is ignoring.
     */
    public List<UUID> getIgnoredPlayers() {
        return ignoredPlayers;
    }

    /**
     * Set the list of players the player is ignoring.
     * @param ignoredPlayers The list of players the player is ignoring.
     */
    public void setIgnoredPlayers(List<UUID> ignoredPlayers) {
        this.ignoredPlayers = ignoredPlayers;
    }

    /**
     * Add a player to the list of players the player is ignoring.
     * @param ignoredPlayer The player to add to the list of players the player is ignoring.
     */
    public void addIgnoredPlayer(UUID ignoredPlayer) {
        ignoredPlayers.add(ignoredPlayer);
    }

    /**
     * Remove a player from the list of players the player is ignoring.
     * @param ignoredPlayer The player to remove from the list of players the player is ignoring.
     */
    public void removeIgnoredPlayer(UUID ignoredPlayer) {
        ignoredPlayers.remove(ignoredPlayer);
    }

}