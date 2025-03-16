package network;

/**
 * Met en forme de PacketWrapper un paquet pour l'envoyer au serveur.
 * exemple d'utilisation
 * FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl())
 */
public class FormatPacket {
    public static String format(String type, String content) {
        return "{\"type\":\"" + type + "\",\"content\":" + content + "}";
    }
}
