package network.client;

import client.controler.ControlerClient;
import client.controler.ControlerParty;
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
    private Gson gson;
    private ControlerParty controlerParty;

    public ThreadCommunicationClient(Client client, ViewClient view) {
        this.client = client;
        this.view = view;
        gson = ModelAdapter.getGson();
        // send PacketUsername to the server
        this.client.sendMessage(FormatPacket.format("PacketUsername", gson.toJson(new PacketUsername(this.client.getUsername()))));
    }

    public void setControlerParty(ControlerParty controlerParty) {
        this.controlerParty = controlerParty;
    }

    @Override
    public void run() {
        while (true) {
            String msg = this.client.receiveMessage();
            if(msg == null){
                System.out.println("NIKMOK");
            }
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
                this.controlerParty.setPartie();
                if (!gameStarted) {
                    new ThreadRepaint(this.view.getViewPartie()).start();
                    gameStarted = true;
                    this.view.changeCard("3");
                }
                break;

            case "PaquetTimer":
                PaquetTimer paquetTimer = gson.fromJson(wrapper.content, PaquetTimer.class);
                this.view.getViewPartie().setTime(paquetTimer.getRemainingTime());
                break;

            case "PaquetEndGame":
                PaquetEndGame paquetEndGame = gson.fromJson(wrapper.content, PaquetEndGame.class);
                this.view.getViewPartie().setEndGame(paquetEndGame.getWinningCampIds());
                this.view.changeCard("4");
                break;
            case "PacketNewWarrior":
                PacketNewWarrior packetNewWarrior = gson.fromJson(wrapper.content, PacketNewWarrior.class);
                this.controlerParty.addNewWarrior(packetNewWarrior.getWarriorId(), packetNewWarrior.getX(), packetNewWarrior.getY());
                break;
            default:
                System.out.println("Invalid message format");
        }
    }


    public Client getClient() {
        return client;
    }

}