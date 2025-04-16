package client.view;

import sharedGUIcomponents.ComposantsPerso.FontPerso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewEndGame extends JPanel {
    public ViewEndGame(String message, List<Integer> winningCampIds, List<String> playerNames) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Title
        JLabel endLabel = new JLabel(message, JLabel.CENTER);
        endLabel.setFont(FontPerso.mvBoli(32));
        endLabel.setForeground(Color.WHITE);
        endLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(endLabel, BorderLayout.NORTH);

        // Call the updated setTable method
        setTable(winningCampIds, playerNames);

        // Quit button
        JButton quitButton = new JButton("Quitter");
        quitButton.setFont(FontPerso.mvBoli(20));
        quitButton.setFocusPainted(false);
        quitButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Méthode pour gérer l'affichage du tableau
    public void setTable(List<Integer> winningCampIds, List<String> playerNames) {
        String[] columnNames = {"Rang", "Camp ID", "Player Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        if (winningCampIds != null && playerNames != null) {
            for (int i = 0; i < winningCampIds.size(); i++) {
                int campId = winningCampIds.get(i);
                String playerName = playerNames.get(i);
                String[] row = {String.valueOf(i + 1), String.valueOf(campId), playerName};
                tableModel.addRow(row);
            }
        }

        JTable table = new JTable(tableModel);
        table.setFont(FontPerso.mvBoli(20));
        table.setForeground(Color.WHITE);
        table.setBackground(Color.BLACK);
        table.setGridColor(Color.WHITE);
        table.setSelectionBackground(Color.GRAY);

        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
    }
}
