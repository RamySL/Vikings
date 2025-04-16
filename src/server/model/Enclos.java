package server.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.DoubleStream;

public class  Enclos {
    private Point position;
    private int campId;
    private int id;
    private ArrayList<Livestock> livestocks;

    public Enclos(Point position, int campId, ArrayList<Livestock> livestock) {
        this.position = position;
        this.campId = campId;
        this.livestocks = livestock;
    }

    public Point getPosition() {
        return position;
    }


    public int getCampId() {
        return campId;
    }

    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // String avec nom de la classe
    @Override
    public String toString() {
        return "Field " + id;
    }

    public void addLivestock(Livestock livestock) {
        livestocks.add(livestock);
    }

}