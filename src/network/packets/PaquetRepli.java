package network.packets;

/**
 * Paquet envoyé par le client pour demander un repli des vikings
 * depuis un camp attaqué vers leur camp d’origine.
 */
public class PaquetRepli {
    private int idCampAttaque; // L'ID du camp qui est actuellement attaqué

    /**
     * @param idCampAttaque ID du camp que mes vikings attaquent et depuis lequel ils doivent revenir
     */
    public PaquetRepli(int idCampAttaque) {
        this.idCampAttaque = idCampAttaque;
    }

    public int getIdCampAttaque() {
        return idCampAttaque;
    }
}