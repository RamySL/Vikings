package server.model;
import view.VueUnite;

/**
 * DeplacementThread class
 * This class represents a thread that moves a unit to its destination.
 * It extends the Thread class.
 */
public class DeplacementThread extends Thread {
    private Unite unite;
    private VueUnite vueUnite;

    /**
     * DeplacementThread constructor
     * This constructor creates a new DeplacementThread with the specified unit and unit view.
     * @param unite The unit to move.
     * @param vueUnite The view of the unit.
     */
    public DeplacementThread(Unite unite, VueUnite vueUnite) {
        this.unite = unite;
        this.vueUnite = vueUnite;
    }

    /**
     * run method
     * This method moves the unit to its destination.
     */
    @Override
    public void run() {
        // Déplacement de l'unité jusqu'à ce qu'elle atteigne sa destination
        while (true) {
            unite.deplacer();
            vueUnite.mettreAJour(); // Met à jour l'affichage à chaque déplacement


            try {
                Thread.sleep(50); // Temporisation plus longue pour observer les déplacements
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

