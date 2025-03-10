package model;

import Main.Main;

import java.util.Random;

public class Pillard extends Entite {
    private int hp;
    private int duree_de_vie = Main.duree_vie_pillard;
    private boolean mort;
    private int gain;

    public Pillard(int id, int pos_x, int pos_y, int dv) {
        super(id, pos_x, pos_y, dv);
        this.hp = 100;
        this.mort = false;
        this.gain = 0;
    }


    public void move(Champ terrain) {
        Random random = new Random();
        int dirChoice;
        int newPosX;
        int newPosY;

        do {
            dirChoice = random.nextInt(100);
            newPosX = getPos_x();
            newPosY = getPos_y();

            if (dirChoice < 10) {
                return; // Stay in place
            } else if (dirChoice < 22) { // North
                newPosX--;
            } else if (dirChoice < 34) { // East
                newPosY++;
            } else if (dirChoice < 46) { // South
                newPosX++;
            } else if (dirChoice < 58) { // West
                newPosY--;
            } else if (dirChoice < 68) { // NorthEast
                newPosX--;
                newPosY++;
            } else if (dirChoice < 78) { // SouthEast
                newPosX++;
                newPosY++;
            } else if (dirChoice < 88) { // NorthWest
                newPosX--;
                newPosY--;
            } else { // SouthWest
                newPosX++;
                newPosY--;
            }

            // Check boundaries
            if (newPosX < 0 || newPosX >= Main.taille_terrain ||
                    newPosY < 0 || newPosY >= Main.taille_terrain) {
                return;
            }

            // Check if position has a resource that can be pillaged
            if (terrain.getCase(newPosX, newPosY).contientRessource() &&
                    !terrain.getCase(newPosX, newPosY).getRessource().getUseless()) {
                break; // We can move here to pillage
            }

        } while (terrain.getCase(newPosX, newPosY).isOccupe());

        // Remove pillard from current position
        terrain.getCase(getPos_x(), getPos_y()).setEntite(null);
        if (!terrain.getCase(getPos_x(), getPos_y()).contientRessource()) {
            terrain.getCase(getPos_x(), getPos_y()).setOccupe(false);
        }

        // If there's a resource at the new position, pillage it
        if (terrain.getCase(newPosX, newPosY).contientRessource()) {
            piller(terrain, newPosX, newPosY);
        }

        // Update pillard position
        setPos_x(newPosX);
        setPos_y(newPosY);

        // Set entity at new position
        terrain.getCase(newPosX, newPosY).setEntite(this);
        terrain.getCase(newPosX, newPosY).setOccupe(true);

        System.out.println("Pillard " + getId() + " s'est déplacé en " + newPosX + ", " + newPosY);
    }


    public void increaseGain(int gain){
        this.gain += gain;
    }

    public long getDuree_de_vie() {
        return duree_de_vie;
    }
    public int getGain() {
        return gain;
    }

    public void piller(Champ terrain, int posX, int posY) {
        if (terrain.getCase(posX, posY).contientRessource() &&
                !terrain.getCase(posX, posY).getRessource().getUseless()) {
            terrain.getCase(posX, posY).getRessource().setUseless(true);
            increaseGain(50);
            System.out.println("Pillard " + getId() + " a pillé une ressource en " + posX + ", " + posY);
            terrain.getCase(posX, posY).setEntite(this);
        }
    }

}
