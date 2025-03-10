package model;

import java.util.ArrayList;

public abstract class Entite {
    private int id;
    private int pos_x, pos_y;
    private int duree_de_vie;
    public Entite(int id, int pos_x, int pos_y, int dv) {
        this.id = id;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.duree_de_vie = dv;
    }
    public int getId() {
        return id;
    }

    public int getPos_x() {
        return pos_x;
    }
    public int getPos_y() {
        return pos_y;
    }


    public long getDuree_de_vie() {
        return duree_de_vie;
    }

    public void setDuree_de_vie(int duree_de_vie) {
        this.duree_de_vie = duree_de_vie;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }


}

