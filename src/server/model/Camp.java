package server.model;



import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe représentant un cap viking, au début j'aivais mis les classes abstraites en type de listes
 * mais après ça faisait une erreur avec Gson, il n'acecptait pas les classes abstraites. Donc j'ai mis Viking Cow
 * Wheat pour le test juste.
 */
public class Camp {
    public static int lastId = 0;
    private final int id;
    private ArrayList<Warrior> warriors;
    private ArrayList<Cow> livestock;
    private ArrayList<Wheat> vegetables;
    private ArrayList<Field> fields;
    private ArrayList<Farmer> farmers;
    // declare the constructor
    public Camp() {
        // initialize the instance variables
        warriors = new ArrayList<Warrior>();
        livestock = new ArrayList<Cow>();
        vegetables = new ArrayList<Wheat>();
        fields = new ArrayList<>();
        farmers = new ArrayList<>();
        this.id = lastId++;
        CampManager.addCamp(this);
        init();

    }
    public void init() {
        // Les position sont relatives à la position du camp
        Warrior v1 = new Warrior(100, new Point(10, 10), this.id);
        Warrior v2 = new Warrior(100, new Point(30, 10), this.id);
        Warrior v3 = new Warrior(100, new Point(50, 10), this.id);
        warriors.add(v1);
        warriors.add(v2);
        warriors.add(v3);

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

        Field f1 = new Field(new Point(10, 100), this.id);
        Field f2 = new Field(new Point(30, 100), this.id);
        fields.add(f1);
        fields.add(f2);

        Farmer fa1 = new Farmer(100, new Point(10, 150), this.id);
        farmers.add(fa1);
    }

    public List<Point> getFieldPositions() {
        List<Point> positions = new ArrayList<>();
        for (Field field : fields) {
            positions.add(field.getPosition());
        }
        return positions;
    }

    // declare the methods
    public void addWarrior(Warrior warrior) {
        // add the viking to the warriors list
        warriors.add(warrior);
    }

    public void addFarmer(Farmer farmer) {
        // add the farmer to the farmers list
        farmers.add(farmer);
    }

    public ArrayList<Field> getFields() {
        return fields;
    }
    public ArrayList<Farmer> getFarmers() {
        return farmers;
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
        // remove the viking from the warriors list
        warriors.remove(viking);
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
        return warriors;
    }

    public int getId() {
        return id;
    }



    // declare the toString method
    public String toString() {
        // return the list of warriors, livestock and vegetables
        return "Camp{" +
                "warriors=" + warriors +
                ", livestock=" + livestock +
                ", vegetables=" + vegetables +
                ", fields= " + fields +
                '}';
    }
}

