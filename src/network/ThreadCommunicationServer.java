package network;

import com.google.gson.Gson;
import server.model.Camp;
import server.model.Warrior;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Le thread que le serveur va lancer pour chaque connexion avec un client pour comminuquer avec lui
 * et donc communiquer avec tout le monde en parallel.
 */
public class ThreadCommunicationServer extends Thread{
    private Server server;
    private Socket client;
    // Les flux d'IO des sockets
    private PrintWriter out;
    private BufferedReader in;
    private Camp camp;
    private boolean firstClick = true;
    private Warrior entitySelected;

    public ThreadCommunicationServer(Server server, Socket client, Camp camp) {
        this.camp=camp;
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
        Gson gson = new Gson();
        PacketWrapper wrapper = gson.fromJson(message, PacketWrapper.class);
        if (wrapper.type == null || wrapper.content == null) {
            System.out.println("Invalid message format");
            return;
        }
        // On demande au serveur si le clic concerne son propre camp au client, sinon on lui affiche un panneau
        // Avec les infos du camp et un bouton attaquer

        // Si c'est le premier click alors
            // Si c'est son camp on parcoure toutes les entité et on revoi par le reseau une instruction pour ouvrir son panneau
        // Sinon:
            // On déplace l'entité vers le click (si entité selectionné)

        switch (wrapper.type){
            case "PaquetCoordClick":
                PaquetCoordClick paquet = gson.fromJson(wrapper.content, PaquetCoordClick.class);
                int x = paquet.getX();
                int y = paquet.getY();
                // On verifie si le click est sur le camp du client
                if(this.server.clickOncamp(this.camp.getId(),x,y)){
                    if(firstClick){
                        entitySelected = DetermineSelected(x,y);
                        if(entitySelected!=null){
                            firstClick=false;
                        }
                        // Et on envoi un message pour ouvrir le panneau (pas encore implementé)
                    }else {
                        // On déplace l'entité vers le click
                        if(entitySelected!=null){
                            entitySelected.move(new Point(x, y));
                        }
                        firstClick = true;
                    }
                }else{
                    //click sur camp ennemie à traiter
                }
        }


    }

    // pour l'instant traite que les warrior on attendant de lire la doc Gson qui n'accepte pas les
    // classes abstraites
    public Warrior DetermineSelected(int x, int y) {
        for (Warrior warrior : this.camp.getVikings()) {
            if (Math.abs(warrior.getPosition().getX() + 400*this.camp.getId() - x) < 8 && Math.abs(warrior.getPosition().getY() - y) < 8) {
                return warrior;
            }
        }
        return null;
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

    public Camp getCamp() {
        return camp;
    }
}
