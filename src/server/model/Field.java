package server.model;

import java.awt.*;
import java.util.stream.DoubleStream;

public class  Field {
    private Point position;
    private boolean isPlanted;
    private String resource;
    private int campId;
    private int id;
    private Vegetable vegetable;

    public Field(Point position, int campId, Vegetable vegetable) {
        this.position = position;
        this.isPlanted = false;
        this.campId = campId;
        this.resource = "";
        this.vegetable = vegetable;
    }

    public Point getPosition() {
        return position;
    }

    public boolean isPlanted() {
        return isPlanted;
    }

    public String getResource() {
        return resource;
    }

    public int getCampId() {
        return campId;
    }

    public void plant(String resource) {
        if (!isPlanted) {
            this.resource = resource;
            this.isPlanted = true;
            System.out.println("ressource "+ resource + " plantée");
            switch (resource) {
                case "Corn":
                    break;
                case "Wheat":
                    vegetable = new Wheat(0,this.position, this.campId, 1);
                    break;
                default:
                    System.out.println("Ressource inconnue");
            }
        }
    }

    /**
     * Post condition l'objet est un vegetable
     * @return
     */
    public void harvest(Camp camp) {
        if (isPlanted) {
            this.resource = "";
            this.isPlanted = false;
            camp.addRessource(this.vegetable);
            this.vegetable = new AbsenceVegetable();

        }
    }

    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // String avec nom de la classe
    @Override
    public String toString() {
        return "Field " + id;
    }

    public Vegetable getVegetable() {
        return (Vegetable) vegetable;
    }

    public void setPlanted(boolean planted) {
        isPlanted = planted;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}