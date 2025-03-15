package client.view;

import server.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Vue de la partie, Dessine tous les éléments du modèle
 * !!! Elle est hardcode pour deux joueurs !!!
 */


public class ViewPartie extends JPanel {
    private JButton broadcastButton, unicastButton;
    private Partie partieModel;
    private PanneauControle panneauControle;  // Ajout du champ PanneauControle

    /**
     * Constructeur de la vue de la partie
     */
    public ViewPartie(ViewClient viewClient, Partie partieModel) {
        this.partieModel = partieModel;
        // Initialisation de PanneauControle
        this.panneauControle = new PanneauControle();

        // Ajouter les boutons broadcast et unicast
        this.setLayout(new BorderLayout());
        broadcastButton = new JButton("Broadcast");
        unicastButton = new JButton("Unicast");
        this.panneauControle.setOpaque(false);

        //this.add(broadcastButton, BorderLayout.NORTH);
        //this.add(unicastButton, BorderLayout.SOUTH);
        this.add(panneauControle, BorderLayout.CENTER);  // Ajouter PanneauControle au panneau principal

    }

    // Méthode pour obtenir PanneauControle
    public PanneauControle getPanneauControle() {
        return panneauControle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // L'anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.RED);


        for (Camp camp : partieModel.getCamps()) {
            drawCamp(camp, g2);
        }
    }

    private void drawCamp(Camp camp, Graphics2D g2) {
        g2.setColor(Color.BLUE);
        if(camp.getId() == 0)
            g2.drawRect(0, 0, 390, 600);
        else{
            g2.drawRect(400, 0, 400, 600);
        }

        drawWarriors(camp.getWarriors(), g2);
        drawSheap(camp.getSheap(), g2);
        drawFarmers(camp.getFarmers(), g2);
        drawFields(camp.getFields(), g2);
    }

    private void drawSheap(ArrayList<Sheap> sheap, Graphics2D g2) {
        for (Sheap l : sheap) {

                g2.setColor(Color.YELLOW);
                g2.fillOval(l.getPosition().x + l.getCampId() * 400, l.getPosition().y, 16, 16);

        }
    }

    public void drawWarrior(Warrior warrior, Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillRect(warrior.getPosition().x + warrior.getCampId() * 400 - 8, warrior.getPosition().y - 8, 16, 16);
    }

    public void drawWarriors(ArrayList<Warrior> warriors, Graphics2D g2) {
        for (Warrior warrior : warriors) {
            drawWarrior(warrior, g2);
        }
    }

    private void drawFarmers(ArrayList<Farmer> farmers, Graphics2D g2) {
        g2.setColor(Color.ORANGE);
        for (Farmer farmer : farmers) {
            g2.fillRect(farmer.getPosition().x + farmer.getCampId() * 400 - 8, farmer.getPosition().y - 8, 16, 16);
        }
    }

    private void drawFields(ArrayList<Field> fields, Graphics2D g2) {
        g2.setColor(Color.BLACK);
        for (Field field : fields) {
            g2.drawRect(field.getPosition().x + field.getCampId() * 400 - 8, field.getPosition().y - 8, 16, 16);
            if (field.isPlanted()) {
                g2.drawString(field.getResource(), field.getPosition().x + field.getCampId() * 400, field.getPosition().y);
            }
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
        this.revalidate();
        this.repaint();

    }
}