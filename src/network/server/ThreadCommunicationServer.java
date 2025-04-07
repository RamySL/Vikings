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
    private Sheep sheepSelected;
    private Gson gson = new Gson();
    private MovementThread currentMovementThread;

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
        if (wrapper.type.equals("PaquetExit")) {
            System.out.println("PaquetExit received");
            this.firstClick = true;
            this.warriorSelected = null;
            this.farmerSelected = null;
            this.fieldSelected = null;
            this.sheepSelected = null;
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

                farmerSelected=DetermineSelectedFarmer(farmerX, farmerY);
                fieldSelected=DetermineSelectedField(fieldX, fieldY);
                fieldSelected.plant(ressource);
                break;
            case "PaquetEat" :
                PaquetEat paquetEat = gson.fromJson(wrapper.content, PaquetEat.class);
                int vikingX = paquetEat.getFarmerX();
                int vikingY = paquetEat.getFarmerY();
                int animalX = paquetEat.getAnimalX();
                int animalY = paquetEat.getAnimalY();
                farmerSelected = DetermineSelectedFarmer(vikingX, vikingY);
                sheepSelected = DetermineSelectedSheep(animalX, animalY);
                farmerSelected.eat(sheepSelected);
                break;
            case "PaquetHarvest" :
                PaquetHarvest paquetHarvest = gson.fromJson(wrapper.content, PaquetHarvest.class);
                int farmerXHarvest = paquetHarvest.getFarmerX();
                int farmerYHarvest = paquetHarvest.getFarmerY();
                int fieldXHarvest = paquetHarvest.getFieldX();
                int fieldYHarvest = paquetHarvest.getFieldY();
                farmerSelected=DetermineSelectedFarmer(farmerXHarvest, farmerYHarvest);
                fieldSelected=DetermineSelectedField(fieldXHarvest, fieldYHarvest);
                fieldSelected.harvest();
                break;
            case "PaquetClick":
                PaquetClick paquet = gson.fromJson(wrapper.content, PaquetClick.class);
                Point viewPoint = new Point(paquet.getX(), paquet.getY());
                Point translation = new Point(paquet.getTranslationX(), paquet.getTranslationY());
                Point modelPoint = Position.viewToModel(viewPoint, translation, paquet.getScale());
                // On verifie si le click est sur le camp du client
                if  (Position.isInCamp(this.camp.getId(), modelPoint.x, modelPoint.y)) {
                    if (firstClick) {
                        this.sendMessage(FormatPacket.format("ClicSurAutreChose", "{}"));
                        warriorSelected = DetermineSelectedWarrior(modelPoint.x, modelPoint.y);
                        if (warriorSelected == null) {
                            farmerSelected = DetermineSelectedFarmer(modelPoint.x, modelPoint.y);
                            fieldSelected= DetermineSelectedField(modelPoint.x, modelPoint.y);
                            sheepSelected = DetermineSelectedSheep(modelPoint.x, modelPoint.y);
                            if (farmerSelected != null) {
                                this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Farmer", farmerSelected.getHealth()))));
                                firstClick = false;
                                FarmerPositionChecker checker = new FarmerPositionChecker(this, camp, farmerSelected, 10);
                                checker.start();
                            }

                            else if (fieldSelected != null) {
                                if (fieldSelected.isPlanted()){
                                    this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Field", fieldSelected.isPlanted(), fieldSelected.getResource()))));
                                }
                                else{
                                    this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Field", fieldSelected.isPlanted()))));
                                }
                            }

                            else if (sheepSelected != null) {
                                this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Sheep", sheepSelected.getHealth()))));
                            }
                        } else {
                            this.sendMessage(FormatPacket.format("PacketOpenPanelControl", gson.toJson(new PacketOpenPanelControl("Warrior", warriorSelected.getHealth()))));
                            firstClick = false;
                        }
                        // Et on envoi un message pour ouvrir le panneau (pas encore implementé)

                        if (fieldSelected == null && farmerSelected == null && warriorSelected == null && sheepSelected == null) {
                            this.sendMessage(FormatPacket.format("ClicSurAutreChose", "{}"));
                        }
                    } else {
                        if (warriorSelected != null) {
                            if (currentMovementThread != null && currentMovementThread.isAlive()) {
                                currentMovementThread.stopMovement();
                                currentMovementThread = new MovementThread(warriorSelected, new Point(modelPoint.x, modelPoint.y));
                                currentMovementThread.start();
                            } else {
                                currentMovementThread = new MovementThread(warriorSelected, new Point(modelPoint.x, modelPoint.y));
                                currentMovementThread.start();
                            }
                            this.sendMessage(FormatPacket.format("ClicSurAutreChose", "{}"));
                        } else if (farmerSelected != null) {
                            if (currentMovementThread != null && currentMovementThread.isAlive()) {
                                currentMovementThread.stopMovement();
                                currentMovementThread = new MovementThread(farmerSelected, new Point(modelPoint.x, modelPoint.y));
                                currentMovementThread.start();
                            } else {
                                currentMovementThread = new MovementThread(farmerSelected, new Point(modelPoint.x, modelPoint.y));
                                currentMovementThread.start();
                            }
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

        for (Warrior warrior : this.camp.getWarriors()) {
            // top left point of the warrior
            Point topLeft = new Point(warrior.getPosition().x - Position.WIDTH_VIKINGS/2, warrior.getPosition().y+Position.HEIGHT_VIKINGS/2);
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
     * @return farmer
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


    /**
     * takes model coordinates and returns the field that is at that position
     * @param x
     * @param y
     * @return field
     */
    public Field DetermineSelectedField(int x, int y){
        System.out.println("DetermineSelectedField "+ x + " " + y);
        for (Field field : this.camp.getFields()) {
            Point topLeft = new Point(field.getPosition().x - Position.WIDTH_FIELD/2, field.getPosition().y+Position.HEIGHT_FIELD/2);
            System.out.println("topLeft "+ topLeft.x + " " + topLeft.y);
            if (x>=topLeft.x && x<=topLeft.x+Position.WIDTH_FIELD && y>=topLeft.y && y>=topLeft.y-Position.HEIGHT_FIELD) {
                return field;
            }
        }
        return null;
    }

    /**
     * takes model coordinates and returns the sheep that is at that position
     * @param x
     * @param y
     * @return sheep
     */
    public Sheep DetermineSelectedSheep(int x, int y){
        for (Sheep sheep : this.camp.getSheep()) {
            Point topLeft = new Point(sheep.getPosition().x - Position.WIDTH_SHEEP/2, sheep.getPosition().y+Position.HEIGHT_SHEEP/2);
            if (x>=topLeft.x && x<=topLeft.x+Position.WIDTH_SHEEP && y<=topLeft.y && y>=topLeft.y-Position.HEIGHT_SHEEP) {
                return sheep;
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
