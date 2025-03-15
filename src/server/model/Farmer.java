package server.model;

import java.awt.*;
import java.util.List;

public class Farmer extends Viking{

    public Farmer(float health, Point position, int camp) {
        super(health, position, camp);
    }

    /*public void plant(Field field, String resource) {
        if (isNearField(field)) {
            field.plant(resource);
        }
    }*/



    public String harvest(Field field) {
        return field.harvest();
    }

    public void feed(){

    }

    public void water(){

    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    public Camp getCamp() {
        // Assuming you have a way to get the camp by ID
        return CampManager.getCampById(this.campId);
    }

    public boolean isOnField() {
        Camp camp = getCamp(); // Récupérer le camp du fermier
        List<Point> fieldPositions = camp.getFieldPositions(); // Récupérer les positions des champs

        // Comparer la position du fermier avec celles des champs
        for (Point fieldPosition : fieldPositions) {
            if (this.position.equals(fieldPosition)) {
                return true; // Le fermier est sur un champ
            }
        }
        return false; // Le fermier n'est pas sur un champ
    }


}
