package controler;

import model.ModelServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Le thread que le serveur va lancer pour chaque connexion avec un client pour comminuquer avec lui
 * et donc communiquer avec tout le monde en parallel.
 */
public class ThreadCommunicationServer extends Thread{
    private ModelServer server;
    private Socket client;
    // Les flux d'IO des sockets
    private PrintWriter out;
    private BufferedReader in;


    public ThreadCommunicationServer(ModelServer server, Socket client){
        this.client = client;
        this.server = server;
        try {
            this.out = new PrintWriter(this.client.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.in = new BufferedReader(new java.io.InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run(){
        String msg;
        while (true) {
            msg = this.receiveMessage();
            if(!msg.isEmpty()) {
                System.out.println(client.getInetAddress() + " : " + msg);
                reactMessage(msg);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Envoie un message au client
     * @param message
     */
    public void sendMessage(String message) {
        this.out.println(message);
        this.out.flush();
    }

    /**
     * Réagit sur le modèle selon le message reçu de la part du client
     * @param message
     */
    public void reactMessage(String message) {

        switch (message) {
            default:
                this.sendMessage("Commande non reconnue");
        }
    }

    /**
     * Recoit un message du client, en lisant sur le flux d'entrée de la socket.
     * @return
     */
    public String receiveMessage() {
        String res = "";
        try {
            res = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
}
