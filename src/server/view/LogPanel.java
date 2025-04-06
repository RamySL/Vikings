package server.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.*;
import java.net.InetAddress;

public class LogPanel extends JPanel {
    private JTextPane logTextPane;
    private StyledDocument doc;
    private JList<String> playersList;
    private DefaultListModel<String> playersModel;
    private JLabel addressLabel;
    private JLabel portLabel;

    // Styles pour les différents types de messages
    private Style receivedStyle;
    private Style sentStyle;
    private Style infoStyle;

    public LogPanel(String serverAddress, int serverPort) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel d'information du serveur (en haut)
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        addressLabel = new JLabel("Adresse: " + serverAddress);
        portLabel = new JLabel("Port: " + serverPort);
        infoPanel.add(addressLabel);
        infoPanel.add(portLabel);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informations serveur"));
        add(infoPanel, BorderLayout.NORTH);

        // Zone de logs (au centre)
        logTextPane = new JTextPane();
        logTextPane.setEditable(false);
        doc = logTextPane.getStyledDocument();

        // Définition des styles
        receivedStyle = logTextPane.addStyle("Received", null);
        StyleConstants.setForeground(receivedStyle, new Color(0, 128, 0)); // Vert

        sentStyle = logTextPane.addStyle("Sent", null);
        StyleConstants.setForeground(sentStyle, new Color(0, 0, 200)); // Bleu

        infoStyle = logTextPane.addStyle("Info", null);
        StyleConstants.setForeground(infoStyle, new Color(222, 122, 0)); // Rouge foncé
        StyleConstants.setBold(infoStyle, true);

        JScrollPane logScrollPane = new JScrollPane(logTextPane);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Logs du serveur"));
        add(logScrollPane, BorderLayout.CENTER);

        // Liste des joueurs (à droite)
        playersModel = new DefaultListModel<>();
        playersList = new JList<>(playersModel);
        JScrollPane playersScrollPane = new JScrollPane(playersList);
        playersScrollPane.setPreferredSize(new Dimension(200, 0));
        playersScrollPane.setBorder(BorderFactory.createTitledBorder("Joueurs connectés"));
        add(playersScrollPane, BorderLayout.EAST);

        // Légende (en bas)
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel sentLabel = new JLabel("Paquets envoyés");
        sentLabel.setForeground(new Color(0, 0, 200));

        JLabel receivedLabel = new JLabel("Paquets reçus");
        receivedLabel.setForeground(new Color(0, 128, 0));

        legendPanel.add(sentLabel);
        legendPanel.add(new JLabel(" | "));
        legendPanel.add(receivedLabel);

        add(legendPanel, BorderLayout.SOUTH);
    }

    // Ajouter un message de paquet reçu
    public void logReceivedPacket(String from, String message) {
        try {
            doc.insertString(doc.getLength(),
                    getTimestamp() + " [REÇU de " + from + "]: " + message + "\n",
                    receivedStyle);
            scrollToBottom();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Ajouter un message de paquet envoyé
    public void logSentPacket(String to, String message) {
        try {
            doc.insertString(doc.getLength(),
                    getTimestamp() + " [ENVOYÉ à " + to + "]: " + message + "\n",
                    sentStyle);
            scrollToBottom();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Ajouter un message d'information
    public void logInfo(String message) {
        try {
            doc.insertString(doc.getLength(),
                    getTimestamp() + " [INFO]: " + message + "\n",
                    infoStyle);
            scrollToBottom();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Ajouter un joueur à la liste
    public void addPlayer(String username, String ip) {
        playersModel.addElement(username + " (" + ip + ")");
    }


    // Obtenir un timestamp pour les logs
    private String getTimestamp() {
        return String.format("[%tT]", new Date());
    }

    // Faire défiler automatiquement vers le bas
    private void scrollToBottom() {
        logTextPane.setCaretPosition(doc.getLength());
    }

}