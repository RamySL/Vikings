package network.client;

import network.packets.*;
import server.model.*;
import client.view.ThreadRepaint;
import client.view.ViewClient;
import com.google.gson.Gson;

import java.util.Objects;


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
    private int sheepX, sheepY, farmerX, farmerY, fieldX, fieldY;
    private boolean isFieldPlanted;
    private  FarmerSheepWrapper farmerSheepWrapper;
    private FarmerFieldWrapper farmerFieldWrapper;

    public ThreadCommunicationClient(Client client, ViewClient view) {
        this.client = client;
        this.view = view;
    }

    @Override
    public void run() {
        while (true) {
            String msg = this.client.receiveMessage();
            reactMessage(msg);
        }
    }


    public void reactMessage(String msg) {
        Gson gson = new Gson();
        PacketWrapper wrapper = gson.fromJson(msg, PacketWrapper.class);
        switch (wrapper.type) {
            case "Partie":
                Partie game = gson.fromJson(wrapper.content, Partie.class);
                this.view.actualisePartie(game);
                if (!gameStarted) {
                    this.view.changeCard("3");
                    new ThreadRepaint(this.view.getViewPartie()).start();
                    gameStarted = true;
                }
                break;
            case "FarmerNearField":
                farmerFieldWrapper = gson.fromJson(wrapper.content, FarmerFieldWrapper.class);
                farmerX = farmerFieldWrapper.getFarmerX();
                farmerY = farmerFieldWrapper.getFarmerY();
                fieldX = farmerFieldWrapper.getFieldX();
                fieldY = farmerFieldWrapper.getFieldY();
                isFieldPlanted = farmerFieldWrapper.getIsPlanted();
                this.view.getViewPartie().getPanneauControle().setFarmerOnField(true, farmerX, farmerY, fieldX, fieldY, isFieldPlanted);
                break;
            case "FarmerNotNearField":
                farmerFieldWrapper = gson.fromJson(wrapper.content, FarmerFieldWrapper.class);
                farmerX = farmerFieldWrapper.getFarmerX();
                farmerY = farmerFieldWrapper.getFarmerY();
                fieldX = farmerFieldWrapper.getFieldX();
                fieldY = farmerFieldWrapper.getFieldY();
                isFieldPlanted = farmerFieldWrapper.getIsPlanted();
                this.view.getViewPartie().getPanneauControle().setFarmerOnField(false, farmerX, farmerY, fieldX, fieldY, isFieldPlanted);
                break;
            case "FarmerNearSheep":
                farmerSheepWrapper = gson.fromJson(wrapper.content, FarmerSheepWrapper.class);
                farmerX = farmerSheepWrapper.getFarmerX();
                farmerY = farmerSheepWrapper.getFarmerY();
                sheepX = farmerSheepWrapper.getSheepX();
                sheepY = farmerSheepWrapper.getSheepY();
                this.view.getViewPartie().getPanneauControle().setFarmerNearSheep(true, farmerX, farmerY, sheepX, sheepY);
                break;
            case "FarmerNotNearSheep":
                farmerSheepWrapper = gson.fromJson(wrapper.content, FarmerSheepWrapper.class);
                farmerX = farmerSheepWrapper.getFarmerX();
                farmerY = farmerSheepWrapper.getFarmerY();
                sheepX = farmerSheepWrapper.getSheepX();
                sheepY = farmerSheepWrapper.getSheepY();
                this.view.getViewPartie().getPanneauControle().setFarmerNearSheep(false, farmerX, farmerY, sheepX, sheepY);
                break;
            case "PacketOpenPanelControl":
                PacketOpenPanelControl pCtrl = gson.fromJson(wrapper.content, PacketOpenPanelControl.class);
                this.view.getViewPartie().getPanneauControle().setVisibility(true);
                String entityType = pCtrl.getEntityType();

                float health;
                if (Objects.equals(entityType, "Farmer")) {
                    health= pCtrl.getHealth();
                    this.view.getViewPartie().getPanneauControle().showInfos(entityType, health);
                }
                else if (Objects.equals(entityType, "Sheep")) {
                    health = pCtrl.getHealth();
                    this.view.getViewPartie().getPanneauControle().showInfos(entityType, health);
                }
                else if (Objects.equals(entityType, "Cow")) {
                    health = pCtrl.getHealth();
                    this.view.getViewPartie().getPanneauControle().showInfos(entityType, health);
                }
                else if (Objects.equals(entityType, "Warrior")) {
                    health = pCtrl.getHealth();
                    this.view.getViewPartie().getPanneauControle().showInfos(entityType, health);
                }
                else if (Objects.equals(entityType, "Field")){
                    boolean isPlanted = pCtrl.isPlanted();
                    if (isPlanted){
                        String ressource = pCtrl.getRessource();
                        this.view.getViewPartie().getPanneauControle().showInfos(entityType, ressource);
                    }
                    else {
                        this.view.getViewPartie().getPanneauControle().showInfos(entityType);
                    }

                }

                break;
            case "ClicSurAutreChose":
                this.view.getViewPartie().getPanneauControle().elseWhereClicked();
                break;
            case "PacketCampId":
                PacketCampId pCampId = gson.fromJson(wrapper.content, PacketCampId.class);
                this.view.getViewPartie().setOffsetCampID(pCampId.getCampId());
                break;
            default:
                System.out.println("Invalid message format");
        }
    }


    public Client getClient() {
        return client;
    }

}