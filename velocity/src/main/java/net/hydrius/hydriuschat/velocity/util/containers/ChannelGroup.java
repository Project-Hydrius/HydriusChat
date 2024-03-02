package net.hydrius.hydriuschat.velocity.util.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelGroup {

    private String id;
    private String name;
    private String alias;
    private String permission;
    private String color;
    private boolean isCrossServer;
    private boolean requiresPermission;

    private UUID owner;
    private String pin;

    private final List<UUID> subscribers = new ArrayList<>();

    public ChannelGroup(String id, String name, String alias, String permission, String color, boolean isCrossServer, boolean requiresPermission, UUID owner, String pin) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.permission = permission;
        this.color = color;
        this.isCrossServer = isCrossServer;
        this.requiresPermission = requiresPermission;

        this.owner = owner;
        this.pin = pin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isCrossServer() {
        return isCrossServer;
    }

    public void setCrossServer(boolean crossServer) {
        this.isCrossServer = crossServer;
    }

    public boolean requiresPermission() {
        return requiresPermission;
    }

    public void setRequiresPermission(boolean requiresPermission) {
        this.requiresPermission = requiresPermission;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isServerOwned() {
        return owner == null;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean hasPin() {
        return pin != null;
    }

    public List<UUID> getSubscribers() {
        return subscribers;
    }

    public void addSubscriber(UUID uuid) {
        subscribers.add(uuid);
    }

    public void removeSubscriber(UUID uuid) {
        subscribers.remove(uuid);
    }

}

