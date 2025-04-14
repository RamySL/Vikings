package network.packets;

/**
 * FormatPacket is a utility class that provides a method to format packets for network communication.
 * It formats the packet as a JSON string with a specified type and content.
 * This class is used to create packets that can be sent over the network.
 */
public class FormatPacket {
    public static String format(String type, String content) {
        return "{\"type\":\"" + type + "\",\"content\":" + content + "}";
    }
}
