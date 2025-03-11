package client.controler;

import com.google.gson.Gson;
import network.FormatPacket;
import network.PaquetCoordClick;
import network.ThreadCommunicationClient;
import network.Client;
import client.view.ViewClient;

import java.awt.event.*;

/**
 * Gère les évenements déclenchés par l'utilisateur en interagissant avec l'interface graphique du jeu
 */
public class ControlerClient extends MouseAdapter implements ActionListener, MouseListener {
    private ThreadCommunicationClient threadCommunicationClient;
    private ViewClient view;

    public ControlerClient(ViewClient view) {
        this.view = view;

        this.view.getViewPartie().addMouseListener(this);
        this.view.getConnectButton().addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.view.getConnectButton()){
            this.view.changeCard("2");
            // On écoute le serveur.// norlement je recupere les infos saisie dans les field
            this.threadCommunicationClient = new ThreadCommunicationClient(new Client("localhost", 1234), this.view);
            this.threadCommunicationClient.start();
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {
        Gson gson = new Gson();
        String contentPaquet = gson.toJson(new PaquetCoordClick(e.getX(),e.getY()));

        // ! ! je pense sans le get c'est mieux
        this.threadCommunicationClient.getClient().sendMessage(FormatPacket.format("PaquetCoordClick",contentPaquet));
    }


}
