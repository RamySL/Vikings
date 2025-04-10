package network.server;

import network.packets.*;
import server.model.*;

import com.google.gson.Gson;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

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
    private Farmer farmerSelected;
    private Field fieldSelected;
    private Gson gson = new Gson();
    // username concat avec IP
    private String usernameIP;

    public ThreadCommunicationServer(Server server, Socket client, Camp camp) {
        this.camp=camp;
        this.client = client;
        this.server = server;
        try {
            this.out = new PrintWriter(this.client.getOutputStream());
            this.in = new BufferedReader(new java.io.InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.sendMessage(FormatPacket.format("PacketCampIdNbPlayers", gson.toJson(new PacketCampIdNbPlayers(camp.getId(), server.getNbJoueurs()))), false);

    }

    @Override
    public void run(){
        // print the THread id
        String msg;
        while (true) {
            msg = this.receiveMessage();
            System.out.println(client.getInetAddress() + " : " + msg);
            reactMessage(msg);
        }

    }

    /**
     * Envoie un message au client
     * @param message
     */
    public void sendMessage(String message, boolean gameState) {
        if(!gameState){
            this.server.logSentPacket(this.usernameIP, message);
        }
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
            case "PacketUsername":
                PacketUsername packetUsername = gson.fromJson(wrapper.content, PacketUsername.class);
                String username = packetUsername.getUsername();
                this.usernameIP = username + ": " + client.getInetAddress().getHostAddress();
                this.server.addPlayerToServerView(username, this.client.getInetAddress().getHostAddress());
                this.camp.setUsername(username);
                // ce
                this.server.broadcastUsernames();
                break;
            case "PaquetPlant":
                PaquetPlant paquetPlant = gson.fromJson(wrapper.content, PaquetPlant.class);
                String ressource= paquetPlant.getResource();
                int farmerX = paquetPlant.getFarmerX();
                int farmerY = paquetPlant.getFarmerY();
                int fieldX = paquetPlant.getFieldX();
                int fieldY = paquetPlant.getFieldY();

                System.out.println("Received PaquetPlant with coordinates: Farmer(" + farmerX + ", " + farmerY + "), Field(" + fieldX + ", " + fieldY + ")");
                farmerSelected=DetermineSelectedFarmer(farmerX, farmerY);
                fieldSelected=DetermineSelectedField(fieldX, fieldY);
                fieldSelected.plant(ressource);
                break;
            case "PacketMovement":
                PacketMovement packetMovement = gson.fromJson(wrapper.content, PacketMovement.class);
                Viking v = this.camp.getVikingByID(packetMovement.getId());
                v.move(Position.viewToModel(packetMovement.getDst(),packetMovement.getTranslation(),packetMovement.getScale()));
                break;
            case "PacketAttack":
                // On vérifie si l'attaque est valide
                PacketAttack packetAttack = gson.fromJson(wrapper.content, PacketAttack.class);
                int[] NbVikings = packetAttack.getNbVikings();
                int nbVRequired = Arrays.stream(NbVikings).sum();
                if(this.camp.getWarriorsInCamp().size() < nbVRequired) {
                    System.out.println("Not enough vikings to attack");
                    return;
                }else{
                    Camp enemy = this.server.getPartie().getCamp(packetAttack.getIdCamp());
                    Point[] dsts =  Arrays.stream(packetAttack.getIdRessources()).mapToObj(id -> enemy.getFieldById(id).getPosition())
                            .toArray(Point[]::new);
                    for (int i = 0; i<NbVikings.length; i++){
                        camp.attack(NbVikings[i], enemy.getId(), dsts[i]);
                    }
                }


                // position

                Point campToAtt =  this.server.getPartie().getCamp(packetAttack.getIdCamp()).getPosition();
                break;
        }
    }


    /**
     * takes model coordinates and returns the farmer that is at that position
     * @param x
     * @param y
     * @return
     */
    public Farmer DetermineSelectedFarmer(int x, int y) {
        for (Farmer farmer : this.camp.getFarmers()) {
            Point topLeft = new Point(farmer.getPosition().x - Position.WIDTH_VIKINGS/2, farmer.getPosition().y+Position.HEIGHT_VIKINGS/2);
            if (x>=topLeft.x && x<=topLeft.x+Position.WIDTH_VIKINGS && y<=topLeft.y && y>=topLeft.y-Position.HEIGHT_VIKINGS) {
                return farmer;
            }
        }
        return null;
    }

    public Field DetermineSelectedField(int x, int y){
        for (Field field : this.camp.getFields()) {
            Point topLeft = new Point(field.getPosition().x - Position.WIDTH_FIELD/2, field.getPosition().y+Position.HEIGHT_FIELD/2);
            if (x>=topLeft.x && x<=topLeft.x+Position.WIDTH_FIELD && y<=topLeft.y && y>=topLeft.y-Position.HEIGHT_FIELD) {
                return field;
            }
        }
        return null;
    }
    /**
     * Recoit un message du client, en lisant sur le flux d'entrée de la socket.<p>
     * Cette méthode est bloquante.
     * @return
     */
    public String receiveMessage() {
        String res;
        try {
            res = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.server.logReceivedPacket(this.usernameIP, res);
        return res;
    }

    public Camp getCamp() {
        return camp;
    }

    public Socket getClient() {
        return client;
    }
}
