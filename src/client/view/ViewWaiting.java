package client.view;

import javax.swing.*;

/**
 * Ecran attente de connexion des joueurs, liste ceux connecté avec leur IP
 */
public class ViewWaiting extends JPanel {
    private JLabel label;

    public ViewWaiting() {
        this.label = new JLabel("En attente de connexion");
        this.add(label);
    }
}
