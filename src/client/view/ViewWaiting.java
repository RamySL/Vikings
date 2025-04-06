package client.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import sharedGUIcomponents.ComposantsPerso.FontPerso;

/**
 * Vue d'attente qui s'affiche lorsqu'un client se connecte au serveur.
 * Affiche le nombre de joueurs attendus et la liste des joueurs connectés.
 */
public class ViewWaiting extends JPanel {
    private Image backgroundImage;
    private JLabel titleLabel;
    private JLabel waitingLabel;
    private JPanel playersListPanel; // Panneau contenant la liste des joueurs
    private List<PlayerInfo> connectedPlayers;
    private int maxPlayers;
    private int currentPlayers;

    /**
     * Constructeur de la vue d'attente.
     * @param maxPlayers Nombre maximum de joueurs attendus
     */
    public ViewWaiting(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.currentPlayers = 0;
        this.connectedPlayers = new ArrayList<>();

        // Chargement de l'image de fond (thème viking)
        backgroundImage = new ImageIcon("src/ressources/images/viking_waiting.jpg").getImage();

        // Configuration du layout
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Titre principal
        titleLabel = new JLabel("Salle d'attente", JLabel.CENTER);
        titleLabel.setFont(FontPerso.mvBoli(36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        // Label d'attente
        waitingLabel = new JLabel(String.format("En attente de joueurs (%d/%d)", currentPlayers, maxPlayers), JLabel.CENTER);
        waitingLabel.setFont(FontPerso.mvBoli(24));
        waitingLabel.setForeground(new Color(255, 215, 0)); // Or

        // Panneau d'en-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(waitingLabel, BorderLayout.CENTER);

        // Création du panneau des joueurs
        JPanel playersPanel = createPlayersPanel();

        // Ajout des composants au panneau principal
        add(headerPanel, BorderLayout.NORTH);
        add(playersPanel, BorderLayout.CENTER);

        // Animation de chargement
        startLoadingAnimation();
    }

    /**
     * Crée le panneau qui affiche la liste des joueurs connectés.
     */
    private JPanel createPlayersPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        containerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Panneau avec fond semi-transparent
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
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // En-tête de la liste des joueurs
        JLabel playersHeaderLabel = new JLabel("Joueurs connectés", JLabel.CENTER);
        playersHeaderLabel.setFont(FontPerso.mvBoli(22));
        playersHeaderLabel.setForeground(Color.WHITE);
        playersHeaderLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Séparateur
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(150, 150, 150));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

        // Panneau d'en-tête avec titre et séparateur
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(playersHeaderLabel, BorderLayout.NORTH);
        headerPanel.add(separator, BorderLayout.CENTER);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Panneau pour la liste des joueurs (sera mis à jour dynamiquement)
        playersListPanel = new JPanel();
        playersListPanel.setLayout(new BoxLayout(playersListPanel, BoxLayout.Y_AXIS));
        playersListPanel.setOpaque(false);

        // Placeholder pour quand aucun joueur n'est connecté
        JLabel noPlayersLabel = new JLabel("Aucun joueur connecté pour le moment...");
        noPlayersLabel.setFont(FontPerso.mvBoli(16));
        noPlayersLabel.setForeground(new Color(200, 200, 200));
        noPlayersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersListPanel.add(noPlayersLabel);

        // Panneau avec scroll pour la liste des joueurs
        JScrollPane scrollPane = new JScrollPane(playersListPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Ajout des composants au panneau interne
        innerPanel.add(headerPanel, BorderLayout.NORTH);
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        // Ajout du panneau interne au conteneur
        containerPanel.add(innerPanel, BorderLayout.CENTER);

        return containerPanel;
    }

    /**
     * Ajoute un joueur à la liste des joueurs connectés.
     * @param username Pseudo du joueur
     * @param ip Adresse IP du joueur
     */
    public void addPlayer(String username, String ip) {
        // Création d'un nouvel objet PlayerInfo
        PlayerInfo playerInfo = new PlayerInfo(username, ip);
        connectedPlayers.add(playerInfo);
        currentPlayers++;

        // Mise à jour du label d'attente
        waitingLabel.setText(String.format("En attente de joueurs (%d/%d)", currentPlayers, maxPlayers));

        // Mise à jour de la liste des joueurs
        updatePlayersList();

        // Si tous les joueurs sont connectés, on peut afficher un message
        if (currentPlayers >= maxPlayers) {
            waitingLabel.setText("Tous les joueurs sont connectés ! La partie va bientôt commencer...");
            waitingLabel.setForeground(new Color(50, 205, 50)); // Vert
        }
    }

    /**
     * Supprime un joueur de la liste des joueurs connectés.
     * @param username Pseudo du joueur à supprimer
     */
    public void removePlayer(String username) {
        for (int i = 0; i < connectedPlayers.size(); i++) {
            if (connectedPlayers.get(i).getUsername().equals(username)) {
                connectedPlayers.remove(i);
                currentPlayers--;
                break;
            }
        }

        // Mise à jour du label d'attente
        waitingLabel.setText(String.format("En attente de joueurs (%d/%d)", currentPlayers, maxPlayers));
        waitingLabel.setForeground(new Color(255, 215, 0)); // Or

        // Mise à jour de la liste des joueurs
        updatePlayersList();
    }

    /**
     * Met à jour la liste des joueurs affichée.
     */
    private void updatePlayersList() {
        // Effacement de la liste actuelle
        playersListPanel.removeAll();

        // Si aucun joueur n'est connecté, on affiche un message
        if (connectedPlayers.isEmpty()) {
            JLabel noPlayersLabel = new JLabel("Aucun joueur connecté pour le moment...");
            noPlayersLabel.setFont(FontPerso.mvBoli(16));
            noPlayersLabel.setForeground(new Color(200, 200, 200));
            noPlayersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playersListPanel.add(noPlayersLabel);
        } else {
            // Ajout de chaque joueur à la liste
            for (int i = 0; i < connectedPlayers.size(); i++) {
                PlayerInfo player = connectedPlayers.get(i);

                JPanel playerPanel = createPlayerPanel(player, i + 1);
                playersListPanel.add(playerPanel);

                // Ajouter un espace entre les joueurs (sauf pour le dernier)
                if (i < connectedPlayers.size() - 1) {
                    playersListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }

        // Rafraîchissement de l'affichage
        playersListPanel.revalidate();
        playersListPanel.repaint();
    }

    /**
     * Crée un panneau pour afficher les informations d'un joueur.
     * @param player Informations du joueur
     * @param index Numéro du joueur
     * @return Panneau contenant les informations du joueur
     */
    private JPanel createPlayerPanel(PlayerInfo player, int index) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(60, 60, 60, 180));
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2d.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Numéro du joueur
        JLabel numberLabel = new JLabel("#" + index);
        numberLabel.setFont(FontPerso.mvBoli(18));
        numberLabel.setForeground(new Color(255, 215, 0)); // Or
        numberLabel.setPreferredSize(new Dimension(40, 40));

        // Pseudo du joueur
        JLabel usernameLabel = new JLabel(player.getUsername());
        usernameLabel.setFont(FontPerso.mvBoli(18));
        usernameLabel.setForeground(Color.WHITE);

        // IP du joueur
        JLabel ipLabel = new JLabel(player.getIp());
        ipLabel.setFont(FontPerso.mvBoli(14));
        ipLabel.setForeground(new Color(180, 180, 180));

        // Panneau pour le pseudo et l'IP
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(usernameLabel);
        infoPanel.add(ipLabel);

        // Icône de statut (connecté)
        JLabel statusLabel = new JLabel("•");
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        statusLabel.setForeground(new Color(50, 205, 50)); // Vert
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setPreferredSize(new Dimension(30, 30));

        // Ajout des composants au panneau du joueur
        panel.add(numberLabel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Démarre une animation de chargement pour le label d'attente.
     */
    private void startLoadingAnimation() {
        Timer timer = new Timer(500, e -> {
            String text = waitingLabel.getText();
            if (currentPlayers < maxPlayers) {
                if (text.endsWith("...")) {
                    waitingLabel.setText(String.format("En attente de joueurs (%d/%d)", currentPlayers, maxPlayers));
                } else if (text.endsWith("..")) {
                    waitingLabel.setText(text + ".");
                } else if (text.endsWith(".")) {
                    waitingLabel.setText(text + ".");
                } else {
                    waitingLabel.setText(text + ".");
                }
            }
        });
        timer.start();
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

    /**
     * Classe interne pour stocker les informations d'un joueur.
     */
    private class PlayerInfo {
        private String username;
        private String ip;

        public PlayerInfo(String username, String ip) {
            this.username = username;
            this.ip = ip;
        }

        public String getUsername() {
            return username;
        }

        public String getIp() {
            return ip;
        }
    }

    /**
     * Définit le nombre maximum de joueurs attendus.
     * @param maxPlayers Nombre maximum de joueurs
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        waitingLabel.setText(String.format("En attente de joueurs (%d/%d)", currentPlayers, maxPlayers));
    }

    // Méthode pour tester l'apparence
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Waiting Room UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        ViewWaiting waitingView = new ViewWaiting(4);
        frame.add(waitingView);
        frame.setVisible(true);

        // Simulation de connexions de joueurs
        Timer timer = new Timer(1500, e -> {
            String[] usernames = {"Thor", "Odin", "Freya", "Loki"};
            String[] ips = {"192.168.1.101", "192.168.1.102", "192.168.1.103", "192.168.1.104"};

            int index = waitingView.currentPlayers;
            if (index < usernames.length) {
                waitingView.addPlayer(usernames[index], ips[index]);
            } else {
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }
}