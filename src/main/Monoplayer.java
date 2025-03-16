package main;

import client.controler.ControlerClient;
import client.controler.ControlerParty;
import client.view.ViewClient;
import server.controler.ControlerServer;
import server.view.Server;

import javax.swing.*;

public class Monoplayer {
    public static void main(String[] args) {
        // Start the server in a new thread
        new Thread(() -> {
            Server serverView = new Server();
            ControlerServer serverControler = new ControlerServer(serverView);
            // Simulate button click to start the server
            serverView.getConnectButton().doClick();
        }).start();

        // Start the client in a new thread
        new Thread(() -> {
            ViewClient clientView = new ViewClient();
            ControlerClient clientControler = new ControlerClient(clientView);
            ControlerParty controlerParty = new ControlerParty(clientControler, clientView.getViewPartie());

            JFrame clientFrame = new JFrame();
            clientFrame.setIconImage(new ImageIcon("src/ressources/images/viking_ico_64.png").getImage());
            clientFrame.setTitle("Vikings");
            clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            clientFrame.add(clientView);
            clientFrame.pack();
            clientFrame.setVisible(true);

            // Simulate button click to connect the client
            clientView.getConnectButton().doClick();
        }).start();
        new Thread(() -> {
            ViewClient clientView = new ViewClient();
            ControlerClient clientControler = new ControlerClient(clientView);
            ControlerParty controlerParty = new ControlerParty(clientControler, clientView.getViewPartie());

            JFrame clientFrame = new JFrame();
            clientFrame.setIconImage(new ImageIcon("src/ressources/images/viking_ico_64.png").getImage());
            clientFrame.setTitle("Vikings");
            clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            clientFrame.add(clientView);
            clientFrame.pack();
            clientFrame.setVisible(true);

            // Simulate button click to connect the client
            clientView.getConnectButton().doClick();
        }).start();
    }
}