package network.packets;

/**
 * Paquet envoyé par le client pour demander l'attaque sur un camp adversaire
* on envoi structure legere avec deux liste séparées, mais chaque ressource à un indice i
 * son correspendant dans l'autre liste se trouve aussi à l'indice i
 *
 */
public class PacketAttack {
    private String type;
    private int idCamp;
    private int[] idRessources;
    private int[] nbVikings;
    /**
     * @param type type de la ressource à attquer
     * @param idCamp id du camp adversaire
     * @param idRessources id des ressources à attaquer
     * @param nbVikings nombre de vikings à envoyer pour chaque ressource
     */
    public PacketAttack(String type, int idCamp, int[] idRessources, int[] nbVikings) {
        this.type = type;
        this.idCamp = idCamp;
        this.idRessources = idRessources;
        this.nbVikings = nbVikings;
    }

    public String getType() {
        return type;
    }

    public int getIdCamp() {
        return idCamp;
    }

    public int[] getIdRessources() {
        return idRessources;
    }

    public int[] getNbVikings() {
        return nbVikings;
    }
}
