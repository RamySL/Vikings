package model;

import Main.Main;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.Random;

public class Champ {
    Random z = new Random();
    ArrayList<Pillard> pillards = new ArrayList<>();
    ArrayList<ArrayList<Case>> terrain;
    //ArrayList<Vague> vagues;
    public Champ() {
        terrain = new ArrayList<>(Main.taille_terrain);
        for (int i = 0; i < Main.taille_terrain; i++) {
            terrain.add(new ArrayList<Case>(Main.taille_terrain));
            for (int j = 0; j < Main.taille_terrain; j++) {
                int value = z.nextInt(100);
                if (z.nextInt(100) > 30) {
                    terrain.get(i).add(new Case());
                } else {
                    Ressource ressource = new Ressource();
                    //System.out.println(ressource.duree_de_vie);
                    terrain.get(i).add(new Case(ressource));
                }
            }
        }
        int x;
        int y;
        for (int j = 0; j < Main.nb_pillards; j++) {
            do {
                x = z.nextInt(Main.taille_terrain);
                y = z.nextInt(Main.taille_terrain);
            } while (terrain.get(x).get(y).isOccupe());
            Pillard tmp = new Pillard(j, x, y, Main.duree_vie_pillard);
            pillards.add(tmp);
            this.getCase(x,y).setEntite(tmp);
            System.out.println(getCase(x,y).isOccupe() && getCase(x,y).contientEntite());
        }
    }

    public Case getCase(int i, int j) {
        return terrain.get(i).get(j);
    }

    public ArrayList<Pillard> getPillards() {
        return pillards;
    }


    public class Ressource{
    private final int duree_de_vie; // le final ici est utile juste pour mon implementation, car selon l'idée sur laquelle on pourrait partir la durée de vie pourrait augmenter ( soins, engrais ... )
    private final int gain;// ce que rapporte la ressource
    private boolean useless;

    public Ressource(int duree_de_vie, int gain){
        this.duree_de_vie = duree_de_vie;
        this.gain = gain;
        this.useless = false;
    }

        public void setUseless(boolean pillé) {
            this.useless = pillé;
        }

        public Ressource(){
        Random tmp = new Random();
        this.duree_de_vie = tmp.nextInt(120) + 45; // je veux qu'une ressource ait une durée de vie de 30 secondes à 1mn30
        this.gain = 50; // kcal
    }

        public long getDuree_de_vie() {
            return duree_de_vie;
        }

        public boolean getUseless() {
            return useless;
        }
    }

public class Case {
    private boolean occupe;
    private Entite entite;
    private Ressource ressource;

    public Case() {
        this.entite = null;
        this.ressource = null;
        occupe = false;
    }

    public Case(Ressource ressource) {
        this.entite = null;
        this.ressource = ressource;
        occupe = true;
    }

    public boolean contientEntite() {
        return entite != null && occupe;
    }


    public Ressource getRessource() {
        return ressource;
    }

    public Entite getEntite() {
        return entite;
    }

    public boolean contientRessource() {
        return ressource != null && occupe;
    }

    public void setEntite(Entite entite) {
        this.entite = entite;
        this.occupe = true;
    }

    public void setOccupe(boolean occupe) {
        this.occupe = occupe;
    }

    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
        this.occupe = true;
    }

    public int getPos_x() {
        return entite.getPos_x();
    }

    public int getPos_y() {
        return entite.getPos_y();
    }

    public boolean isOccupe() {
        return occupe;
    }
}

    public ArrayList<ArrayList<Case>> getTerrain() {
        return terrain;
    }
}