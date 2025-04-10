package network.packets;

/**
 * Paquet envoyé par le client pour pour communiquer son pseudo au serveur
 */
public class PacketUsername {
    private String username;

    public PacketUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
