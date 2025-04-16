package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class EndGameScreenModern extends JFrame {

    public EndGameScreenModern(List<String> classementJoueurs) {
        setTitle("Fin de la partie");
        setSize(640, 500);
        setLocationRelativeTo(null);
        setUndecorated(true); // sans bordure
        setShape(new RoundRectangle2D.Double(0, 0, 640, 500, 40, 40));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel background = new JPanel();
        background.setBackground(new Color(25, 25, 25));
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        background.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Titre stylisé
        JLabel title = new JLabel("CLASSEMENT DES VIKINGS", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setForeground(new Color(222, 184, 135));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        background.add(title);
        background.add(Box.createRigidArea(new Dimension(0, 20)));

        // Classement
        JPanel classementPanel = new JPanel();
        classementPanel.setOpaque(false);
        classementPanel.setLayout(new GridLayout(classementJoueurs.size(), 1, 0, 15));

        for (int i = 0; i < classementJoueurs.size(); i++) {
            String joueur = classementJoueurs.get(i);
            classementPanel.add(createRankCard(i + 1, joueur));
        }

        background.add(classementPanel);
        background.add(Box.createRigidArea(new Dimension(0, 30)));

        // Boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));

        JButton replayBtn = createStyledButton("Rejouer");
        JButton quitBtn = createStyledButton("Quitter");

        replayBtn.addActionListener(e -> {
            System.out.println("Rejouer...");
            dispose();
        });

        quitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(replayBtn);
        buttonPanel.add(quitBtn);
        background.add(buttonPanel);

        setContentPane(background);
    }

    private JPanel createRankCard(int rank, String name) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(40, 40, 40));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160, 82, 45), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        JLabel label = new JLabel(rank + " - " + name);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Serif", Font.PLAIN, 22));
        card.add(label, BorderLayout.CENTER);
        return card;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setForeground(new Color(255, 255, 255));
        button.setBackground(new Color(60, 60, 60));
        button.setBorder(BorderFactory.createLineBorder(new Color(222, 184, 135), 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    // Test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<String> classement = List.of("Ragnar", "Lagertha", "Bjorn", "Floki");
            new EndGameScreenModern(classement).setVisible(true);
        });
    }
}
