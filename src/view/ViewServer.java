package view;

import javax.swing.*;
import java.awt.*;

/**
 * Classe pricipale pour l'interface graphique du serveur.
 */
public class ViewServer extends JPanel {
    private CardLayout cardLayout;
    private Lancement lancement;

    public ViewServer() {
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension(800,600));
        this.cardLayout = new CardLayout();
        this.lancement = new Lancement();
        this.setLayout(this.cardLayout);
        this.add(this.lancement, "1");
        this.cardLayout.show(this, "1");
    }

    public CardLayout getCardLayout() {
        return cardLayout;

    }

    public JButton getConnectButton() {
        return lancement.getConnectButton();
    }

    class Lancement extends JPanel{
        private JButton connectButton;
        private JTextField textField;

        public Lancement(){
            connectButton = new JButton("Lancer le server");
            textField = new JTextField();
            this.textField.setPreferredSize(new Dimension(200, 50));
            this.add(connectButton);
            this.add(textField);
        }

        public JButton getConnectButton() {
            return connectButton;
        }
    }

}

