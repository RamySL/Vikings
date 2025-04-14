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
    public static  final Object lock = new Object();
    private ControlerParty controlerParty;


    public ControlerClient(ViewClient view) {
        this.view = view;
        this.view.getViewPartie().addMouseListener(this);
        this.view.getConnectButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.view.getConnectButton()) {
            this.client = new Client(this.view.getIp(), this.view.getPort(), this.view.getUsername());
            this.threadCommunicationClient = new ThreadCommunicationClient(client, this.view);
            this.threadCommunicationClient.setControlerParty(controlerParty);
            this.threadCommunicationClient.start();
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
            this.view.changeCard("2");
        }
    }

    public void setControlerParty(ControlerParty controlerParty) {
        this.controlerParty = controlerParty;
    }

    public ThreadCommunicationClient getThreadCommunicationClient() {
        return threadCommunicationClient;
    }
}
