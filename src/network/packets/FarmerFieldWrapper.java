package network.packets;

/**
 * FarmerFieldWrapper is a class that represents the coordinates of a farmer and a field.
 * It contains the x and y coordinates of both the farmer and the field, as well as a boolean indicating if the field is planted.
 * This class is used to wrap the information about the farmer and field coordinates together.
 */
public class FarmerFieldWrapper {
    private int idFarmer, idField;
    private boolean isPlanted;

    /**
     * Constructor for the FarmerFieldWrapper class.
     * @param isPlanted A boolean indicating if the field is planted.
     */
    public FarmerFieldWrapper(int idFarmer, int idField, boolean isPlanted) {
        this.idFarmer = idFarmer;
        this.idField = idField;
        this.isPlanted=isPlanted;
    }

    public int getIdFarmer() {
        return idFarmer;
    }


    public int getIdField() {
        return idField;
    }

    /**
     * @return A boolean indicating if the field is planted.
     */
    public boolean getIsPlanted() {
        return isPlanted;
    }
}