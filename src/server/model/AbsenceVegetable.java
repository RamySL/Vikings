package server.model;

import java.awt.*;

/**
 * Représente l'absence d'une ressource dans un field.
 * Permet d'eviter l'erreur d'envoyer un paquet avec null
 */
public class AbsenceVegetable extends Vegetable{
    public AbsenceVegetable() {
        super(0, new Point(), -1, -1);
    }
}
