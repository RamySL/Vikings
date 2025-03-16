package network;

import com.google.gson.Gson;
import server.model.Camp;
import server.model.Farmer;
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
    private Warrior warriorSelected;
    private Farmer farmerSelected;
    private Gson gson = new Gson();

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
        // we send the camp id to the client
        this.sendMessage(FormatPacket.format("PacketCampId", gson.toJson(new PacketCampId(camp.getId()))));

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
        PacketWrapper wrapper = gson.fromJson(message, PacketWrapper.class);
        if (wrapper.type == null || wrapper.content == null) {
            System.out.println("Invalid message format");
            return;
        }

        switch (wrapper.type) {
            case "PaquetCoordClick":
                PaquetCoordClick paquet = gson.fromJson(wrapper.content, PaquetCoordClick.class);
                int x = paquet.getX();
                int y = paquet.getY();
                // On verifie si le click est sur le camp du client
                if (this.server.clickOncamp(this.camp.getId(), x, y)) {
                    if (firstClick) {
                        warriorSelected = DetermineSelectedWarrior(x, y);
                        if (warriorSelected == null) {
                            farmerSelected = DetermineSelectedFarmer(x, y);
                            if (farmerSelected != null) {
                                System.out.println("Farmer selected at position: " + farmerSelected.getPosition());
                                firstClick = false;
                                FarmerPositionChecker checker = new FarmerPositionChecker(this, camp, farmerSelected, 10);
                                checker.start();
                            }
                        } else {
                            System.out.println("Warrior selected at position: " + warriorSelected.getPosition());
                            firstClick = false;
                        }
                        // Et on envoi un message pour ouvrir le panneau (pas encore implementé)
                        this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl())));
                    } else {
                        // On déplace l'entité vers le click
                        if (warriorSelected != null) {
                            System.out.println("Moving warrior to position: " + x + ", " + y);
                            warriorSelected.move(new Point(x, y));
                        } else if (farmerSelected != null) {
                            System.out.println("Moving farmer to position: " + x + ", " + y);
                            farmerSelected.move(new Point(x, y));

                        }
                        firstClick = true;
                    }
                } else {
                    // click sur camp ennemie à traiter
                }
                break;
        }
    }

    // pour l'instant traite que les warrior on attendant de lire la doc Gson qui n'accepte pas les
    // classes abstraites
    public Warrior DetermineSelectedWarrior(int x, int y) {
        for (Warrior warrior : this.camp.getWarriors()) {
            if (Math.abs(warrior.getPosition().getX() + 400*this.camp.getId() - x) < 8 && Math.abs(warrior.getPosition().getY() - y) < 8) {
                return warrior;
            }
        }
        return null;
    }
    public Farmer DetermineSelectedFarmer(int x, int y) {
        for (Farmer farmer : this.camp.getFarmers()) {
            if (Math.abs(farmer.getPosition().getX() + 400*this.camp.getId() - x) < 8 && Math.abs(farmer.getPosition().getY() - y) < 8) {
                return farmer;
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
