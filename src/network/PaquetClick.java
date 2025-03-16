package network;

/**
 * Paquet de mouvement envoyé par le client lorsque il bouge la souris pour déplacer le mouton.
 */
public class PaquetClick {
    private int x;
    private int y;
    // ratio x et y de la vue
    private int translationX, translationY;

    public PaquetClick(int x, int y, int translationX, int translationY) {
        this.x = x;
        this.y = y;
        this.translationX = translationX;
        this.translationY = translationY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTranslationX() {
        return translationX;
    }

    public int getTranslationY() {
        return translationY;
    }
}
