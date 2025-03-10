package Vue;

import javax.swing.*;
import java.awt.*;
import Modele2.Ble;

public class PanelBle extends JPanel {
    private Ble ble;
    private JButton boutonArroser;
    private JProgressBar barreRendement;

    public PanelBle(Ble ble) {
        this.ble = ble;
        setLayout(new BorderLayout());

        barreRendement = new JProgressBar(0, 1000);
        barreRendement.setStringPainted(true);
        barreRendement.setValue(0);
        add(barreRendement, BorderLayout.NORTH);

        boutonArroser = new JButton("Arroser");
        add(boutonArroser, BorderLayout.SOUTH);

        Timer timer = new Timer(1000, e -> repaint());
        timer.start();
    }

    public Ble getBle() {
        return this.ble;
    }

    public void setBle(Ble ble) {
        this.ble = ble;
        repaint(); 
    }

    public JButton getBoutonArroser() {
        return boutonArroser;
    }

    public JProgressBar getBarreRendement() {
        return barreRendement;
    }

    public void updateRendement(int rendement) {
        barreRendement.setValue(rendement);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (ble == null || ble.estMort() || ble.getTaille() == 0) {
            return;  
        }

        int diameter = ble.getTaille();
        if (diameter < 10) {
            diameter = 10;
        }

        g.setColor(Color.GREEN);
        g.fillOval(getWidth() / 2 - diameter / 2, getHeight() / 2 - diameter / 2, diameter, diameter);
        g.setColor(Color.BLACK);
        g.drawString("Santé : " + ble.getSante(), getWidth() / 2 - diameter / 2, getHeight() / 2 - diameter / 2 - 5);
    }

    
    public void updateBle(Ble newBle) {
        this.ble = newBle;
        repaint();
    }
}
