package network.client;

import client.controler.ControlerClient;
import network.ModelAdapter;
import network.packets.*;
import server.model.*;
import client.view.ThreadRepaint;
import client.view.ViewClient;
import com.google.gson.Gson;


/**
 * ThreadCommunicationClient is a class that handles the communication between the client and the server.
 * It receives messages from the server and updates the view accordingly.
 * This class is responsible for processing the messages received from the server and updating the view.
 * It runs in a separate thread to ensure that the client can continue to receive messages while the user interacts with the UI.
 * It uses the Gson library to parse JSON messages and update the view.
 * It also handles the visibility of the control panel and updates the farmer and sheep positions.
 * It is a crucial part of the client-side implementation of the game.
 * It extends the Thread class to run in a separate thread.
 */
public class ThreadCommunicationClient extends Thread {
    private Client client;
    private boolean gameStarted = false;
    private ViewClient view;
    private boolean isFieldPlanted;
    private int idFarmer, idField, idSheep;
    private  FarmerSheepWrapper farmerSheepWrapper;
    private FarmerFieldWrapper farmerFieldWrapper;
    private Gson gson;

    public ThreadCommunicationClient(Client client, ViewClient view) {
        this.client = client;
        this.view = view;
        gson = ModelAdapter.getGson();
        // send PacketUsername to the server
        this.client.sendMessage(FormatPacket.format("PacketUsername", gson.toJson(new PacketUsername(this.client.getUsername()))));
    }

    @Override
    public void run() {
        while (true) {
            String msg = this.client.receiveMessage();
            reactMessage(msg);
        }
    }

    public void reactMessage(String msg) {

        PacketWrapper wrapper = gson.fromJson(msg, PacketWrapper.class);
        switch (wrapper.type) {
            case "PacketCampIdNbPlayers":
                PacketCampIdNbPlayers pCampId = gson.fromJson(wrapper.content, PacketCampIdNbPlayers.class);
                this.view.setViewWaiting(pCampId.getNbPlayers());
                this.view.getViewPartie().setOffsetCampID(pCampId.getCampId());
                synchronized (ControlerClient.lock) {
                    ControlerClient.lock.notify();
                }
                break;

            case "PacketConnectedPlayers":
                PacketConnectedPlayers packetConnectedPlayers = gson.fromJson(wrapper.content, PacketConnectedPlayers.class);
                String[] usernames = packetConnectedPlayers.getPseudos();
                String[] ips = packetConnectedPlayers.getIps();
                this.view.addConnectedPlayers(usernames, ips);
                break;

            case "Partie":
                Partie game = gson.fromJson(wrapper.content, Partie.class);
                this.view.actualisePartie(game);
                if (!gameStarted) {
                    new ThreadRepaint(this.view.getViewPartie()).start();
                    gameStarted = true;
                    // un petit délai d'attente avant de changer vers la vue de la partie
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    this.view.changeCard("3");
                }
                break;
                // les 4 prochains ne sont pas dans packets (ils sont dans FarmerPositionChecker)
            case "FarmerNearField":
                farmerFieldWrapper = gson.fromJson(wrapper.content, FarmerFieldWrapper.class);
                idFarmer = farmerFieldWrapper.getIdFarmer();
                isFieldPlanted = farmerFieldWrapper.getIsPlanted();
                idField = farmerFieldWrapper.getIdField();
                this.view.getViewPartie().panelSetFarmerOnField(true,idFarmer, idField, isFieldPlanted);
                break;
            case "FarmerNotNearField":
                farmerFieldWrapper = gson.fromJson(wrapper.content, FarmerFieldWrapper.class);
                idFarmer = farmerFieldWrapper.getIdFarmer();
                idField = farmerFieldWrapper.getIdField();
                isFieldPlanted = farmerFieldWrapper.getIsPlanted();
                this.view.getViewPartie().panelSetFarmerOnField(false,idFarmer, idField, isFieldPlanted);
                break;
            case "FarmerNearSheep":
                farmerSheepWrapper = gson.fromJson(wrapper.content, FarmerSheepWrapper.class);
                idFarmer = farmerSheepWrapper.getIdFarmer();
                idSheep = farmerSheepWrapper.getIdSheep();
                this.view.getViewPartie().panelSetFarmerNearSheep(true, idFarmer, idSheep);
                break;
            case "FarmerNotNearSheep":
                farmerSheepWrapper = gson.fromJson(wrapper.content, FarmerSheepWrapper.class);
                idFarmer = farmerSheepWrapper.getIdFarmer();
                idSheep = farmerSheepWrapper.getIdSheep();
                this.view.getViewPartie().panelSetFarmerNearSheep(false, idFarmer, idSheep);
                break;

            default:
                System.out.println("Invalid message format");
        }
    }


    public Client getClient() {
        return client;
    }

}