package server.model;



import java.awt.*;
import java.util.ArrayList;

/**
 * Classe représentant un cap viking, au début j'aivais mis les classes abstraites en type de listes
 * mais après ça faisait une erreur avec Gson, il n'acecptait pas les classes abstraites. Donc j'ai mis Viking Cow
 * Wheat pour le test juste.
 */
public class Camp {
    public static int lastId = 0;
    private final int id;
    private ArrayList<Warrior> vikings;
    private ArrayList<Cow> livestock;
    private ArrayList<Wheat> vegetables;

    // declare the constructor
    public Camp() {
        // initialize the instance variables
        vikings = new ArrayList<Warrior>();
        livestock = new ArrayList<Cow>();
        vegetables = new ArrayList<Wheat>();
        this.id = lastId++;
        init();

    }
    public void init() {
        // Les position sont relatives à la position du camp
        Warrior v1 = new Warrior(100, new Point(10, 10), this.id);
        Warrior v2 = new Warrior(100, new Point(30, 10), this.id);
        Warrior v3 = new Warrior(100, new Point(50, 10), this.id);
        vikings.add(v1);
        vikings.add(v2);
        vikings.add(v3);

        // add some livestock
        Cow l1 = new Cow(100, new Point(10, 50), this.id);
        Cow l2 = new Cow(100, new Point(30, 50), this.id);
        Cow l3 = new Cow(100, new Point(50, 50), this.id);
        livestock.add(l1);
        livestock.add(l2);
        livestock.add(l3);

        // add some vegetables
        Wheat v = new Wheat(100, new Point(70, 50), this.id);
        vegetables.add(v);
    }

    // declare the methods
    public void addViking(Warrior viking) {
        // add the viking to the vikings list
        vikings.add(viking);
    }

//    public void addLivestock(Livestock l) {
//        // add the livestock to the livestock list
//        livestock.add(l);
//    }
//
//    public void addVegetable(Vegetable v) {
//        // add the vegetable to the vegetable list
//        vegetables.add(v);
//    }

    public void removeViking(Viking viking) {
        // remove the viking from the vikings list
        vikings.remove(viking);
    }

    public void removeLivestock(Livestock l) {
        // remove the livestock from the livestock list
        livestock.remove(l);
    }

    public void removeVegetable(Vegetable v) {
        // remove the vegetable from the vegetable list
        vegetables.remove(v);
    }

    public void growVegetables() {
        // loop through the vegetable list
        for (Vegetable v : vegetables) {
            // grow the vegetable
            v.grow();
        }
    }

    public void moveLivestock() {
        // loop through the livestock list
        for (Livestock l : livestock) {
            // move the livestock

        }
    }

    public ArrayList<Cow> getLivestock() {
        return livestock;
    }

    public ArrayList<Wheat> getVegetables() {
        return vegetables;
    }

    public ArrayList<Warrior> getVikings() {
        return vikings;
    }

    public int getId() {
        return id;
    }

    // declare the toString method
    public String toString() {
        // return the list of vikings, livestock and vegetables
        return "Camp{" +
                "vikings=" + vikings +
                ", livestock=" + livestock +
                ", vegetables=" + vegetables +
                '}';
    }
}

