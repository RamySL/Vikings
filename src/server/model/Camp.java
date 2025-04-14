package server.model;



import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Classe représentant un cap viking, au début j'aivais mis les classes abstraites en type de listes
 * mais après ça faisait une erreur avec Gson, il n'acecptait pas les classes abstraites. Donc j'ai mis Viking Cow
 * Wheat pour le test juste.<p>
 * !!!!!!!! On ne peut pas compter sur l'attribut static lastId pour avoir l'id du camp parceque gsoon quand on reçoit
 * du coté du client il va réexecuter le constructeur. On delesse la tache au serveur pour les id.
 */
public class Camp {
    //public static int lastId = 0;
    private final int id;
    private float strength; // Force du camp

    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Warrior> warriors = new ArrayList<>();
    private ArrayList<Viking> vikings = new ArrayList<>();
    private ArrayList<Farmer> farmers = new ArrayList<>();
    private ArrayList<Field> fields = new ArrayList<>();
    private ArrayList<Livestock> livestocks = new ArrayList<>();
    private ArrayList<Sheep> sheeps = new ArrayList<>();
    private ArrayList<Cow> cows = new ArrayList<>();
    private ArrayList<Vegetable> vegetables = new ArrayList<>();
    private ArrayList<Wheat> wheats = new ArrayList<>();

    // map entre les camp attaqué par les vikings de ce camp et les vikings qui l'attaquent
    private HashMap<Integer, ArrayList<Warrior>> vikingsAttack = new HashMap<>();
    private ArrayList<Warrior> warriorsInCamp = new ArrayList<>();
    // username du joueur qui détient ce camp
    private String username;

    // Constructeur
    public Camp(int id) {
        this.id = id;
        this.strength = 0;
        CampManager.addCamp(this);
        init();

    }

    /**
     * the init of the position is relative to the camp.
     */
    public void init() {
        // Ajout des guerriers
        Point topLeftCamp =  Position.MAP_CAMPS_POSITION.get(this.id);
        Warrior v1 = new Warrior(50,new Point(topLeftCamp.x + 10,topLeftCamp.y - 10), this.id);
        Warrior v2 = new Warrior(50,new Point(topLeftCamp.x + 30,topLeftCamp.y - 10), this.id);

        Farmer f1 = new Farmer(100, new Point(topLeftCamp.x + 10,topLeftCamp.y - 30), this.id);
        Farmer f2 = new Farmer(100, new Point(topLeftCamp.x + 30,topLeftCamp.y - 30), this.id);

        warriors.add(v1);
        warriors.add(v2);
        warriorsInCamp.add(v1);
        warriorsInCamp.add(v2);

        vikings.add(v1);
        vikings.add(v2);

        // add farmer
        farmers.add(f1);
        farmers.add(f2);

        vikings.add(f1);
        vikings.add(f2);

        Sheep s1 = new Sheep(20, new Point(topLeftCamp.x + 10,topLeftCamp.y - 50), this.id, 6);
        Sheep s2 = new Sheep(20, new Point(topLeftCamp.x + 30,topLeftCamp.y - 50), this.id, 5);
        sheeps.add(s1);
        sheeps.add(s2);


        Cow c1 = new Cow(100, new Point(topLeftCamp.x + 10,topLeftCamp.y - 70), this.id, 6/*,this*/);
        Cow c2 = new Cow(100, new Point(topLeftCamp.x + 30,topLeftCamp.y - 70), this.id, 5/*,this*/);
        cows.add(c1);
        cows.add(c2);
        // Ajout des cultures
        //Wheat v = new Wheat(100, new Point(70, 50), this.id, 0);
        Wheat v = new Wheat(100, new Point(topLeftCamp.x + 70,topLeftCamp.y - 50), this.id, 0);
        wheats.add(v);

        vegetables.add(v);

        Field fi1 = new Field(new Point(topLeftCamp.x + 15,topLeftCamp.y - 100), this.id);
        Field fi2 = new Field(new Point(topLeftCamp.x + 50,topLeftCamp.y - 100), this.id);
        fields.add(fi1);
        fields.add(fi2);

        // ajoute tout les objet de camp créer dans entities
        entities = new ArrayList<>();
        entities.addAll(warriors);
        entities.addAll(farmers);
        entities.addAll(sheeps);
        entities.addAll(wheats);

        livestocks.addAll(sheeps);
        livestocks.addAll(cows);

        this.setEntitiesId();

    }

    /**
     * Set the id of the entities in the camp.
     */
    public void setEntitiesId(){
        int i = 0;
        for (Viking viking : vikings) {
            viking.setId(id * 10 + i);
            i++;
        }
        for (Field field : fields) {
            field.setId(id * 10 + i);
            i++;
        }

        for (Livestock livestock : livestocks) {
            livestock.setId(id * 10 + i);
            i++;
        }

    }

    /**
     * envoi n warrior pour attquer le camp avec l'id id
     * Précondition:  le nombre de guerriers soit suffisant
     * @param n
     * @param id
     */
    public void attack(int n, int id, Point dst) {

        for (int i = 0; i < n; i++) {
            Warrior warrior = this.warriors.get(i);
            warrior.move(dst);
            // add the warrior to the vikingsAttack
            if (this.vikingsAttack.containsKey(id)) {
                this.vikingsAttack.get(id).add(warrior);
            } else {
                ArrayList<Warrior> warriors = new ArrayList<>();
                warriors.add(warrior);
                this.vikingsAttack.put(id, warriors);
            }
            warriorsInCamp.remove(i);
        }

    }
    public List<Point> getFieldPositions() {
        List<Point> positions = new ArrayList<>();
        for (Field field : fields) {
            positions.add(field.getPosition());
        }
        return positions;
    }

    /*public List<Field> getFields() {
        return fields;
    }*/

    /**
     * Augmente la force du camp en fonction de la nourriture consommée.
     */
    public void increaseStrength(float amount) {
        this.strength += amount;
        System.out.println("Force du camp augmentée ! Nouvelle force : " + strength);
    }

    public void vikingEats(Livestock animal) {
        // remove the livestock from the camp
        this.removeLiveStock(animal);
        // increase the strength of the camp
        this.increaseStrength(animal.getHealth() * Viking.getCoeffStrength());
    }

    /**
     * Ajoute un Viking au camp.
     */
    /*public void addViking(Viking viking) {
        vikings.add(viking);
    }*/
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
    public ArrayList<Warrior> getWarriors() {
        return warriors;
    }

    /**
     * Ajoute un animal d'élevage au camp.
     */
    public void addSheep(Sheep l) {
        sheeps.add(l);
        System.out.println("Un nouvel animal a été ajouté au camp !");
    }

    /**
     * Ajoute une culture au camp.
     */
    public void addWheat(Wheat vegetable) {
        wheats.add(vegetable);
    }

    /**
     * Supprime un Viking du camp.
     */
    /*public void removeViking(Viking viking) {
        vikings.remove(viking);
    }*/

    /**
     * Supprime un animal du camp.
     */

    public void removeLiveStock(Livestock l) {
        livestocks.remove(l);
        if (l instanceof Sheep) {
            sheeps.remove(l);
        } else if (l instanceof Cow) {
            cows.remove(l);
        }
        entities.remove(l);
    }

    /**
     * Supprime une culture du camp.
     */
    public void removeWheat(Wheat v) {
        vegetables.remove(v);
        wheats.remove(v);
        entities.remove(v);
    }

    /**
     * Fait grandir toutes les cultures.
     */
    public void growWheats() {
        // loop through the vegetable list
        for (Wheat v : wheats) {
            // grow the vegetable
            v.grow();
        }
    }

    public ArrayList<Sheep> getSheeps() {
        return sheeps;
    }

    /**
     * Retourne la liste des cultures du camp.
     */
    public ArrayList<Wheat> getWheats() {
        return wheats;
    }

    /**
     * Retourne la liste des Vikings du camp.
     */
    /*public ArrayList<Viking> getVikings() {
        return vikings;
    }*/

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
        // return the list of warriors, sheep and wheats
        return "Camp{" +
                /*"vikings=" + vikings +*/
                ", sheep=" + sheeps +
                ", wheats=" + wheats +
                ", fields= " + fields +
                ", strength=" + strength +
                '}';
    }
    public Point getPosition() {
        return Position.MAP_CAMPS_POSITION.get(this.id);
    }

    public ArrayList<Viking> getVikings() {
        return vikings;
    }

    public ArrayList<Warrior> getWarriorsInCamp() {
        return warriorsInCamp;
    }

    public ArrayList<Livestock> getLivestocks() {
        return livestocks;
    }

    public Viking getVikingByID(int id){
        for (Viking viking : vikings) {
            if (viking.getId() == id) {
                return viking;
            }
        }
        return null;
    }

    public Field getFieldByID(int id){
        System.out.println("getFieldByID: " + id);
        for (Field field : fields) {
            System.out.println("field id: " + field.getId());
            if (field.getId() == id) {
                return field;
            }
        }
        return null;
    }

    public Livestock getLivestockByID(int id){
        for (Livestock livestock : livestocks) {
            if (livestock.getId() == id) {
                return livestock;
            }
        }
        return null;
    }

    public Vegetable getVegetableByID(int id){
        for (Vegetable vegetable : vegetables) {
            if (vegetable.getId() == id) {
                return vegetable;
            }
        }
        return null;
    }

    public Entity getEntityByID(int id){
        for (Entity entity : entities) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Cow> getCows() {
        return cows;
    }


    public ArrayList<Livestock> getLivestocks() {
        return livestocks;
    }
}

