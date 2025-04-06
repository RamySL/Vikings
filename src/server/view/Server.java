package server.view;

import javax.swing.*;
import java.awt.*;

/**
 * Classe pricipale pour l'interface graphique du serveur.
 */
public class Server extends JPanel {
    private CardLayout cardLayout;
    private Start start;
    private LogPanel logPanel;

    public Server() {
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension(1000,600));
        this.cardLayout = new CardLayout();
        this.start = new Start();
        this.setLayout(this.cardLayout);
        this.add(this.start, "1");
        this.cardLayout.show(this, "1");
    }

    // changer de vue generique
    public void changeView(String viewName) {
        this.cardLayout.show(this, viewName);
    }

    public void setLogPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
        this.add(this.logPanel, "2");
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

