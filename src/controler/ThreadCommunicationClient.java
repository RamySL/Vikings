package controler;

import model.ModelClient;
import model.Partie;
import view.ThreadRepaint;
import view.ViewClient;
import com.google.gson.Gson;

/**
 * Thread qui écouter ce que le serveur envoie et actualise l'interface graphique du client
 */
public class ThreadCommunicationClient extends Thread{
    private ModelClient client;
    // pour distinguer l'unique moment ou on doit changer
    // la vue ver celle de la partie
    private boolean gameStarted = false;
    private ViewClient view;
    public ThreadCommunicationClient(ModelClient client, ViewClient view) {
        this.client = client;
        this.view = view;
    }

    @Override
    public void run() {
        while (true) {
            String msg = this.client.receiveMessage();
            if(!msg.isEmpty()) {
                System.out.println("Reçus: " + msg);
                reactMessage(msg);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Actualise l'interface graphique du client en fonction du message reçu du serveur<p>
     * Pour l'instant on gère qu'un seule type d'objet reçus.
     * @param msg
     */
    public void reactMessage(String msg){
        Partie game = deserialize(msg);
        this.view.actualisePartie(game);
        if(!gameStarted) {
            this.view.changeCard("3");
            new ThreadRepaint(this.view.getViewPartie()).start();
        }
    }

    /**
     * Déserialise le meessage reçu par le serveur en objet Partie<p>
     * Précondition: le message est un objet Partie !! sérialisé
     * @param msg
     * @return
     */
    public Partie deserialize(String msg){
        Gson gson = new Gson();
        return gson.fromJson(msg, Partie.class);
    }

    public ModelClient getClient() {
        return client;
    }
}
