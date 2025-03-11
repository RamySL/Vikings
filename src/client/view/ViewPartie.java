package client.view;

import server.model.Mouton;
import server.model.Partie;

import javax.swing.*;
import java.awt.*;

/**
 * Vue de la partie, Dessine tous les éléments du modèle
 */
public class ViewPartie extends JPanel{
    private JButton broadcastButton, unicastButton;
    private Partie partieModel;

    /**
     * ? a quoi sert viewCient ?
     * @param viewClient
     * @param partieModel
     */
    public ViewPartie(ViewClient viewClient,Partie partieModel) {
        this.partieModel = partieModel;
        // ajouter les boutons broadcast et unicast
        this.setLayout(new BorderLayout());
        broadcastButton = new JButton("Broadcast");
        unicastButton = new JButton("Unicast");

        //this.add(broadcastButton, BorderLayout.NORTH);
        //this.add(unicastButton, BorderLayout.NORTH);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // l'antialiasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);
        Color[] couleurs = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA};
        for (Mouton mouton : partieModel.getMoutons()) {
            g2.setColor(couleurs[mouton.getId()]);
            g2.fillOval(mouton.getX(), mouton.getY(), 50, 50);
        }
    }

    public JButton getBroadcastButton() {
        return broadcastButton;
    }

    public JButton getUnicastButton() {
        return unicastButton;
    }

    public void setPartie(Partie partieModel) {
        this.partieModel = partieModel;
    }


}