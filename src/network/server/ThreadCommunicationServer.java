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
    private Viking vikingSelected;
    private Field fieldSelected;
    private Livestock livestockSelected;
    private Gson gson = new Gson();
    private MovementThread currentMovementThread;
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
        System.out.println("Je vais traiter un message du camp : " + camp.getId() );
        PacketWrapper wrapper = gson.fromJson(message, PacketWrapper.class);
        if (wrapper.type == null || wrapper.content == null) {
            System.out.println("Invalid message format");
            return;
        }
        if (wrapper.type.equals("PaquetExit")) {
            System.out.println("PaquetExit received");
            //this.firstClick = true;
            this.vikingSelected = null;
            this.fieldSelected = null;
            this.livestockSelected = null;
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
                // il ne sert à rien pour l'instant le farmer qui plante dans la méthode plant
                //vikingSelected = this.camp.getVikingByID(paquetPlant.getIdFarmer());
                fieldSelected= this.camp.getFieldByID(paquetPlant.getIdField());
                fieldSelected.plant(ressource);
                break;
            case "PaquetEat" :
                PaquetEat paquetEat = gson.fromJson(wrapper.content, PaquetEat.class);
                vikingSelected = this.camp.getVikingByID(paquetEat.getIdViking());
                livestockSelected = this.camp.getLivestockByID(paquetEat.getIdAnimal());
                vikingSelected.eat(livestockSelected);
                break;
            case "PaquetHarvest" :
                PaquetHarvest paquetHarvest = gson.fromJson(wrapper.content, PaquetHarvest.class);
                vikingSelected = this.camp.getVikingByID(paquetHarvest.getIdFarmer());
                fieldSelected = this.camp.getFieldByID(paquetHarvest.getIdField());
                fieldSelected.harvest();
                break;
            case "PacketMovement":
                PacketMovement packetMovement = gson.fromJson(wrapper.content, PacketMovement.class);
                Viking v = this.camp.getVikingByID(packetMovement.getId());
                v.stop();
                v.move(Position.viewToModel(packetMovement.getDst(),packetMovement.getTranslation(),packetMovement.getScale()));
                if (v instanceof Farmer){
                    FarmerPositionChecker checker = new FarmerPositionChecker(this, camp,(Farmer)v, 10);
                    checker.start();
                }
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
                    Point[] dsts =  Arrays.stream(packetAttack.getIdRessources()).mapToObj(id -> enemy.getFieldByID(id).getPosition())
                            .toArray(Point[]::new);
                    for (int i = 0; i<NbVikings.length; i++){
                        camp.attack(NbVikings[i], enemy.getId(), dsts[i]);
                    }
                }
                // position
                //Point campToAtt =  this.server.getPartie().getCamp(packetAttack.getIdCamp()).getPosition();
                break;
        }
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
