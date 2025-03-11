package view;

import model.Partie;

import javax.swing.*;
import java.awt.*;

/**
 * Classe qui regroupe toutes les vues de l'application.<p>
 * - Vue de lancement
 * - Vue d'attente de connexion des joeurs
 * - Vue de la partie
 * - Vue de fin de partie etc
 */
public class ViewClient extends JPanel {
    private CardLayout cardLayout;
    private Lancement lancement;
    private ViewWaiting viewWaiting;
    private ViewPartie viewPartie;

    public ViewClient() {

        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension(800,600));
        this.cardLayout = new CardLayout();

        this.lancement = new Lancement();
        // A ce moment la partie n'est pas encore lancer don on met null
        this.viewPartie = new ViewPartie(this,null);
        this.viewWaiting = new ViewWaiting();

        this.setLayout(this.cardLayout);
        this.add(this.lancement, "1");
        this.add(this.viewWaiting, "2");
        this.add(this.viewPartie, "3");

        this.cardLayout.show(this, "1");

    }

    /**
     * Change la vue affichée, grace à son id
     * @param cardName
     */
    public void changeCard(String cardName){
        this.cardLayout.show(this, cardName);
    }

    /**
     * Enregistre une nouvelle vue
     * @param cardName
     * @param panel
     */
    public void registerNewCard(String cardName, JPanel panel){
        this.add(panel, cardName);
    }

    public ViewPartie getViewPartie() {
        return viewPartie;
    }
    /**
     * !! Cette classe ne devrait pas avoir de méthode pour actualiser la partie
     */
    public void actualisePartie(Partie partie) {
        this.viewPartie.setPartie(partie);
    }

    public JButton getConnectButton() {
        return lancement.getConnectButton();
    }

    public JButton getBroadcastButton() {
        return viewPartie.getBroadcastButton();
    }

    public JButton getUnicastButton() {
        return viewPartie.getUnicastButton();
    }

    /**
     * Vue de lancement
     */
    class Lancement extends JPanel{
        private JButton connectButton;
        private JTextField textField;

        public Lancement(){
            connectButton = new JButton("Rejoinre le server");
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

