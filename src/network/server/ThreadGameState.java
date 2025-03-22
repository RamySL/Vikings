package network.server;

/**
 * Thread qui envoie l'état du jeu à tous les clients chaque 100ms<p>
 * ? On pourrait réduire le nombre d'envoi par sec. pour éviter la redondance quand rien ne bouge chez les
 * clients, et donc faire d'autre broadcast en dehors de cette classe.
 */
public class ThreadGameState extends Thread{
    private Server server;

    public ThreadGameState(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            this.server.broadcastGameState();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}