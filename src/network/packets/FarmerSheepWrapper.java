package network.packets;

/**
 * FarmerSheepWrapper is a class that represents the coordinates of a farmer and a sheep.
 * It contains the x and y coordinates of both the farmer and the sheep.
 * This class is used to wrap the information about the farmer and sheep coordinates together.
 */
public class FarmerSheepWrapper {
    private int idFarmer, idSheep;

    /**
     * Constructor for the FarmerSheepWrapper class.

     */
    public FarmerSheepWrapper(int idFarmer, int idSheep) {
        this.idFarmer = idFarmer;
        this.idSheep = idSheep;

    }

    public int getIdFarmer() {
        return idFarmer;
    }

    public int getIdSheep() {
        return idSheep;
    }
}