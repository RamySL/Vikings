package client.controler.event;


public class EatEvent {
    private int vikingX, vikingY, animalX, animalY;

    public EatEvent( int vikingX, int vikingY, int animalX, int animalY) {
        this.vikingX = vikingX;
        this.vikingY = vikingY;
        this.animalX = animalX;
        this.animalY = animalY;
    }


    public int getVikingX() {
        return vikingX;
    }

    public int getVikingY() {
        return vikingY;
    }

    public int getAnimalX() {
        return animalX;
    }

    public int getAnimalY() {
        return animalY;
    }
}