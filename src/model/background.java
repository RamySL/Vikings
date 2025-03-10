package model;

import Main.Main;
import model.Champ;
import view.Affichage;

public class background extends Thread { // mouvement des joueurs, vagues, et durée de vie
    private int DELAY = 950;
    private Champ terrain;
    private Affichage affichage;

    public background(Champ terrain, Affichage affichage) {
        this.terrain = terrain;
        this.affichage = affichage;
    }


    @Override
    public void run() {
        System.out.println(Main.debut_partie);
        while (true) {
            // 1. Déplacer tous les pillards
            for (Pillard p : terrain.getPillards()) {
                p.move(terrain);
            }

            // 2. Vérifier toutes les cases
            for (int i = 0; i < Main.taille_terrain; i++) {
                for (int j = 0; j < Main.taille_terrain; j++) {
                    Champ.Case tmp = terrain.getCase(i, j);
                    if (tmp.contientRessource() && tmp.getRessource().getDuree_de_vie() + Main.debut_partie <= System.currentTimeMillis() / 1000) {
                        terrain.getCase(i, j).getRessource().setUseless(true);
                        System.out.println("La ressource en " + i + " " + j + " a disparu");
                    } else if (tmp.contientEntite() && tmp.getEntite().getDuree_de_vie() + Main.debut_partie - 10 <= System.currentTimeMillis() / 1000 && tmp.getEntite() instanceof Pillard) {
                        System.out.println("L'entité pillard en " + i + " " + j + " est morte");
                    }
                }
            }
            printTotalPillage();
            affichage.repaint();

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void printTotalPillage() {
        int totalGain = 0;
        for (Pillard p : terrain.getPillards()) {
            totalGain += p.getGain();
        }
        System.out.println("Valeur totale du pillage: " + totalGain);
    }


}