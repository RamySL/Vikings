package network;

import server.model.Partie;
import client.view.ThreadRepaint;
import client.view.ViewClient;
import com.google.gson.Gson;

/**
 * Thread qui écouter ce que le serveur envoie et actualise l'interface graphique du client
 */
public class ThreadCommunicationClient extends Thread{
    private Client client;
    // pour distinguer l'unique moment ou on doit changer
    // la vue ver celle de la partie
    private boolean gameStarted = false;
    private ViewClient view;
    public ThreadCommunicationClient(Client client, ViewClient view) {
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
     * @param msg
     */
    public void reactMessage(String msg){
        Gson gson = new Gson();
        PacketWrapper wrapper = gson.fromJson(msg, PacketWrapper.class);
        switch (wrapper.type){
            case "Partie":
                Partie game = gson.fromJson(wrapper.content, Partie.class);
                this.view.actualisePartie(game);
                if(!gameStarted) {
                    this.view.changeCard("3");
                    new ThreadRepaint(this.view.getViewPartie()).start();
                    gameStarted=true;
                }
                break;
            case "PacketOpenPanelControl":
                PacketOpenPanelControl pCtrl = gson.fromJson(wrapper.content, PacketOpenPanelControl.class);
                //this.view.changeCard("4");
            default:
                System.out.println("Invalid message format");
        }

    }


    public Client getClient() {
        return client;
    }
}
