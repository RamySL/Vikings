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
    private ArrayList<Warrior> warriors;
    private ArrayList<Field> fields;
    private ArrayList<Farmer> farmers;
    // declare the constructor
    private float strength; // Force du camp
    private ArrayList<Viking> vikings; // Liste des Vikings (Warrior et Farmer)
    private ArrayList<Livestock> livestock;
    private ArrayList<Vegetable> vegetables;

    // Constructeur
    public Camp() {
        // initialize the instance variables
        warriors = new ArrayList<Warrior>();
        fields = new ArrayList<>();
        farmers = new ArrayList<>();
        this.id = lastId++;
        this.strength = 0;
        vikings = new ArrayList<>();
        livestock = new ArrayList<>();
        vegetables = new ArrayList<>();
        CampManager.addCamp(this);
        init();

    }
    public void init() {
        // Ajout des guerriers
        Warrior v1 = new Warrior(100, new Point(10, 10), this.id, this);
        Warrior v2 = new Warrior(100, new Point(30, 10), this.id, this);
        Warrior v3 = new Warrior(100, new Point(50, 10), this.id, this);

        // Ajout des fermiers
        Farmer f1 = new Farmer(100, new Point(15, 10), this.id, this);
        Farmer f2 = new Farmer(100, new Point(25, 10), this.id, this);

        vikings.add(v1);
        vikings.add(v2);
        vikings.add(v3);
        vikings.add(f1);
        vikings.add(f2);

        // Ajout du bétail
        Cow l1 = new Cow(100, new Point(10, 50), this.id, 6, this);
        Cow l2 = new Cow(100, new Point(30, 50), this.id, 5, this);
        Cow l3 = new Cow(100, new Point(50, 50), this.id, 4, this);
        warriors.add(v1);
        warriors.add(v2);
        warriors.add(v3);

        Sheap s1 = new Sheap(100, new Point(5, 50), this.id, 6, this);
        Sheap s2 = new Sheap(100, new Point(15, 50), this.id, 5, this);
        Sheap s3 = new Sheap(100, new Point(35, 50), this.id, 4, this);

        livestock.add(l1);
        livestock.add(l2);
        livestock.add(l3);
        livestock.add(s1);
        livestock.add(s2);
        livestock.add(s3);

        // Ajout des cultures
        Wheat v = new Wheat(100, new Point(70, 50), this.id, 0);
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

    /**
     * Augmente la force du camp en fonction de la nourriture consommée.
     */
    public void increaseStrength(float amount) {
        this.strength += amount;
        System.out.println("Force du camp augmentée ! Nouvelle force : " + strength);
    }

    /**
     * Ajoute un Viking au camp.
     */
    public void addViking(Viking viking) {
        vikings.add(viking);
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

    /**
     * Ajoute un animal d'élevage au camp.
     */
    public void addLivestock(Livestock l) {
        livestock.add(l);
        System.out.println("Un nouvel animal a été ajouté au camp !");
    }

    /**
     * Ajoute une culture au camp.
     */
    public void addVegetable(Vegetable vegetable) {
        vegetables.add(vegetable);
    }

    /**
     * Supprime un Viking du camp.
     */
    public void removeViking(Viking viking) {
        vikings.remove(viking);
    }

    /**
     * Supprime un animal du camp.
     */
    public void removeLivestock(Livestock l) {
        // remove the livestock from the livestock list
        livestock.remove(l);
    }

    /**
     * Supprime une culture du camp.
     */
    public void removeVegetable(Vegetable v) {
        // remove the vegetable from the vegetable list
        vegetables.remove(v);
    }

    /**
     * Fait grandir toutes les cultures.
     */
    public void growVegetables() {
        // loop through the vegetable list
        for (Vegetable v : vegetables) {
            // grow the vegetable
            v.grow();
        }
    }

    /**
     * Déplace le bétail de façon aléatoire.
     */
    public void moveLivestock() {
        for (Livestock l : livestock) {
            l.move(new Point(l.getPosition().x + 5, l.getPosition().y));
        }
    }

    /**
     * Retourne la liste des animaux du camp.
     */
    public ArrayList<Livestock> getLivestock() {
        return livestock;
    }

    /**
     * Retourne la liste des cultures du camp.
     */
    public ArrayList<Vegetable> getVegetables() {
        return vegetables;
    }

    /**
     * Retourne la liste des Vikings du camp.
     */
    public ArrayList<Viking> getVikings() {
        return vikings;
    }

    /**
     * Retourne la force actuelle du camp.
     */
    public float getStrength() {
        return strength;
    }

    /**
     * Retourne l'ID du camp.
     */
    public int getId() {
        return id;
    }
/**
 * Diminue la force du camp lorsqu'il est attaqué.
 */
public void decreaseStrength(float amount) {
    this.strength -= amount;
    if (this.strength < 0) {
        this.strength = 0;
    }
    System.out.println("La force du camp " + id + " a diminué à : " + strength);
}



    // declare the toString method
    @Override
    public String toString() {
        // return the list of warriors, livestock and vegetables
        return "Camp{" +
                "vikings=" + vikings +
                ", livestock=" + livestock +
                ", vegetables=" + vegetables +
                ", fields= " + fields +
                ", strength=" + strength +
                '}';
    }
}

