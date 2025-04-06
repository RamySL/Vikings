package main;

import client.controler.ControlerClient;
import client.controler.ControlerParty;
import client.view.ViewClient;
import server.view.Server;

import javax.swing.*;

public class Monoplayer {
    public static void main(String[] args) {
        // mettez le nombre de joueur que vous voulez
        int nbJR = 4;
        new Thread(() -> {
            Server serverView = new Server();
            // Simulate button click to start the server
            network.server.Server server = new network.server.Server(50123,nbJR);
            // lance un thread avec la méthode launch
            new Thread(() -> server.launch()).start();
        }).start();

        for (int i = 0; i < nbJR; i++) {
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
}