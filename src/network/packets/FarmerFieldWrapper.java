package network.packets;

public class FarmerFieldWrapper {
    private int farmerX;
    private int farmerY;
    private int fieldX;
    private int fieldY;
    private boolean isPlanted;

    public FarmerFieldWrapper(int farmerX, int farmerY, int fieldX, int fieldY, boolean isPlanted) {
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.fieldX = fieldX;
        this.fieldY = fieldY;
        this.isPlanted=isPlanted;
    }

    public int getFarmerX() {
        return farmerX;
    }

    public int getFarmerY() {
        return farmerY;
    }

    public int getFieldX() {
        return fieldX;
    }

    public int getFieldY() {
        return fieldY;
    }

    public boolean getIsPlanted() {
        return isPlanted;
    }
}