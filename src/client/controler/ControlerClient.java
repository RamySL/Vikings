package client.controler;

import network.client.ThreadCommunicationClient;
import network.client.Client;
import client.view.ViewClient;

import java.awt.event.*;

/**
 * Gère les évenements déclenchés par l'utilisateur en interagissant avec l'interface graphique du jeu
 */
public class ControlerClient extends MouseAdapter implements ActionListener, MouseListener {
    private ThreadCommunicationClient threadCommunicationClient;
    private ViewClient view;
    private Client client;


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
            this.client=new Client(this.view.getIp(), this.view.getPort());
            this.threadCommunicationClient = new ThreadCommunicationClient(client, this.view);
            this.threadCommunicationClient.start();
        }

    }


    public ThreadCommunicationClient getThreadCommunicationClient() {
        return threadCommunicationClient;
    }
}
