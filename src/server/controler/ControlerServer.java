package server.controler;

import server.view.LogPanel;
import server.view.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Gère les évenements déclenchés par l'utilisateur en interagissant avec l'interface graphique
 * du serveur. Lance le thread du serveur quand l'utilisateur clique sur le bouton.
 */
public class
ControlerServer implements ActionListener {
    private network.server.Server server;
    private Server view;

    public ControlerServer(Server view) {
        this.view = view;
        this.view.getConnectButton().addActionListener(this);
    }

    /**
     * On lançant une boucle ou une fonction blockante ici on va blocker l'EDT
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.view.getConnectButton()){
            // norlement je recupere les infos saisie dans les field
            this.server = new network.server.Server(this.view.getPort(),this.view.getNbPlayers());
            this.view.getConnectButton().setEnabled(false);
            try {
                this.view.setLogPanel(new LogPanel(InetAddress.getLocalHost().getHostAddress(), this.view.getPort()));
                this.view.changeView("2");
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            }
            this.server.setServerView(this.view);
            // lance un thread avec la méthode launch
            new Thread(() -> this.server.launch()).start();
        }
    }
}