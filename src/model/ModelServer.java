package model;

import com.google.gson.Gson;
import controler.ThreadCommunicationServer;
import controler.ThreadGameState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe pricipale du serveur, Création du serveur, lancement, gestion des connexions, gestion des partie.
 */
public class ModelServer {

    private ServerSocket socket;
    private ArrayList<ThreadCommunicationServer> clients;
    private int nbJoueurs;
    private Partie partie;

    /**
     * Lance le serveur sur le port donné et attend le nombre de joueur donné pour lancer la partie
     * @param port
     * @param nbJoueurs nombre de jr à attendre pour lancer la partie
     */
    public ModelServer(int port, int nbJoueurs) {
        this.nbJoueurs = nbJoueurs;
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Création de socket échouée");
            throw new RuntimeException(e);
        }
        this.clients = new ArrayList<>();

    }

    /**
     * Attend les connexions des joueurs et lance la partie une fois que le nombre de joueur est atteint<p>
     *  Cree un thread pour chaque joueur connecté
     *  @see ThreadCommunicationServer
     */
    public void launch() {
       Socket clientSoket;
       ThreadCommunicationServer threadCommunicationServer;
       int nbJoueursConnectes = 0;
       ArrayList<Mouton> moutons = new ArrayList<>();

       while (nbJoueursConnectes<nbJoueurs) {
           try {
               System.out.println("En attente de connexion");
               clientSoket =  socket.accept();
               System.out.println("Connexion établie avec " + clientSoket.getInetAddress());
               nbJoueursConnectes++;
               // lancer un thread pour communiquer avec le client
               Mouton mouton = new Mouton();
               moutons.add(mouton);
               threadCommunicationServer = new ThreadCommunicationServer(this, clientSoket, mouton);
               this.clients.add(threadCommunicationServer);
               threadCommunicationServer.start();
           } catch (IOException e) {
               System.out.println("Erreur lors de la connexion");
               throw new RuntimeException(e);
           }
       }
       // Tous le joueurs sont connectés, on init la partie et on l'envoie
        createPartie(moutons);
       /*for (Mouton mouton : moutons) {
           (new ThreadMouvement(mouton)).start();
       }*/
       (new ThreadGameState(this)).start();

    }

    /**
     * Intialise la partie.
     */
    public void createPartie(ArrayList<Mouton> moutons) {
        this.partie = new Partie();
        for (Mouton mouton : moutons) {
            this.partie.addMouton(mouton);
        }
    }

    /**
     * Envoie l'état de la partie à tous les clients connectés
     */
    public void broadcastGameState() {
        Gson gson = new Gson();
        String json = gson.toJson(partie);
        broadcast(json);
    }

    /**
     * Envoie un message à tous les clients connectés
     * @param message
     */
    public void broadcast(String message) {
        for (ThreadCommunicationServer client : this.clients) {
            client.sendMessage(message);
        }
    }

    public void closeConnection() {
        try {
            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
