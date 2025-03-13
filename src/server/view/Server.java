package server.view;

import javax.swing.*;
import java.awt.*;

/**
 * Classe pricipale pour l'interface graphique du serveur.
 */
public class Server extends JPanel {
    private CardLayout cardLayout;
    private Start start;

    public Server() {
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension(800,600));
        this.cardLayout = new CardLayout();
        this.start = new Start();
        this.setLayout(this.cardLayout);
        this.add(this.start, "1");
        this.cardLayout.show(this, "1");
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JButton getConnectButton() {
        return this.start.getConnectButton();
    }

    public int getPort(){
        return this.start.getPort();
    }

    public int getNbPlayers(){
        return this.start.getNbPlayers();
    }

}

