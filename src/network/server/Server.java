package network.server;

import com.google.gson.Gson;
import network.ModelAdapter;
import network.packets.FormatPacket;
import network.packets.PacketConnectedPlayers;
import network.packets.PaquetEndGame;
import network.packets.PaquetTimer;
import server.model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pricipale du serveur, Création du serveur, lancement, gestion des connexions, gestion des partie.
 */
public class Server {

    private transient ServerSocket socket;
    private ArrayList<ThreadCommunicationServer> clients;
    private int nbJoueurs;
    private Partie partie;
    private Gson gson = ModelAdapter.getGson();
    private server.view.Server serverView;
    private GameTimer timer;
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
        this.timer = new GameTimer(1000);

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
               clientSoket =  socket.accept();
               // On créer un camp pour le client connecté
               Camp camp = new Camp(nbJoueursConnectes);
               new ThreadCollisionCamp(camp).start();
               camps[nbJoueursConnectes] = camp;
               nbJoueursConnectes++;
               threadCommunicationServer = new ThreadCommunicationServer(this, clientSoket, camp);
               this.clients.add(threadCommunicationServer);
               threadCommunicationServer.start();
               threadCommunicationServer.setThreadCommunicationServer(camp);


           } catch (IOException e) {
               System.out.println("Erreur lors de la connexion");
               throw new RuntimeException(e);
           }
       }
        createPartie(camps, timer);
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        timer.start();
        (new ThreadGameState(this)).start();

    }

    /**
     * Intialise la partie.
     */
    public void createPartie(Camp[] camps, GameTimer timer) {
        this.partie = new Partie(camps, timer);

    }

    /**
     * Envoie l'état de la partie à tous les clients connectés
     */
    public void broadcastGameState() {
        String content = gson.toJson(partie);
        broadcast(FormatPacket.format("Partie",content), true);
        if (partie != null && !partie.isGameOver()) {
            String timerContent = gson.toJson(new PaquetTimer(partie.getRemainingTime()));
            broadcast(FormatPacket.format("PaquetTimer", timerContent),!partie.isGameOver() );
        }
        if (partie!=null && partie.isGameOver()){
            String endGameContent = gson.toJson(new PaquetEndGame(getWinnerCampIds()));
            broadcast(FormatPacket.format("PaquetEndGame", endGameContent), true);
        }
    }

    /**
     * Envoie un message à tous les clients connectés
     * @param message
     */
    public void broadcast(String message, boolean gameState) {
        //System.out.println("thread id: " + Thread.currentThread().threadId());
        for (ThreadCommunicationServer client : this.clients) {
            client.sendMessage(message, gameState);
        }
    }

    /**
     * Une méthodes pour broadcast les usernames et ips des joueurs
     */
    public void broadcastUsernames() {
        // creer un String[] usernames à partir des camps et String[] ips
        String[] usernames = new String[this.clients.size()];
        String[] ips = new String[this.clients.size()];
        for (int i = 0; i < this.clients.size(); i++) {
            usernames[i] = this.clients.get(i).getCamp().getUsername();
            ips[i] = this.clients.get(i).getClient().getInetAddress().toString();
        }
        // creer un PacketConnectedPlayers
        PacketConnectedPlayers packetConnectedPlayers = new PacketConnectedPlayers(usernames, ips);
        // prépare le packet et envoie le packet en broadcast
        String content = new Gson().toJson(packetConnectedPlayers);
        broadcast(FormatPacket.format("PacketConnectedPlayers", content), false);
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

    public Partie getPartie() {
        return partie;
    }

    public int getNbJoueurs() {
        return nbJoueurs;
    }

    public void setServerView(server.view.Server serverView) {
        this.serverView = serverView;
        this.serverView.logInfo("Les paquets d'état courant de la partie ne sont pas affichées, pour ne pas surcharger les logs, " +
                "l'état de la partie est envoyé toutes les 100ms dès que tous les joueurs sont connectés.");
    }

    // add player to server view
    public void addPlayerToServerView(String username, String ip) {
        this.serverView.addPlayer(username, ip);
    }

    // add log sent packet
    public void logSentPacket(String to, String message) {
        this.serverView.logSentPacket(to, message);
    }

    // add log received packet
    public void logReceivedPacket(String from, String message) {
        this.serverView.logReceivedPacket(from, message);
    }

    public List<Integer> getWinnerCampIds() {
        int maxScore = 0;
        List<Integer> winnerCampIds = new ArrayList<>();
        Camp[] camps = partie.getCamps();

        for (Camp camp : camps) {
            int score = 0;

            for (Entity ressource : camp.getRessources()) {
                if (ressource instanceof Livestock) {
                    score += (int) (((Livestock) ressource).getHealth() * 2);
                } else if (ressource instanceof Wheat) {
                    score += 1;
                }
            }

            if (score > maxScore) {
                maxScore = score;
                winnerCampIds.clear();
                winnerCampIds.add(camp.getId());
            } else if (score == maxScore) {
                winnerCampIds.add(camp.getId());
            }

        }
        return winnerCampIds;
    }
}
