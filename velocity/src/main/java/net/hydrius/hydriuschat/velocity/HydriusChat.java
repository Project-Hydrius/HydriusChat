package net.hydrius.hydriuschat.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.hydrius.hydriuschat.velocity.command.ChannelCommand;
import net.hydrius.hydriuschat.velocity.command.PrivateMessageCommand;
import net.hydrius.hydriuschat.velocity.config.Locale;
import net.hydrius.hydriuschat.velocity.listener.PluginMessageListener;
import net.hydrius.hydriuschat.velocity.listener.PlayerListener;
import net.hydrius.hydriuschat.velocity.manager.ChatManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "hydriuschat",
        name = "HydriusChat",
        version = "0.0.1",
        description = "A chat plugin for Hydrius",
        url = "flrp.dev, hydrius.net",
        authors = {"flrp"},
        dependencies = {
                @Dependency(id = "luckperms")
        }
)
public class HydriusChat {

    private final Logger logger;
    private final Path dataDirectory;
    private final ProxyServer proxyServer;

    private LuckPerms luckPerms;

    private ConfigurationNode config;
    private ConfigurationNode locale;

    private ChatManager chatManager;

    @Inject
    public HydriusChat(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        config = createConfig("config.yml");
        locale = createConfig("language.yml");

        if(config == null || locale == null) {
            logger.error("Failed to load config files");
        }

        new Locale(this).load();

        luckPerms = LuckPermsProvider.get();

        chatManager = new ChatManager(this);

        proxyServer.getEventManager().register(this, new PlayerListener(this));
        proxyServer.getEventManager().register(this, new PluginMessageListener(this));

        proxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.from("hydriuschat:main"));

        CommandManager commandManager = proxyServer.getCommandManager();
        commandManager.register(commandManager.metaBuilder("channel").aliases("channel").build(), new ChannelCommand(this));
        commandManager.register(commandManager.metaBuilder("message").aliases("dm", "w", "whisper", "pm", "msg").build(), new PrivateMessageCommand(this));

    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public ConfigurationNode getConfig() {
        return config;
    }

    public ConfigurationNode getLocale() {
        return locale;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    private ConfigurationNode createConfig(String fileName) {
        Path configFile = dataDirectory.resolve(fileName);
        if (Files.notExists(configFile)) {
            try {
                // Create the parent directories if they don't exist
                Files.createDirectories(configFile.getParent());

                // Copy the resource file to the config file
                Files.copy(getClass().getClassLoader().getResourceAsStream(fileName), configFile);
            } catch (IOException e) {
                logger.error("Failed to copy the resource config file", e);
            }
        }
        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(configFile).build();
        try {
            return loader.load();
        } catch (IOException e) {
            logger.error("Failed to load config file", e);
        }
        return null;
    }

}
