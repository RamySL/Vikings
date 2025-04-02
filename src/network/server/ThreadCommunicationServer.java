package network.server;

import network.packets.*;
import server.model.*;

import com.google.gson.Gson;
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
    private Field fieldSelected;
    private Gson gson = new Gson();

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
        //we send the camp id to the client
        this.sendMessage(FormatPacket.format("PacketCampId", gson.toJson(new PacketCampId(camp.getId()))));

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
            case "PaquetEat" :
                break;
            case "PaquetClick":
                PaquetClick paquet = gson.fromJson(wrapper.content, PaquetClick.class);
                Point viewPoint = new Point(paquet.getX(), paquet.getY());
                Point translation = new Point(paquet.getTranslationX(), paquet.getTranslationY());
                Point modelPoint = Position.viewToModel(viewPoint, translation, paquet.getScale());
                // On verifie si le click est sur le camp du client
                if  (Position.isInCamp(this.camp.getId(), modelPoint.x, modelPoint.y)) {
                    System.out.println("Click in camp");
                    if (firstClick) {
                        this.sendMessage(FormatPacket.format("ClicSurAutreChose", "{}"));
                        warriorSelected = DetermineSelectedWarrior(modelPoint.x, modelPoint.y);
                        if (warriorSelected == null) {
                            farmerSelected = DetermineSelectedFarmer(modelPoint.x, modelPoint.y);
                            if (farmerSelected != null) {
                                System.out.println("Farmer selected at position: " + farmerSelected.getPosition());
                                this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Farmer", farmerSelected.getHealth()))));
                                firstClick = false;
                                FarmerPositionChecker checker = new FarmerPositionChecker(this, camp, farmerSelected, 10);
                                checker.start();
                            }
                            fieldSelected= DetermineSelectedField(modelPoint.x, modelPoint.y);
                            if (fieldSelected != null) {
                                System.out.println("Field selected at position: " + fieldSelected.getPosition());
                                if (fieldSelected.isPlanted()){
                                    this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Field", fieldSelected.isPlanted(), fieldSelected.getResource()))));
                                }
                                else{
                                    this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Field", fieldSelected.isPlanted()))));
                                }
                            }
                        } else {
                            System.out.println("Warrior selected at position: " + warriorSelected.getPosition());
                            this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Warrior", warriorSelected.getHealth()))));
                            firstClick = false;
                        }
                        // Et on envoi un message pour ouvrir le panneau (pas encore implementé)
                        //this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl())));

                        if (fieldSelected == null && farmerSelected == null && warriorSelected == null) {
                            this.sendMessage(FormatPacket.format("ClicSurAutreChose", "{}"));
                        }
                    } else {
                        // On déplace l'entité vers le click
                        if (warriorSelected != null) {
                            System.out.println("Moving warrior to position: " + modelPoint.x + ", " + modelPoint.y + " in model ");
                            warriorSelected.move(new Point(modelPoint.x, modelPoint.y));
                            this.sendMessage(FormatPacket.format("ClicSurAutreChose", "{}"));
                        } else if (farmerSelected != null) {
                            System.out.println("Moving farmer to position: " + modelPoint.x + ", " + modelPoint.y + " in model ");
                            farmerSelected.move(new Point(modelPoint.x, modelPoint.y));
                            this.sendMessage(FormatPacket.format("ClicSurAutreChose", "{}"));
                        }
                        firstClick = true;

                    }
                } else {
                    // click sur camp ennemie à traiter
                    this.sendMessage(FormatPacket.format("ClicSurAutreChose", "{}"));
                }
                break;
        }
    }

    /**
     * takes model coordinates and returns the warrior that is at that position
     * @param x
     * @param y
     * @return
     */
    public Warrior DetermineSelectedWarrior(int x, int y) {
        System.out.println("Determining selected warrior");
        System.out.println("x: " + x + " y: " + y);

        for (Warrior warrior : this.camp.getWarriors()) {
            // top left point of the warrior
            Point topLeft = new Point(warrior.getPosition().x - Position.WIDTH_VIKINGS/2, warrior.getPosition().y+Position.HEIGHT_VIKINGS/2);
            System.out.println("Top left point of warrior: " + topLeft);
            if (x>=topLeft.x && x<=topLeft.x+Position.WIDTH_VIKINGS && y<=topLeft.y && y>=topLeft.y-Position.HEIGHT_VIKINGS) {
                return warrior;
            }
        }
        return null;
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
            System.out.println(field.getPosition().x + ", " + field.getPosition().y);
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
