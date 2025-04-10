package network.packets;

import java.awt.*;

/**
 * Paquet envoyé par le client pour demander le déplacement d'un viking ou d'un farmer
 */
public class PacketMovement {
    private int id;
    private Point dst;
    private Point translation;
    private double scale;

    /**
     *
     * @param id id du viking ou farmer
     * @param dst destination du déplacement
     */
    public PacketMovement(int id, Point dst, Point translation, double scale) {
        this.id = id;
        this.dst = dst;
        this.translation = translation;
        this.scale = scale;
    }

    public int getId() {
        return id;
    }

    public Point getDst() {
        return dst;
    }

    public Point getTranslation() {
        return translation;
    }

    public double getScale() {
        return scale;
    }
}



