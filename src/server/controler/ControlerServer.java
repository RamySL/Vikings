package server.controler;

import server.view.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Gère les évenements déclenchés par l'utilisateur en interagissant avec l'interface graphique
 * du serveur. Lance le thread du serveur quand l'utilisateur clique sur le bouton.
 */
public class ControlerServer implements ActionListener {
    private network.Server server;
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
            this.server = new network.Server(1234,2);
            // lance un thread avec la méthode launch
            new Thread(() -> this.server.launch()).start();
        }
    }
}