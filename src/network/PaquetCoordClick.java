package network;

/**
 * Paquet de mouvement envoyé par le client lorsque il bouge la souris pour déplacer le mouton.
 */
public class PaquetCoordClick {
    private int x;
    private int y;

    public PaquetCoordClick(int x, int y) {
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
