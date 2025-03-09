package model;

/**
 * Pour représenter un objet du modèle
 */
public class Mouton {
    private static int idstatic = 0;
    private int id;
    private int x;
    private int y;
    private int speed=10;

    public Mouton(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;

    }

    public Mouton(int id) {
        this.id = id;
    }

    public Mouton(){
        this(idstatic,100,100);
        idstatic++;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveUp() {
        this.y -= speed;
    }

    public void moveDown() {
        this.y += speed;
    }

    public void moveLeft() {
        this.x -= speed;
    }

    public void moveRight() {
        this.x += speed;
    }
}
