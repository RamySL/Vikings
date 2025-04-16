package server.model;

import com.google.gson.Gson;
import network.packets.FormatPacket;
import network.packets.PacketNewWarrior;
import network.server.ThreadCommunicationServer;

import java.awt.Point;
import java.util.concurrent.TimeUnit;

public class VikingRegenerator implements Runnable {
    private final Camp camp;
    private final int idStart;
    private transient final ThreadCommunicationServer threadCommunicationServer;

    private final Object lock;
    private boolean locked;

    public VikingRegenerator(Camp camp, int idStart, ThreadCommunicationServer threadCommunicationServer, Object lock) {
        this.camp = camp;
        this.idStart = idStart;
        this.threadCommunicationServer = threadCommunicationServer;
        this.lock = lock;
        this.locked = false;
    }
    private void checkAndLockIfNeeded() {
        if (camp.getWarriors().size() > 2) {
            lock();
        }
    }


    public void lock() {
        locked = true;
    }

    public void unlock() {
        synchronized (lock) {
            locked = false;
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            checkAndLockIfNeeded();
            if (locked) {
                synchronized (lock) {
                    while (locked) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("VikingRegenerator thread interrupted during wait.");
                            Thread.currentThread().interrupt();
                            return;
                        }
                        // Recheck the condition to possibly unlock
                        if (camp.getWarriors().size() <= 2) {
                            unlock();
                        }
                    }
                }
            }


            try {
                float health = 100.0f;

                TimeUnit.SECONDS.sleep(8);
                Point pos= Position.MAP_CAMPS_POSITION.get(camp.getId());
                Point position = new Point(pos.x+100, pos.y);


                Warrior newWarrior = new Warrior(health, position, camp.getId());
                newWarrior.setId(camp.generateNewId());
                camp.addWarrior(newWarrior);

                PacketNewWarrior packet = new PacketNewWarrior(newWarrior.getId(), newWarrior.getPosition().x, newWarrior.getPosition().y);
                String packetJson = new Gson().toJson(packet);
                threadCommunicationServer.sendMessage(FormatPacket.format("PacketNewWarrior", packetJson), true);

                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                System.out.println("The regeneration thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
