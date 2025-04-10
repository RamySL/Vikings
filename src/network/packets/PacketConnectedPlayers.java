package network.packets;

/**
 * Paquet envoyé par le serveur pour informer le client qu'un joueur s'est connecté.
 * contient le pseudo du joueur et son ip
 *
 * Il ya un map entre le pseudo et l'ip (on  utilise un tableau primitif pour être le plus leger possible)
 */

public class PacketConnectedPlayers {
    private String[] pseudos;
    private String[] ips;

    public PacketConnectedPlayers(String[] pseudos, String[] ips) {
        this.pseudos = pseudos;
        this.ips = ips;
    }

    public String[] getIps() {
        return ips;
    }

    public String[] getPseudos() {
        return pseudos;
    }
}

