package server.model;



import network.server.ThreadCommunicationServer;

import java.awt.*;
import java.util.*;
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
    private ArrayList<Entity> ressources = new ArrayList<>();
    private ArrayList<Enclos> enclosses = new ArrayList<>();

    // map entre les camp attaqué par les vikings de ce camp et les vikings qui l'attaquent
    private HashMap<Integer, ArrayList<Warrior>> vikingsAttack = new HashMap<>();
    private ArrayList<Warrior> warriorsInCamp = new ArrayList<>();
    // username du joueur qui détient ce camp
    private String username;
    private int lastId;
    private transient ThreadCommunicationServer threadCommunicationServer;
    Object regenLock = new Object();


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
        Point topLeftCamp = Position.MAP_CAMPS_POSITION.get(this.id);
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(Position.WIDTH + 1);  // +1 pour inclure WIDTH
            int y = random.nextInt(Position.HEIGHT + 1); // +1 pour inclure HEIGHT
            Point p = new Point(topLeftCamp.x + x, topLeftCamp.y - y);
            Warrior v = new Warrior(100, p, i); // on utilise 'i' comme identifiant
            warriors.add(v);
            warriorsInCamp.add(v);
            vikings.add(v);
        }

        for (int i = 0; i < 3; i++) {
            int x = random.nextInt(Position.WIDTH + 1);
            int y = random.nextInt(Position.HEIGHT + 1);
            Point p = new Point(topLeftCamp.x + x, topLeftCamp.y - y);
            Farmer f = new Farmer(100, p, i);
            farmers.add(f);
            vikings.add(f);
        }



        // Ajout des cultures
        //Wheat v = new Wheat(100, new Point(70, 50), this.id, 0);
        Wheat v = new Wheat(100, new Point(topLeftCamp.x + 70, topLeftCamp.y - 50), this.id, 0);
        wheats.add(v);
        ressources.add(v);

        vegetables.add(v);
        int prc1 = 2;
        Field fi1 = new Field(new Point(Position.WIDTH_FIELD / 2 + topLeftCamp.x + prc1 * Position.WIDTH / 100, -Position.HEIGHT_FIELD / 2 + topLeftCamp.y - prc1 * Position.HEIGHT / 100), this.id, new AbsenceVegetable());
        Field fi2 = new Field(new Point(Position.WIDTH_FIELD / 2 + topLeftCamp.x + prc1 * Position.WIDTH / 100, -Position.HEIGHT_FIELD / 2 + topLeftCamp.y - prc1 * Position.HEIGHT / 100 - Position.HEIGHT_FIELD - prc1 * Position.HEIGHT / 100),
                this.id, new AbsenceVegetable());
        Field fi3 = new Field(new Point(Position.WIDTH_FIELD / 2 + topLeftCamp.x + prc1 * Position.WIDTH / 100 + Position.WIDTH_FIELD + prc1 * Position.WIDTH / 100, -Position.HEIGHT_FIELD / 2 + topLeftCamp.y - prc1 * Position.HEIGHT / 100 - Position.HEIGHT_FIELD - prc1 * Position.HEIGHT / 100), this.id, new AbsenceVegetable());
        Field fi4 = new Field(new Point(Position.WIDTH_FIELD / 2 + topLeftCamp.x + prc1 * Position.WIDTH / 100 + Position.WIDTH_FIELD + prc1 * Position.WIDTH / 100, -Position.HEIGHT_FIELD / 2 + topLeftCamp.y - prc1 * Position.HEIGHT / 100), this.id, new AbsenceVegetable());

        Field fi5 = new Field(
                new Point(
                        Position.WIDTH_FIELD / 2 + topLeftCamp.x + prc1 * Position.WIDTH / 100,
                        // On soustrait pour "descendre" dans un repère où Y+ = haut
                        topLeftCamp.y - Position.HEIGHT + prc1 * Position.HEIGHT / 100 + Position.HEIGHT_FIELD - Position.HEIGHT_FIELD / 2
                ),
                this.id,
                new AbsenceVegetable()
        );
        Field fi6 = new Field(
                new Point(
                        Position.WIDTH_FIELD / 2 + topLeftCamp.x + prc1 * Position.WIDTH / 100 + Position.WIDTH_FIELD + prc1 * Position.WIDTH / 100,
                        // On "descend" encore plus : on retire la hauteur supplémentaire
                        topLeftCamp.y - Position.HEIGHT + prc1 * Position.HEIGHT / 100 + Position.HEIGHT_FIELD - Position.HEIGHT_FIELD / 2
                ),
                this.id,
                new AbsenceVegetable()
        );

        fields.add(fi1);
        fields.add(fi2);
        fields.add(fi3);
        fields.add(fi4);
        fields.add(fi5);
        fields.add(fi6);

        int xenclos1 = Position.WIDTH_ENCLOS / 2 + topLeftCamp.x + prc1 * Position.WIDTH / 100 - 40;
        int yenclos1 = topLeftCamp.y - Position.HEIGHT / 2 + Position.HEIGHT_ENCLOS - (int) (Position.HEIGHT_ENCLOS * 1.4) + 20;
        int xenclos2 = topLeftCamp.x + Position.WIDTH - prc1 * Position.WIDTH / 100 - Position.WIDTH_ENCLOS / 2 + 30;
        int yenclos2 = -Position.HEIGHT_FIELD / 2 + topLeftCamp.y - prc1 * Position.HEIGHT / 100 - 7;
        Enclos e1 = new Enclos(
                new Point(
                        xenclos1,
                        // On soustrait pour "descendre" dans un repère où Y+ = haut
                        yenclos1
                ),
                this.id, livestocks);

        Enclos e2 = new Enclos(new Point(xenclos2, yenclos2), this.id, livestocks);

        Sheep s1 = new Sheep(100, new Point(xenclos1 - 5 , yenclos1), this.id, 6);
        Sheep s2 = new Sheep(100, new Point(xenclos1 + 17, yenclos1), this.id, 5);
        Sheep s3= new Sheep(100, new Point(xenclos1 - 5 , yenclos1 - 13), this.id, 6);
        Sheep s4 = new Sheep(100, new Point(xenclos1 + 17, yenclos1 - 13), this.id, 5);
        sheeps.add(s1);
        sheeps.add(s2);
        sheeps.add(s3);
        sheeps.add(s4);
        ressources.add(s1);
        ressources.add(s2);
        ressources.add(s3);
        ressources.add(s4);


        Cow c1 = new Cow(100, new Point(xenclos2 - 7, yenclos2), this.id, 6/*,this*/);
        Cow c2 = new Cow(100, new Point(xenclos2 + 19, yenclos2), this.id, 5/*,this*/);
        Cow c3 = new Cow(100, new Point(xenclos2 - 7, yenclos2 - 13), this.id, 5/*,this*/);
        Cow c4 = new Cow(100, new Point(xenclos2 + 19, yenclos2 - 13), this.id, 5/*,this*/);

        cows.add(c1);
        cows.add(c2);
        cows.add(c3);
        cows.add(c4);

        ressources.add(c1);
        ressources.add(c2);
        ressources.add(c3);
        ressources.add(c4);

        e1.addLivestock(s1);
        e1.addLivestock(s2);
        e1.addLivestock(s3);
        e1.addLivestock(s4);

        e2.addLivestock(c1);
        e2.addLivestock(c2);
        e2.addLivestock(c3);
        e2.addLivestock(c4);

        enclosses.add(e1);
        enclosses.add(e2);


        // ajoute tout les objet de camp créer dans entities
        entities = new ArrayList<>();
        entities.addAll(warriors);
        entities.addAll(farmers);
        entities.addAll(sheeps);
        entities.addAll(wheats);
        entities.addAll(cows);

        livestocks.addAll(sheeps);
        livestocks.addAll(cows);

        this.setEntitiesId();


    }
    private int lastIds = 0;



    public synchronized int generateNewId() {
        return id * 100 + lastIds++;
    }
    /**
     * Set the id of the entities in the camp.
     */
    /*public void setEntitiesId(){
        int i = 0;
        for (Viking viking : vikings) {
            viking.setId(id * 100 + i);
            i++;
        }
        for (Field field : fields) {
            field.setId(id * 100 + i);
            i++;
        }

        for (Livestock livestock : livestocks) {
            livestock.setId(id * 100 + i);
            i++;
        }
        this.lastId=i;

    }*/
    public void setEntitiesId() {
        this.lastId = 0;
        for (Viking viking : vikings) {
            viking.setId(generateNewId());
        }
        for (Field field : fields) {
            field.setId(generateNewId());
        }
        for (Livestock livestock : livestocks) {
            livestock.setId(generateNewId());
        }
    }

    /**
     * envoi n warrior pour attquer le camp avec l'id id
     * Précondition:  le nombre de guerriers soit suffisant
     * @param n
     */
    public void attack(int n,Camp campSrc, Camp camp, Point dst, int idResource) {

        for (int i = 0; i < n; i++) {
            Warrior warrior = this.warriorsInCamp.get(i);
            warrior.moveAttack(dst,campSrc, camp, idResource);
            // add the warrior to the vikingsAttack
            if (this.vikingsAttack.containsKey(id)) {
                this.vikingsAttack.get(camp.getId()).add(warrior);
            } else {
                ArrayList<Warrior> warriors = new ArrayList<>();
                warriors.add(warrior);
                this.vikingsAttack.put(camp.getId(), warriors);
            }
            warriorsInCamp.remove(warrior);
        }

    }
    public void repli(int attackedCampId) {
        if (!vikingsAttack.containsKey(attackedCampId)) {
            System.out.println("Aucun viking n'attaque ce camp");
            return;
        }

        ArrayList<Warrior> attackingWarriors = vikingsAttack.get(attackedCampId);
        if (attackingWarriors == null || attackingWarriors.isEmpty()) {
            System.out.println("Aucun guerrier a moi attaquant ce camps");
            return;
        }

        Point dest = Position.MAP_CAMPS_POSITION.get(this.id);
        System.out.println("Début du repli vers le camp " + this.id + " depuis le camp attaqué " + attackedCampId);

        for (Warrior warrior : attackingWarriors) {
            warrior.move(dest);

        }
        warriorsInCamp.addAll(attackingWarriors);
        vikingsAttack.remove(attackedCampId);
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
        this.addRessource(animal);
        this.entities.remove(animal);
        if(animal instanceof Sheep){
            this.sheeps.remove((Sheep)animal);
        }else if(animal instanceof Cow){
            this.cows.remove((Cow)animal);
        }
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
        warriorsInCamp.add(warrior);
        entities.add(warrior);
        vikings.add(warrior);
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
        ressources.add(vegetable);
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

    public void addRessource(Entity e){
        this.ressources.add(e);
    }
    public ArrayList<Entity> getRessources() {
        System.out.println("ressources: " + ressources);
        return ressources;
    }

    public void setThreadCommunicationServer(ThreadCommunicationServer threadCommunicationServer) {
        this.threadCommunicationServer = threadCommunicationServer;
        VikingRegenerator regenerator = new VikingRegenerator(this, this.lastId, threadCommunicationServer, regenLock );
        Thread regenThread = new Thread(regenerator);
        regenThread.start();
    }

    public void removeRessource(Entity e){
        this.ressources.remove(e);
    }

    public ArrayList<Enclos> getEnclosses() {
        return enclosses;
    }

    public HashMap<Integer, ArrayList<Warrior>> getVikingsAttack() {
        return vikingsAttack;
    }
}

