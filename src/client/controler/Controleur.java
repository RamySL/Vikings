package client.controler;

import model.Unite;
import view.VueUnite;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Controleur class
 * This class represents the controller for the game.
 * It extends the MouseAdapter class and handles mouse events.
 */
public class Controleur extends MouseAdapter {
    private List<Unite> unites;
    private Unite uniteSelectionnee;
    private VueUnite vueUnite;  // Référence à VueUnite

    /**
     * Constructor for the Controleur class.
     * Initializes the list of units and the view.
     *
     * @param unites The list of units in the game.
     * @param vueUnite The view of the units in the game.
     */
    public Controleur(List<Unite> unites, VueUnite vueUnite) {
        this.unites = unites;
        this.vueUnite = vueUnite;  // Initialisation de vueUnite
    }

    /**
     * mousePressed method
     * This method handles the mouse pressed event.
     * It checks if a unit is clicked and updates the destination of the unit.
     *
     * @param e The mouse event that occurred.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // Vérifier si un clic est sur une unité
        for (Unite unite : unites) {
            if (Math.abs(unite.getX() - x) < 10 && Math.abs(unite.getY() - y) < 10) {
                uniteSelectionnee = unite;
                return;
            }
        }

        // Si une unité est sélectionnée et qu'on clique en dehors
        if (uniteSelectionnee != null) {
            // Vérifier si la destination est réellement différente
            if (uniteSelectionnee.getX() != x || uniteSelectionnee.getY() != y) {
                uniteSelectionnee.setDestination(x, y);
                vueUnite.mettreAJour(); // Mettre à jour la vue après avoir changé la destination
            }
        }
    }

}
