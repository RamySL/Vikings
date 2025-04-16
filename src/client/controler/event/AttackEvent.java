package client.controler.event;

public class AttackEvent {

    private int idCamp;
    private int[] idRessources;
    private int[] nbVikings;
    /**
     * @param idCamp id du camp adversaire
     * @param idRessources id des ressources à attaquer
     * @param nbVikings nombre de vikings à envoyer pour chaque ressource
     */
    public AttackEvent(int idCamp, int[] idRessources, int[] nbVikings) {
        this.idCamp = idCamp;
        this.idRessources = idRessources;
        this.nbVikings = nbVikings;
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
