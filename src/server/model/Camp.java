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
    private ArrayList<Livestock> livestock;
    private ArrayList<Vegetable> vegetables;

    // declare the constructor
    public Camp() {
        // initialize the instance variables
        vikings = new ArrayList<>();
        livestock = new ArrayList<>();
        vegetables = new ArrayList<>();
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
        Cow l1 = new Cow(100, new Point(10, 50), this.id , 6, this);
        Cow l2 = new Cow(100, new Point(30, 50), this.id,5,this);
        Cow l3 = new Cow(100, new Point(50, 50), this.id , 4 , this );

        Sheap s1 = new Sheap(100, new Point(5, 50), this.id , 6 , this );
        Sheap s2 = new Sheap(100, new Point(15, 50), this.id,5,this);
        Sheap s3 = new Sheap(100, new Point(35, 50), this.id,4,this);
        



        livestock.add(l1);
        livestock.add(l2);
        livestock.add(l3);

        livestock.add(s1);
        livestock.add(s2);
        livestock.add(s3) ;

        // add some vegetables
        Wheat v = new Wheat(100, new Point(70, 50), this.id);
        vegetables.add(v);
    }

    // declare the methods
    public void addViking(Warrior viking) {
        // add the viking to the vikings list
        vikings.add(viking);
    }

    public void addLivestock(Livestock l) {
        
        livestock.add(l);
        System.out.println("Un nouvel animal a été ajouté au camp !");
    }
    public void addVegetable(Vegetable vegetable) {
        vegetables.add(vegetable);
    }


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

    // Déplacement du bétail (simulation)
    public void moveLivestock() {
        for (Livestock l : livestock) {
            // Déplacement aléatoire (
            l.move(new Point(l.getPosition().x + 5, l.getPosition().y));
        }
    }

    public ArrayList<Livestock> getLivestock() {
        return livestock;
    }

    public ArrayList<Vegetable> getVegetables() {
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

