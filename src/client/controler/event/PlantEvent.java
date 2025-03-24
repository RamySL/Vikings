package client.controler.event;


public class PlantEvent {
    private String resource;
    private int farmerX, farmerY, fieldX, fieldY;

    public PlantEvent( String resource, int farmerX, int farmerY, int fieldX, int fieldY) {
        this.resource = resource;
        this.farmerX = farmerX;
        this.farmerY = farmerY;
        this.fieldX = fieldX;
        this.fieldY = fieldY;
    }

    public String getResource() {
        return resource;
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
}