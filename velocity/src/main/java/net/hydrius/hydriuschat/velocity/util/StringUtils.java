package net.hydrius.hydriuschat.velocity.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class StringUtils {

    public static Component parseWithColorCodes(String messageFormat) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(messageFormat);
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
