package client.view;

import model.Unite;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 * VueUnite class
 * This class represents the view for the units.
 * It extends the JPanel class and displays the units.
 */
public class VueUnite extends JPanel {
    private List<Unite> unites;

    /**
     * Constructor for the VueUnite class.
     * Initializes the list of units.
     * @param unites The list of units to display.
     */
    public VueUnite(List<Unite> unites) {
        this.unites = unites;
        setPreferredSize(new Dimension(500, 500));
    }

    /**
     * paintComponent method
     * This method paints the units on the panel.
     * @param g The graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Unite unite : unites) {
            g.setColor(Color.RED); // Couleur du cercle (l'unité)
            g.fillOval(unite.getX() - 5, unite.getY() - 5, 10, 10); // Dessine un petit rond
        }
    }

    /**
     * mettreAJour method
     * This method updates the view.
     */
    public void mettreAJour() {
        repaint();
    }
}
