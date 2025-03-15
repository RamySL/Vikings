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

        drawVikings(camp.getVikings(), g2);
        drawLivestock(camp.getLivestock(), g2);
        drawFarmers(camp.getFarmers(), g2);
        drawFields(camp.getFields(), g2);
    }

    private void drawLivestock(ArrayList<Cow> livestock, Graphics2D g2) {
        for (Livestock l : livestock) {
            if(l instanceof Cow){
                g2.setColor(Color.GREEN);
                g2.fillOval(l.getPosition().x + l.getCampId() * 400, l.getPosition().y, 16, 16);
            } else {
                g2.setColor(Color.YELLOW);
                g2.fillOval(l.getPosition().x + l.getCampId() * 400, l.getPosition().y, 16, 16);
            }
        }
    }

    public void drawViking(Warrior viking, Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillRect(viking.getPosition().x + viking.getCampId() * 400 - 8, viking.getPosition().y - 8, 16, 16);
    }

    public void drawVikings(ArrayList<Warrior> vikings, Graphics2D g2) {
        for (Warrior viking : vikings) {
            drawViking(viking, g2);
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