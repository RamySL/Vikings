package network.client;

import client.controler.ControlerClient;
import network.VikingAdapter;
import network.packets.*;
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
    private Gson gson;

    public ThreadCommunicationClient(Client client, ViewClient view) {
        this.client = client;
        this.view = view;
        gson = VikingAdapter.getGson();
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
            default:
                System.out.println("Invalid message format");
        }
    }

    public Client getClient() {
        return client;
    }

}