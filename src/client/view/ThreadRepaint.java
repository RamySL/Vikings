package client.view;

/**
 * Redessine la vue de la partie
 */
public class ThreadRepaint extends Thread {
    private ViewPartie viewPartie;

    public ThreadRepaint(ViewPartie viewPartie) {
        this.viewPartie = viewPartie;
    }

    @Override
    public void run() {
        while (true) {
            this.viewPartie.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
