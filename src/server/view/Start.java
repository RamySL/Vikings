package server.view;

import sharedGUIcomponents.ComposantsPerso.Buttons;
import sharedGUIcomponents.ComposantsPerso.CenteredPanel;
import sharedGUIcomponents.ComposantsPerso.FontPerso;

import javax.swing.*;
import java.awt.*;

/**
 * Vue qui aparaît au lancement de l'IU du serveur (première vue).
 */
public class Start extends JPanel {
    private Image backgroundImage;
    private Buttons.BouttonHorsJeu connectButton;
    private GameConfig gameConfig;

    public Start() {

        backgroundImage = new ImageIcon("src/ressources/images/background_server.jpg").getImage();
        this.connectButton = new Buttons.BouttonHorsJeu("Lancer le server");
        this.gameConfig = new GameConfig();

        this.setLayout(new BorderLayout());
        CenteredPanel.centerArrangement(this);

        this.add(connectButton, BorderLayout.NORTH);
        this.add(gameConfig, BorderLayout.CENTER);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(),this);
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public int getPort(){
        return Integer.parseInt(this.gameConfig.fieldPort.getText());
    }

    public int getNbPlayers(){
        return Integer.parseInt(this.gameConfig.fieldNbPlayers.getText());
    }

    /**
     * La partie au centre qui contient les textfields pour le port du serveur et confguration du jeu.
     */
    class GameConfig extends JPanel{
        protected JTextField fieldPort;
        protected JTextField fieldNbPlayers;
        private Buttons.BouttonHorsJeu startButton;

        public GameConfig() {

            this.setOpaque(false);
            this.setLayout(new GridLayout(10,1));

            JPanel panelPort = new JPanel();
            panelPort.setOpaque(false);
            panelPort.setLayout(new FlowLayout(FlowLayout.CENTER));
            JLabel labelPort = new JLabel("Port du serveur   ");
            labelPort.setFont(FontPerso.mvBoli(20));
            panelPort.add(labelPort);
            labelPort.setForeground(Color.WHITE);
            this.fieldPort = new JTextField("1234");
            this.fieldPort.setPreferredSize(new Dimension(40,30));
            panelPort.add(this.fieldPort);

            JPanel panelNbPlayers = new JPanel();
            panelNbPlayers.setOpaque(false);
            panelNbPlayers.setLayout(new FlowLayout(FlowLayout.CENTER));
            this.fieldNbPlayers = new JTextField("2");
            this.fieldNbPlayers.setPreferredSize(new Dimension(40,30));
            JLabel labelNbPlayers = new JLabel("Nombre de joueurs");
            labelNbPlayers.setFont(FontPerso.mvBoli(20));
            panelNbPlayers.add(labelNbPlayers);
            labelNbPlayers.setForeground(Color.WHITE);
            panelNbPlayers.add(this.fieldNbPlayers);

            this.add(panelPort);
            this.add(panelNbPlayers);

        }

    }
}