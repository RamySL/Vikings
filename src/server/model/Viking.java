package server.model;

import javax.swing.text.Position;
import java.awt.*;

public abstract class Viking extends Entity implements Moveable{


    public Viking(float health, Point position, int camp) {
        super(health, position, camp);
    }

    public void move(Point position){
        if(this.campId==1){
            this.position = new Point(position.x-400, position.y);
        }else{
            this.position =  position;
        }

    }
    public void eat(){


    }

    public void takeDamage(float amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }
}
