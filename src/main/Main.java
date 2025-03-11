package main;

import model.DeplacementThread;
import model.Unite;
import view.VueUnite;
import view.PanneauControle;
import control.Controleur;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 * main.Main class
 * This class is the entry point of the application.
 * It creates the units, the view, the controller, the control panel, and the frame.
 * It then starts the threads for the units.
 */
public class Main {
    /**
     * main.Main method
     * This method is the entry point of the application.
     * It creates the units, the view, the controller, the control panel, and the frame.
     * It then starts the threads for the units.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // Créer des unités
        List<Unite> unites = new ArrayList<>();
        unites.add(new Unite("Unite1", 100, 100));
        unites.add(new Unite("Unite2", 200, 200));

        // Créer la vue avec les unités
        VueUnite vueUnite = new VueUnite(unites);

        // Créer le contrôleur
        Controleur controleur = new Controleur(unites, vueUnite);
        vueUnite.addMouseListener(controleur);

        // Créer le panneau de contrôle avec des boutons
        PanneauControle panneauControle = new PanneauControle();

        // Créer la fenêtre et ajouter la vue et le panneau de contrôle
        JFrame frame = new JFrame("Simulation des unités");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Ajouter la carte (vue des unités) à gauche
        frame.add(vueUnite, BorderLayout.CENTER);

        // Ajouter le panneau de contrôle à droite
        frame.add(panneauControle, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);

        // Lancer les threads de déplacement pour chaque unité
        for (Unite unite : unites) {
            new Thread(new DeplacementThread(unite, vueUnite)).start();
        }
    }
}
