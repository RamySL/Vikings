package client.controler.event;


/** * EatEvent class represents an event where a Viking eats an animal.
 * It contains the coordinates of the Viking and the animal involved in the event.
 */
public class EatEvent {
    private int idViking;
    private int idAnimal;

    /**
     * Constructor for the PaquetEat class.
     * @param idViking The ID of the viking.
     * @param idAnimal The ID of the animal.
     */
    public EatEvent(int idViking, int idAnimal) {
        this.idViking = idViking;
        this.idAnimal = idAnimal;
    }
    /**
     * Getter for the ID of the viking.
     * @return The ID of the viking.
     */
    public int getIdViking() {
        return idViking;
    }
    /**
     * Getter for the ID of the animal.
     * @return The ID of the animal.
     */
    public int getIdAnimal() {
        return idAnimal;
    }
}