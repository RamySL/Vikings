package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Créer un client qui se connecte au serveur déterminé par l'adresse et le port.<p>
 * ? Peut etre enlver la creation de la socket du constructeur et la mettre dans une methode comme ça le constructeur
 * ne prend pas de param.
 */
public class Client {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String host, int port) {
        try {
            this.socket = new Socket(host,port);
        } catch (IOException e) {
            System.out.println("Création de socket échouée");
            throw new RuntimeException(e);
        }

        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Envoie un message au serveur.
     * @param message
     */
    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    /**
     * Recoit un message du serveur.
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

    public void closeConnection() {
        try {
            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

}

