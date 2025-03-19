package network;

import com.google.gson.Gson;
import server.model.Camp;
import server.model.Partie;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe pricipale du serveur, Création du serveur, lancement, gestion des connexions, gestion des partie.
 */
public class Server {

    private ServerSocket socket;
    private ArrayList<ThreadCommunicationServer> clients;
    private int nbJoueurs;
    private Partie partie;

    /**
     * Lance le serveur sur le port donné et attend le nombre de joueur donné pour lancer la partie
     * @param port
     * @param nbJoueurs nombre de jr à attendre pour lancer la partie
     */
    public Server(int port, int nbJoueurs) {
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
        Camp[] camps = new Camp[nbJoueurs];

       while (nbJoueursConnectes<nbJoueurs) {
           try {
               System.out.println("En attente de connexion");
               clientSoket =  socket.accept();
               System.out.println("Connexion établie avec " + clientSoket.getInetAddress());

               // On créer un camp pour le client connecté
               Camp camp = new Camp(nbJoueursConnectes);
               System.out.println("Camp créé : " + camp);
               camps[nbJoueursConnectes] = camp;
               nbJoueursConnectes++;
               threadCommunicationServer = new ThreadCommunicationServer(this, clientSoket, camp);
               this.clients.add(threadCommunicationServer);
               threadCommunicationServer.start();
           } catch (IOException e) {
               System.out.println("Erreur lors de la connexion");
               throw new RuntimeException(e);
           }
       }
        createPartie(camps);
       (new ThreadGameState(this)).start();

    }

    /**
     * Intialise la partie.
     */
    public void createPartie(Camp[] camps) {
        this.partie = new Partie(camps);

    }

    /**
     * Envoie l'état de la partie à tous les clients connectés
     */
    public void broadcastGameState() {
        Gson gson = new Gson();
        String content = gson.toJson(partie);
        broadcast(FormatPacket.format("Partie",content));
    }

    /**
     * Envoie un message à tous les clients connectés
     * @param message
     */
    public void broadcast(String message) {
        //System.out.println("thread id: " + Thread.currentThread().threadId());
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
