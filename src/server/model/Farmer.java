package server.model;

import java.awt.*;
import java.util.List;

public class Farmer extends Viking {
     // Référence au camp pour gérer les cultures et le bétail

     public Farmer(float health, Point position, int camp) {
        super(health, position, camp);
    }


    /**met
     * Plante du blé à la position actuelle du fermier.
     * La vérification de proximité est gérée ailleurs (ex: un thread détecte la proximité et affiche un bouton).
     */
    public void plant(Camp camp) {
        // Vérifie s'il y a déjà du blé à proximité (évite la superposition)
        for (Vegetable v : camp.getWheats()) {
            if (position.distance(v.getPosition()) < 30) {
                System.out.println("Un blé est déjà planté ici !");
                return;
            }
        }

        // Génération d'une position légèrement aléatoire autour du fermier
        int offsetX = (int) (Math.random() * 6) - 3; // Valeur entre -3 et +3
        int offsetY = (int) (Math.random() * 6) - 3;
        Point plantPosition = new Point(position.x + offsetX, position.y + offsetY);

        // Création et ajout du nouveau blé
        Wheat newWheat = new Wheat(100, plantPosition, camp.getId(), 0);
        camp.addWheat(newWheat);

        System.out.println("Le fermier plante du blé à " + plantPosition);
    }

    /*public void plant(Field field, String resource) {
        if (isNearField(field)) {
            field.plant(resource);
        }
    }*/

    /**
     * Récolte du blé mûr si le fermier est à proximité d'un champ.
     * La détection de proximité est gérée par un thread externe.
     */
    public void harvest(Camp camp) {
        for (Vegetable v : camp.getWheats()) {
            if (v instanceof Wheat wheat && wheat.isMature()) {
                System.out.println("Le fermier récolte du blé !");
                camp.removeWheat(wheat);
                return;
            }
        }
        System.out.println("Aucun blé mûr à récolter !");
    }

    /*public String harvest(Field field) {
        return field.harvest();
    }*/

    /**
     * Nourrit les animaux.
     * La logique d'affichage du bouton dépend du thread de détection de proximité.
     */

    public void feed(Camp camp) {
        for (Livestock l : camp.getLivestocks()) {
            System.out.println("Le fermier nourrit " + (l instanceof Cow ? "une vache" : "un mouton") + " !");
            l.health += 10;
            if (l.health > 100) l.health = 100; // Limite à 100
            return;
        }
        System.out.println("Aucun animal à nourrir !");
    }

    /**
     * Arrose les cultures sans vérifier la proximité (gérée ailleurs).
     */
    public void water(Camp camp) {
        for (Vegetable v : camp.getWheats()) {
            System.out.println("Le fermier arrose les cultures !");
            v.grow(); // Augmente la croissance
            return;
        }
        System.out.println("Pas de culture disponible !");
    }


    public Camp getCamp() {
        // Assuming you have a way to get the camp by ID
        return CampManager.getCampById(this.campId);
    }

    // String avec nom de la classe
    @Override
    public String toString() {
        return "Farmer";
    }


}

