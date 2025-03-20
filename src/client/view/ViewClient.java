package client.view;

import client.controler.ControlerClient;
import server.model.Partie;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
    private ControlerClient controlerClient;
    private JButton connectButton;

    public ViewClient() {
        this.setPreferredSize(new Dimension(ViewPartie.WIDTH_VIEW, ViewPartie.HEIGHT_VIEW));
        this.cardLayout = new CardLayout();

        this.start = new Start();
        this.viewPartie = new ViewPartie(this, null);
        this.viewWaiting = new ViewWaiting();

        this.setLayout(this.cardLayout);
        this.add(this.start, "1");
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
     *  Cette classe ne devrait pas avoir de méthode pour actualiser la partie
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

