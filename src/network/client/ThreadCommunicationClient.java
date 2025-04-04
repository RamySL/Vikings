package network.client;

import network.packets.FarmerFieldWrapper;
import network.packets.PacketCampId;
import network.packets.PacketOpenPanelControl;
import network.packets.PacketWrapper;
import server.model.*;
import client.view.ThreadRepaint;
import client.view.ViewClient;
import com.google.gson.Gson;


/**
 * Thread qui écouter ce que le serveur envoie et actualise l'interface graphique du client
 */
public class ThreadCommunicationClient extends Thread {
    private Client client;
    private boolean gameStarted = false;
    private ViewClient view;

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
                //System.out.println("Received FarmerNearField message, showing Plant button.");
                FarmerFieldWrapper farmerFieldWrapper = gson.fromJson(wrapper.content, FarmerFieldWrapper.class);
                int farmerX = farmerFieldWrapper.getFarmerX();
                int farmerY = farmerFieldWrapper.getFarmerY();
                int fieldX = farmerFieldWrapper.getFieldX();
                int fieldY = farmerFieldWrapper.getFieldY();
                boolean isFieldPlanted = farmerFieldWrapper.getIsPlanted();
                this.view.getViewPartie().getPanneauControle().setFarmerOnField(true, farmerX, farmerY, fieldX, fieldY, isFieldPlanted);
                break;
            case "FarmerNotNearField":
                //System.out.println("Received FarmerNotNearField message, hiding Plant button.");
                farmerFieldWrapper = gson.fromJson(wrapper.content, FarmerFieldWrapper.class);
                farmerX = farmerFieldWrapper.getFarmerX();
                farmerY = farmerFieldWrapper.getFarmerY();
                fieldX = farmerFieldWrapper.getFieldX();
                fieldY = farmerFieldWrapper.getFieldY();
                isFieldPlanted = farmerFieldWrapper.getIsPlanted();
                // Le fermier n'est pas sur un champ, donc on cache le bouton Planter
                this.view.getViewPartie().getPanneauControle().setFarmerOnField(false, farmerX, farmerY, fieldX, fieldY, isFieldPlanted);
                break;
            case "PacketOpenPanelControl":
                PacketOpenPanelControl pCtrl = gson.fromJson(wrapper.content, PacketOpenPanelControl.class);
                break;
            case "ClicSurAutreChose":
                //System.out.println("Received ClicSurAutreChose message, hiding Plant button.");
                // Le joueur a cliqué sur autre chose que le bouton Planter, donc on cache le bouton Planter
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