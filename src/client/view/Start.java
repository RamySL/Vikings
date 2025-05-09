package client.view;

import sharedGUIcomponents.ComposantsPerso.Buttons;
import sharedGUIcomponents.ComposantsPerso.FontPerso;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Vue qui apparaît au lancement de l'IU du client (première vue).
 */
public class Start extends JPanel {
    private Image backgroundImage;
    private Buttons.BouttonHorsJeu connectButton;
    private GameConfig gameConfig;
    private JLabel titleLabel;

    public Start() {
        // Chargement de l'image de fond
        backgroundImage = new ImageIcon("src/ressources/images/background_client.jpg").getImage();

        // Configuration du layout principal
        setLayout(new BorderLayout(0, 20));
        setBorder(new EmptyBorder(40, 60, 40, 60));

        // Titre principal
        titleLabel = new JLabel("Connexion au Serveur", JLabel.CENTER);
        titleLabel.setFont(FontPerso.mvBoli(32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 0, 30, 0));

        // Création du panneau de configuration
        gameConfig = new GameConfig();

        // Bouton de connexion
        connectButton = new Buttons.BouttonHorsJeu("Rejoindre le serveur");
        connectButton.setFont(FontPerso.mvBoli(18));
        connectButton.setPreferredSize(new Dimension(250, 50));

        // Panneau pour centrer le bouton
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(connectButton);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        // Ajout des composants au panneau principal
        add(titleLabel, BorderLayout.NORTH);
        add(gameConfig, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessin de l'image de fond avec un effet d'assombrissement
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Overlay semi-transparent pour améliorer la lisibilité
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.dispose();
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public String getIp() {
        return this.gameConfig.fieldIp.getText();
    }

    public int getPort() {
        return Integer.parseInt(this.gameConfig.fieldPort.getText());
    }


    /**
     * La partie au centre qui contient les textfields pour l'IP et le port du serveur.
     */
    class GameConfig extends JPanel {
        protected JTextField fieldIp;
        protected JTextField fieldPort;
        protected JTextField fieldUsername;

        public GameConfig() {
            setOpaque(false);
            setLayout(new GridBagLayout());
            setBorder(new EmptyBorder(10, 30, 10, 30));

            // Création d'un panneau interne avec fond semi-transparent
            JPanel innerPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(30, 30, 30, 200));
                    g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                    g2d.dispose();
                }
            };

            innerPanel.setOpaque(false);
            innerPanel.setLayout(new GridBagLayout());
            innerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            // Configuration de l'IP
            JLabel labelIp = createLabel("IP du serveur");
            fieldIp = createTextField("127.0.0.1");

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.weightx = 0.4;
            innerPanel.add(labelIp, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.6;
            innerPanel.add(fieldIp, gbc);

            // Configuration du port
            JLabel labelPort = createLabel("Port du serveur");
            fieldPort = createTextField("12340");

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0.4;
            innerPanel.add(labelPort, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.6;
            innerPanel.add(fieldPort, gbc);

            // Nom d'utilisateur (nouvelle option)
            JLabel labelUsername = createLabel("Pseudo");
            fieldUsername = createTextField("Joueur");

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 0.4;
            innerPanel.add(labelUsername, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.6;
            innerPanel.add(fieldUsername, gbc);

            // Ajout du panneau interne au panneau principal
            add(innerPanel);
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(FontPerso.mvBoli(18));
            label.setForeground(Color.WHITE);
            return label;
        }

        private JTextField createTextField(String defaultText) {
            JTextField field = new JTextField(defaultText);
            field.setFont(FontPerso.mvBoli(16));
            field.setPreferredSize(new Dimension(180, 35));
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            return field;
        }
    }

    public String getUsername() {
        return this.gameConfig.fieldUsername.getText();
    }

}