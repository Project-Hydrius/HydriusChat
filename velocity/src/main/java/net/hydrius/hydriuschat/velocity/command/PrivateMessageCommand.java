package net.hydrius.hydriuschat.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.hydrius.hydriuschat.velocity.HydriusChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class PrivateMessageCommand implements SimpleCommand {

    private final HydriusChat plugin;

    public PrivateMessageCommand(HydriusChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if(!(source instanceof Player sender)) {
            source.sendMessage(Component.text("[Chat] You must be a player to use this command."));
            return;
        }

        if(args.length < 2) {
            source.sendMessage(Component.text("[Chat] Invalid usage! /chat help"));
            return;
        }

        if(plugin.getProxyServer().getPlayer(args[0]).isEmpty()) {
            source.sendMessage(Component.text("[Chat] The player " + args[0] + " is not online."));
            return;
        }

        Player receiver = plugin.getProxyServer().getPlayer(args[0]).get();
        String senderFormat = plugin.getConfig().getNode("private-messages").getNode("sender").getString();
        String receiverFormat = plugin.getConfig().getNode("private-messages").getNode("receiver").getString();
        String message = String.join(" ", args).replace(args[0], "").trim();

        sender.sendMessage(parseWithColorCodes(senderFormat.replace("{receiver}", receiver.getUsername()).replace("{message}", message)));
        receiver.sendMessage(parseWithColorCodes(receiverFormat.replace("{sender}", sender.getUsername()).replace("{message}", message)));

    }

    private Component parseWithColorCodes(String messageFormat) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(messageFormat);
    }

}
