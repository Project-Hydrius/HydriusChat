package net.hydrius.hydriuschat.paper;

import net.hydrius.hydriuschat.paper.listener.LuckPermsListener;
import net.hydrius.hydriuschat.paper.listener.PlayerListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class HydriusChat extends JavaPlugin {

    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        luckPerms = LuckPermsProvider.get();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new LuckPermsListener(this), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "hydriuschat:main");
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

}
