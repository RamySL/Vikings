package client.view;

import server.model.Partie;

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
    private Start start;
    private ViewWaiting viewWaiting;
    private ViewPartie viewPartie;
    //private PartyConfigPanel partyConfigPanel;

    public ViewClient() {

        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension(850,600));
        this.cardLayout = new CardLayout();

        this.start = new Start();
        // A ce moment la partie n'est pas encore lancer don on met null
        this.viewPartie = new ViewPartie(this,null);
        this.viewWaiting = new ViewWaiting();
        //this.partyConfigPanel = new PartyConfigPanel();

        this.setLayout(this.cardLayout);
        this.add(this.start, "1");
        this.add(this.viewWaiting, "2");
        this.add(this.viewPartie, "3");
        //this.add(this.partyConfigPanel, "4");

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
        return start.getConnectButton();
    }

    public String getIp(){
        return this.start.getIp();
    }

    public int getPort(){
        return this.start.getPort();
    }


}

