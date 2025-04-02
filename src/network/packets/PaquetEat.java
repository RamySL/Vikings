package network.packets;


public class PaquetEat {
    private int vikingX, vikingY, animalX, animalY;

    public PaquetEat( int vikingX, int vikingY, int animalX, int animalY) {
        this.vikingX = vikingX;
        this.vikingY = vikingY;
        this.animalX = animalX;
        this.animalY = animalY;
    }


    public int getvikingX() {
        return vikingX;
    }

    public int getFarmerY() {
        return vikingY;
    }

    public int getFieldX() {
        return animalX;
    }

    public int getFieldY() {
        return animalY;
    }
}