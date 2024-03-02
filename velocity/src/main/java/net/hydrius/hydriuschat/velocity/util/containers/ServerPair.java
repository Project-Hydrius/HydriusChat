package net.hydrius.hydriuschat.velocity.util.containers;

import java.util.HashMap;
import java.util.List;

public class ServerPair {

    String name;
    HashMap<String, List<Integer>> servers = new HashMap<>();

    public ServerPair(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addServer(String address, int port) {
        if(servers.containsKey(address)) {
            servers.get(address).add(port);
        } else {
            servers.put(address, List.of(port));
        }
    }

    public String getServer(String address, int port) {
        if(servers.containsKey(address)) {
            if(servers.get(address).contains(port)) {
                return address + ":" + port;
            }
        }
        return null;
    }

    public HashMap<String, List<Integer>> getServers() {
        return servers;
    }

}
