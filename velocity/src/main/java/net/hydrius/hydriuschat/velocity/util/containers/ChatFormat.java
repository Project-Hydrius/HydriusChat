package net.hydrius.hydriuschat.velocity.util.containers;

import net.hydrius.hydriuschat.velocity.util.component.ChatComponent;

import java.util.List;

public record ChatFormat(List<ChatComponent> components) {

    public String getFormat() {
        StringBuilder format = new StringBuilder();
        for (ChatComponent component : this.components) {
            format.append(component.getValue());
        }
        return format.toString();
    }

}
