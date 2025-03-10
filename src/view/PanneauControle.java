package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * PanneauControle class
 * This class represents the control panel for the game.
 * It extends the JPanel class and displays the control buttons.
 */
public class PanneauControle extends JPanel {
    public PanneauControle() {
        // Configuration du layout pour disposer les boutons en carré
        setLayout(new GridLayout(2, 2));

        // Création des boutons
        JButton boutonDeplacer = new JButton("Se déplacer");
        JButton boutonPlanter = new JButton("Planter");
        JButton boutonRecolter = new JButton("Récolter");
        JButton boutonRester = new JButton("Rester sur place");

        /**
         * ActionListener for the "Se déplacer" button.
         * This ActionListener prints a message to the console when the button is clicked.
         */
        // Ajouter les ActionListeners aux boutons
        boutonDeplacer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action : Se déplacer");
            }
        });

        /**
         * ActionListener for the "Planter" button.
         * This ActionListener prints a message to the console when the button is clicked.
         */
        boutonPlanter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action : Planter");
            }
        });

        /**
         * ActionListener for the "Récolter" button.
         * This ActionListener prints a message to the console when the button is clicked.
         */
        boutonRecolter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action : Récolter");
            }
        });

        /**
         * ActionListener for the "Rester sur place" button.
         * This ActionListener prints a message to the console when the button is clicked.
         */
        boutonRester.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action : Rester sur place");
            }
        });

        // Ajouter les boutons au panneau de contrôle
        add(boutonDeplacer);
        add(boutonPlanter);
        add(boutonRecolter);
        add(boutonRester);
    }
}
