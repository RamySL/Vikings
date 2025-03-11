package controler;

/**
 * Paquet de mouvement envoyé par le client lorsque il bouge la souris pour déplacer le mouton.
 */
public class PaquetMouvement {
    private int x;
    private int y;

    public PaquetMouvement(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
