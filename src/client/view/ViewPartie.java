package client.view;

import server.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Vue de la partie, Dessine tous les éléments du modèle
 * !!! Elle est hardcode pour deux joueurs !!!
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

        drawVikings(camp.getVikings(),g2);
        drawLivestock(camp.getLivestock(),g2);

    }

    private void drawLivestock(ArrayList<Cow> livestock, Graphics2D g2) {
        for (Livestock l : livestock) {
            if(l instanceof Cow){
                g2.setColor(Color.GREEN);
                g2.fillOval(l.getPosition().x + l.getCampId()*400, l.getPosition().y, 16, 16);
            }
            else{
                g2.setColor(Color.YELLOW);
                g2.fillOval(l.getPosition().x + l.getCampId()*400, l.getPosition().y, 16, 16);
            }

        }
    }

    public void drawViking(Warrior viking, Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillRect(viking.getPosition().x + viking.getCampId()*400 - 8, viking.getPosition().y - 8, 16, 16);
    }

    public void drawVikings(ArrayList<Warrior> vikings, Graphics2D g2) {
        for (Warrior viking : vikings) {
            drawViking(viking, g2);
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