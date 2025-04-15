package sharedGUIcomponents.ComposantsPerso;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BarreDeTemps extends JComponent {
    private int tempsMax;
    private int tempsActuelle;
    private BufferedImage horlogeImage;

    public BarreDeTemps(int tempsMax) {
        this.tempsMax = tempsMax;
        this.tempsActuelle = tempsMax;
        this.setPreferredSize(new Dimension(200, 40));
        try {
            horlogeImage = ImageIO.read(getClass().getResource("/assets/horloge_pixel.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Image de l'horloge introuvable !");
            horlogeImage = null;
        }
    }

    public void setTemps(int nouveauTemps) {
        this.tempsActuelle = Math.max(0, Math.min(nouveauTemps, tempsMax));
        repaint();
    }

    public void setTempsMax(int nouveauTempsMax) {
        this.tempsMax = nouveauTempsMax;
        this.tempsActuelle = Math.min(tempsActuelle, tempsMax);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int horlogeAfficheLargeur = 40;
        int horlogeAfficheHauteur = 40;
        Graphics2D g2 = (Graphics2D) g.create();

        int largeur = getWidth();
        int hauteur = getHeight();

        int padding = 5;
        int horlogeLargeur = horlogeAfficheLargeur;
        int horlogeHauteur = horlogeAfficheHauteur;

        // Position du cœur



        int barreX = padding + horlogeLargeur -15;
        int barreLargeur = largeur - barreX - padding;
        int barreHauteur = 12;
        int barreY = (hauteur - barreHauteur) / 2;

        // Calcul du ratio de temps
        float ratio = (float) tempsActuelle / tempsMax;
        int largeurTemps = (int) (barreLargeur * ratio);
        int minutes = tempsActuelle / 60;
        int secondes = tempsActuelle % 60;
        String texteTemps = String.format("%02d:%02d", minutes, secondes);

        Font font = new Font("mvBoli", Font.BOLD, 10);
        g2.setFont(font);
        FontMetrics metrics = g2.getFontMetrics(font);
        int textWidth = metrics.stringWidth(texteTemps);
        int textHeight = metrics.getHeight();

        int textX = barreX + (barreLargeur - textWidth) / 2;
        int textY = barreY + ((barreHauteur - textHeight) / 2) + metrics.getAscent();





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

        g2.setColor(couleur);
        g2.fillRoundRect(barreX, barreY, largeurTemps, barreHauteur,10, 10);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(barreX, barreY, barreLargeur, barreHauteur,10, 10);
        if (horlogeImage != null) {


            g2.drawImage(horlogeImage, padding, (hauteur - horlogeAfficheHauteur) / 2,
                    horlogeAfficheLargeur, horlogeAfficheHauteur, null);
        }
        if (ratio < 0.3f) {
            g2.setColor(Color.WHITE);
        } else {
            g2.setColor(Color.BLACK);
        }
        g2.drawString(texteTemps, textX, textY);

        g2.dispose();
    }
}
