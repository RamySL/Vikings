package model;

/**
 * Bouge un objet du modèle
 */
public class ThreadMouvement extends Thread {
    private Mouton mouton;
    private boolean running = true;

    public ThreadMouvement(Mouton mouton) {
        this.mouton = mouton;
    }

    @Override
    public void run() {
        while (running) {
            if (mouton.getX() < 400) {
                mouton.moveRight();
            }else {
                mouton.setX(100);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopThread() {
        running = false;
    }
}