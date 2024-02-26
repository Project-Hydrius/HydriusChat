package net.hydrius.hydriuschat.velocity.util.containers;

import java.util.HashMap;

public class FormatGroup {

    private String name;
    private int weight;
    private final HashMap<String, ChatFormat> formats = new HashMap<>();

    public FormatGroup(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void addFormat(String name, ChatFormat format) {
        formats.put(name, format);
    }

    public void removeFormat(String name) {
        formats.remove(name);
    }

    public HashMap<String, ChatFormat> getFormats() {
        return formats;
    }

}
