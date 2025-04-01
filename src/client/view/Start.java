package client.view;

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

        backgroundImage = new ImageIcon("src/ressources/images/background_client.jpg").getImage();
        this.connectButton = new Buttons.BouttonHorsJeu("Rejoindre le server");
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

    public String getIp(){
        return this.gameConfig.fieldIp.getText();
    }

    public int getPort(){
        return Integer.parseInt(this.gameConfig.fieldPort.getText());
    }

    /**
     * La partie au centre qui contient les textfields pour le port du serveur et confguration du jeu.
     */
    class GameConfig extends JPanel{
        protected JTextField fieldIp;
        protected JTextField fieldPort;
        private Buttons.BouttonHorsJeu startButton;

        public GameConfig() {

            this.setOpaque(false);
            this.setLayout(new GridLayout(10,1));

            JPanel panelIp = new JPanel();
            panelIp.setOpaque(false);
            panelIp.setLayout(new FlowLayout(FlowLayout.CENTER));
            JLabel ipLabel = new JLabel("IP du serveur   ");
            ipLabel.setFont(FontPerso.mvBoli(20));
            panelIp.add(ipLabel);
            ipLabel.setForeground(Color.WHITE);
            this.fieldIp = new JTextField("127.0.0.1");
            this.fieldIp.setPreferredSize(new Dimension(60,30));
            panelIp.add(this.fieldIp);

            JPanel panelPort = new JPanel();
            panelPort.setOpaque(false);
            panelPort.setLayout(new FlowLayout(FlowLayout.CENTER));
            this.fieldPort = new JTextField("50123");
            this.fieldPort.setPreferredSize(new Dimension(60,30));
            JLabel labelPort = new JLabel("Port du serveur");
            labelPort.setFont(FontPerso.mvBoli(20));
            panelPort.add(labelPort);
            labelPort.setForeground(Color.WHITE);
            panelPort.add(this.fieldPort);

            this.add(panelIp);
            this.add(panelPort);

        }

    }
}