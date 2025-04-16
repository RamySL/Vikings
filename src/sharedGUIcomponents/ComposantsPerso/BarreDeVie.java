package sharedGUIcomponents.ComposantsPerso;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BarreDeVie extends JComponent {
    private int vieMax;
    private int vieActuelle;
    private BufferedImage coeurImage;

    public BarreDeVie(int vieMax) {
        this.vieMax = vieMax;
        this.vieActuelle = vieMax;
        this.setPreferredSize(new Dimension(200, 20)); // ajustable selon le sprite
        try {
            coeurImage = ImageIO.read(getClass().getResource("/assets/coeur_pixel.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Image du cœur introuvable !");
            coeurImage = null;
        }
    }

    public void setVie(int nouvelleVie) {
        this.vieActuelle = Math.max(0, Math.min(nouvelleVie, vieMax));
        repaint();
    }

    public void setVieMax(int nouvelleVieMax) {
        this.vieMax = nouvelleVieMax;
        this.vieActuelle = Math.min(vieActuelle, vieMax);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int coeurAfficheLargeur = 40;
        int coeurAfficheHauteur = 40;
        Graphics2D g2 = (Graphics2D) g.create();

        int largeur = getWidth();
        int hauteur = getHeight();

        int padding = 5;
        int coeurLargeur = coeurAfficheLargeur;
        int coeurHauteur = coeurAfficheHauteur;

        // Position du cœur



        int barreX = padding + coeurLargeur -15;
        int barreLargeur = largeur - barreX - padding;
        int barreHauteur = 12;
        int barreY = (hauteur - barreHauteur) / 2;

        // Calcul du ratio de vie
        float ratio = (float) vieActuelle / vieMax;
        int largeurVie = (int) (barreLargeur * ratio);

        // Déterminer la couleur (verte > orange > rouge)
        Color couleur;
        if (ratio > 0.6f) {
            couleur = new Color(0x00cc00); // Vert
        } else if (ratio > 0.3f) {
            couleur = new Color(0xffa500); // Orange
        } else {
            couleur = new Color(0xcc0000); // Rouge
        }

        // Dessiner le fond de la barre
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(barreX, barreY, barreLargeur, barreHauteur,10, 10);

        // Dessiner la barre de vie actuelle
        g2.setColor(couleur);
        g2.fillRoundRect(barreX, barreY, largeurVie, barreHauteur,10, 10);

        // Bordure noire
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(barreX, barreY, barreLargeur, barreHauteur,10, 10);
        if (coeurImage != null) {
            //g2.drawImage(coeurImage, padding, (hauteur - coeurHauteur) / 2, null);


            g2.drawImage(coeurImage, padding, (hauteur - coeurAfficheHauteur) / 2,
                    coeurAfficheLargeur, coeurAfficheHauteur, null);
        }

        g2.dispose();
    }
}
