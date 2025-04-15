package server.model;

import java.util.concurrent.TimeUnit;

public class VikingRegenerator implements Runnable {
    private final Camp camp;
    private int x, y, idStart;

    public VikingRegenerator(Camp camp, int x, int y, int idStart) {
        this.camp = camp;
        this.x=x;
        this.y=y;
        this.idStart=idStart;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Create a new Warrior with the correct constructor parameters
                float health = 100.0f;
                java.awt.Point position = new java.awt.Point(this.x+50, this.y-12*idStart+50);
                idStart = idStart+1;

                Warrior newWarrior = new Warrior(health, position, idStart);

                // Add the Warrior to the camp
                synchronized (camp) {
                    camp.addWarrior(newWarrior);
                }

                System.out.println("A new Warrior has been regenerated: " );

                // Wait 30 seconds before regenerating another Warrior
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                System.out.println("The regeneration thread was interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}