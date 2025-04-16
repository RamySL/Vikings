package network.packets;

public class PacketNewWarrior {
    private int id;
    private int x;
    private int y;

    public PacketNewWarrior(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getWarriorId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}