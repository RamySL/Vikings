package Main;

import model.Champ;
import model.Pillard;
import model.background;
import view.Affichage;

import javax.swing.*;

public class Main {
    public static int taille_terrain = 10;
    //public static int nb_vagues = 1;
    public static long debut_partie = System.currentTimeMillis()/1000;
    public static Thread background;
    // Paramètres du jeu
    public static int nb_pillards = 2; // Nombre de pillards
    public static int duree_vie_pillard = 50;    // Durée de vie en secondes
    //public static int intervalle_vagues = 20;
    //
    public static void main(String[] args) {
        Champ terrain = new Champ();
        Affichage affichage = new Affichage(terrain);
        background bg = new background(terrain, affichage);
        background = new Thread(bg);
        background.start();
        JFrame frame = new JFrame("Affichage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(affichage);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrer la fenêtre
        frame.setVisible(true);
        /*
        System.out.println("ici");

        for(Pillard p : terrain.getPillards()){
            System.out.println(p.getPos_x() + " " + p.getPos_y());
            System.out.println(terrain.getCase(p.getPos_x(), p.getPos_y()).contientEntite());
        }
        System.out.println();
        */

    }
}