package experiments;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class CustomImageButton {
    public static void main(String[] args) {
        // Création de la fenêtre
        JFrame frame = new JFrame("Bouton Image Transparent");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Création du panel
        JPanel panel = new JPanel();
        panel.setLayout(null); // Permet de placer librement les composants

        try {
            // Charger l'image
            BufferedImage img = ImageIO.read(new File("src/experiments/attack_0.png")); // Remplace avec ton chemin

            // Créer un bouton personnalisé avec l'image
            JButton button = new ImageButton(img);
            button.setBounds(50, 50, img.getWidth(), img.getHeight()); // Adapter la taille à l'image

            // Ajouter le bouton au panel
            panel.add(button);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ajouter le panel à la fenêtre
        frame.add(panel);
        frame.setVisible(true);
    }
}

// Classe personnalisée pour un bouton qui suit la transparence de l'image
class ImageButton extends JButton {
    private BufferedImage image;

    public ImageButton(BufferedImage img) {
        this.image = img;
        setIcon(new ImageIcon(img));
        setBorderPainted(true);
        setContentAreaFilled(true);
        setFocusPainted(false);
        setOpaque(false);

        // Ajoute un effet de surbrillance au survol
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    @Override
    public boolean contains(int x, int y) {
        // Vérifie si les coordonnées du clic sont sur un pixel non transparent
        if (x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight()) {
            return false;
        }
        int alpha = (image.getRGB(x, y) >> 24) & 0xff; // Récupère la valeur alpha du pixel
        return alpha > 0; // Si alpha > 0, le pixel est visible donc cliquable
    }
}
