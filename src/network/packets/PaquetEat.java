package network.packets;

/**
 * PaquetEat is a class that represents the coordinates of a farmer and an animal.
 * It contains the x and y coordinates of both the farmer and the animal.
 * This class is used to wrap the information about the farmer and animal coordinates together.
 */
public class PaquetEat {
    private int idViking;
    private int idAnimal;

    /**
     * Constructor for the PaquetEat class.
     * @param idViking The ID of the viking.
     * @param idAnimal The ID of the animal.
     */
    public PaquetEat(int idViking, int idAnimal) {
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